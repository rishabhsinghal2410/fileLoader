package com.fileloader.service;

import com.fileloader.dao.FileLoaderDao;
import com.fileloader.models.CorruptDeal;
import com.fileloader.models.CurrencyDealCount;
import com.fileloader.models.Deal;
import com.fileloader.models.FileStatus;
import com.fileloader.util.FileLoaderUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class FileLoaderService {

    private final Logger LOG = Logger.getLogger(FileLoaderService.class);

    @Autowired
    private FileLoaderDao fileLoaderDao;

    @Autowired
    private FileLoaderUtil util;

    public Iterable<Deal> getAllDeals(){
        return fileLoaderDao.getAllDeals();
    }

    //@Cacheable(value = "files")
    public List<FileStatus> getAllFiles(){
        return fileLoaderDao.getAllFiles();
    }

    public List<CurrencyDealCount> getAllCurrencyDealCount() throws ExecutionException, InterruptedException {
        return fileLoaderDao.getCurrencyDealsCount().get();
    }

    //@Cacheable(value = "deals", unless="#result.size()>64")
    public List<Deal> getAllDeals(String fileName){
        return fileLoaderDao.getAllDeals(fileName);
    }

    //@Cacheable(value = "corrupt", unless="#result.size()>64")
    public List<CorruptDeal> getAllCorruptDeals(String fileName){
        return fileLoaderDao.getAllCorruptDeals(fileName);
    }

    //@CacheEvict(value = "files", allEntries = true)
    public void load(MultipartFile file) throws IOException, ExecutionException, InterruptedException {
        String fileName = file.getOriginalFilename();
        FileStatus fileStatus = setInitialFileStatus(fileName);
        //List<Deal> deals = new ArrayList<>();
        HashMap<String, List<Deal>> currencyCodeDeals = new HashMap<>();
        List<CorruptDeal> corruptDeals = new ArrayList<>();
        Future<List<CurrencyDealCount>> existingCurrencyDealCount = fileLoaderDao.getCurrencyDealsCount();
        if (!file.isEmpty()) {
            String content = new String(file.getBytes());
            String[] rows =  content.split("\n");
            for (String row: rows) {
                String[] items = row.split(",");
                if(items.length == 5){
                    try {
                        //deals.add(util.asyncPrepareDeal(items, fileName).get());
                        Deal deal = util.syncPrepareDeal(items, fileName);
                        if(currencyCodeDeals.containsKey(deal.getFromCurrencyISOcode().toUpperCase())){
                            currencyCodeDeals.get(deal.getFromCurrencyISOcode().toUpperCase()).add(deal);
                        }else{
                            List<Deal> currencyDeals = new ArrayList<>();
                            currencyDeals.add(deal);
                            currencyCodeDeals.put(deal.getFromCurrencyISOcode().toUpperCase(), currencyDeals);
                        }
                    } catch (Exception exception) {
                        LOG.error(exception);
                        corruptDeals.add(new CorruptDeal(row, fileName, exception.getMessage()));
                    }
                }else{
                    corruptDeals.add(new CorruptDeal(row, fileName, "One or more field missing"));
                }
            }
            fileLoaderDao.uploadDealData(currencyCodeDeals, corruptDeals, fileStatus);
            fileLoaderDao.uploadDealCounts(currencyCodeDeals, existingCurrencyDealCount.get());
        }
    }

    private FileStatus setInitialFileStatus(String fileName){
        FileStatus fileStatus = new FileStatus(fileName, "PROCESSING");
        fileLoaderDao.uploadFileStatus(fileStatus);
        return fileStatus;
    }
}
