package com.fileloader.service;

import com.fileloader.dao.FileLoaderDao;
import com.fileloader.models.CorruptDeal;
import com.fileloader.models.CurrencyDealCount;
import com.fileloader.models.Deal;
import com.fileloader.models.FileStatus;
import com.fileloader.util.FileLoaderUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyObject;

public class FileLoaderServiceTest {

    @Mock
    private FileLoaderDao dao;

    @Mock
    private FileLoaderUtil util;

    @InjectMocks
    FileLoaderService fileLoaderService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllDeals(){
        List<Deal> deals = new ArrayList<>();
        deals.add(new Deal(1,"USD","GBP", new Timestamp(new Date().getTime()),100d,"deals.csv"));
        Mockito.when(dao.getAllDeals()).thenReturn(deals);
        Assert.assertArrayEquals(deals.toArray(),((List<Deal>)fileLoaderService.getAllDeals()).toArray());
    }

    @Test
    public void testGetAllFiles(){
        List<FileStatus> files = new ArrayList<>();
        files.add(new FileStatus("deals.csv", "Done"));
        Mockito.when(dao.getAllFiles()).thenReturn(files);
        Assert.assertArrayEquals(files.toArray(),((List<FileStatus>)fileLoaderService.getAllFiles()).toArray());
    }

    @Test
    public void testGetAllCurrencyDealCount() throws ExecutionException, InterruptedException {
        List<CurrencyDealCount> currencyDealCounts = new ArrayList<>();
        currencyDealCounts.add(new CurrencyDealCount("USD", 100));
        Future<List<CurrencyDealCount>> future = CompletableFuture.completedFuture(currencyDealCounts);
        Mockito.when(dao.getCurrencyDealsCount()).thenReturn(future);
        Assert.assertArrayEquals(currencyDealCounts.toArray(),((List<CurrencyDealCount>)fileLoaderService.getAllCurrencyDealCount()).toArray());
    }

    @Test
    public void testGetAllDealsAssociatedWithAFile(){
        List<Deal> deals = new ArrayList<>();
        deals.add(new Deal(1,"USD","GBP", new Timestamp(new Date().getTime()),100d,"deals.csv"));
        Mockito.when(dao.getAllDeals("deals.csv")).thenReturn(deals);
        Assert.assertArrayEquals(deals.toArray(), fileLoaderService.getAllDeals("deals.csv").toArray());
    }

    @Test
    public void testGetAllCorruptDealsAssociatedWithAFile(){
        List<CorruptDeal> corruptDeals = new ArrayList<>();
        corruptDeals.add(new CorruptDeal("1,USD,GBP, ,100d,", "deals.csv", "Missing field"));
        Mockito.when(dao.getAllCorruptDeals("deals.csv")).thenReturn(corruptDeals);
        Assert.assertArrayEquals(corruptDeals.toArray(), fileLoaderService.getAllCorruptDeals("deals.csv").toArray());
    }

    @Test
    public void testLoad() throws Exception {
        String[] items = new String[]{"1", "USD", "GBP", "2016-02-03 00:00:00.0", "100"};
        MockMultipartFile file = new MockMultipartFile("deals", "deals.csv", "text/plain", "1,USD,GBP,2016-02-03 00:00:00.0,100".getBytes());
        List<CurrencyDealCount> currencyDealCounts = new ArrayList<>();
        currencyDealCounts.add(new CurrencyDealCount("USD", 100));
        Future<List<CurrencyDealCount>> future = CompletableFuture.completedFuture(currencyDealCounts);
        Mockito.when(dao.getCurrencyDealsCount()).thenReturn(future);
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Mockito.when(util.syncPrepareDeal(items, "deals.csv")).thenReturn(new Deal(1, "USD", "GBP", timestamp, 100d, "deals.csv"));
        Mockito.doNothing().when(dao).uploadFileStatus(anyObject());
        Mockito.doNothing().when(dao).uploadDealData((HashMap<String, List<Deal>>) anyMap(), anyList(), anyObject());
        Mockito.doNothing().when(dao).uploadDealCounts((HashMap<String, List<Deal>>) anyMap(),anyList());
        fileLoaderService.load(file);
        List<Deal> deals = new ArrayList<>();
        deals.add(new Deal(1, "USD", "GBP", timestamp, 100d, "deals.csv"));
        HashMap<String, List<Deal>> currencyDeals = new HashMap<>();
        currencyDeals.put("USD", deals);
        FileStatus fileStatus = new FileStatus("deals.csv", "PROCESSING");
        Mockito.verify(dao, Mockito.times(1)).uploadDealCounts(currencyDeals,future.get());
        Mockito.verify(dao, Mockito.times(1)).uploadDealData(currencyDeals, new ArrayList<CorruptDeal>(), fileStatus);
        Mockito.verify(util, Mockito.times(1)).syncPrepareDeal(items, "deals.csv");
    }
}