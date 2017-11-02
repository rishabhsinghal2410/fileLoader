package com.fileloader.controller;

import com.fileloader.models.CorruptDeal;
import com.fileloader.models.CurrencyDealCount;
import com.fileloader.models.Deal;
import com.fileloader.models.FileStatus;
import com.fileloader.service.FileLoaderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Matchers.anyObject;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class FileLoaderControllerTest {

    @Mock
    private FileLoaderService service;

    private MockMvc mvc;

    @InjectMocks
    private FileLoaderController controller;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void shouldReturnViewWithoutFilesWhenNoFilesHaveBeenAdded() throws Exception{
        List<FileStatus> emptyList = new ArrayList<>();
        Mockito.when(service.getAllFiles()).thenReturn(emptyList);
        this.mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("files", emptyList))
                .andExpect(view().name("landingPage"));
    }

    @Test
    public void shouldReturnViewWithFilesWhenFilesHaveBeenAdded() throws Exception{
        FileStatus fileStatus = new FileStatus("deals.csv", "Done");
        List<FileStatus> fileStatusList = new ArrayList<>();
        Mockito.when(service.getAllFiles()).thenReturn(fileStatusList);
        this.mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("files"))
                .andExpect(model().attribute("files", fileStatusList))
                .andExpect(view().name("landingPage"));
    }

    @Test
    public void shouldReturnViewWithCurrencyDealCount() throws Exception {
        CurrencyDealCount dealCount = new CurrencyDealCount("USD", 100);
        List<CurrencyDealCount> dealCountList = new ArrayList<>();
        dealCountList.add(dealCount);
        List<String> dealCountString = dealCountList.stream().map(deal -> deal.toString()).collect(Collectors.toList());
        Mockito.when(service.getAllCurrencyDealCount()).thenReturn(dealCountList);
        this.mvc.perform(get("/currencyDealCount"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("currencyCounts"))
                .andExpect(model().attribute("currencyCounts", dealCountString))
                .andExpect(view().name("currencyCount"));
    }

    @Test
    public void shouldReturnViewWithFileDetails() throws Exception {
        Deal deal = new Deal(1,"USD","GBP",new Timestamp(new Date().getTime()), 100d, "deals.csv");
        List<Deal> dealList = new ArrayList<>();
        dealList.add(deal);
        List<String> dealString = dealList.stream().map(deal1 -> deal1.toString()).collect(Collectors.toList());
        CorruptDeal corruptDeal = new CorruptDeal("1,USD,GBP,,100d", "deals.csv", "missing field");
        List<CorruptDeal> corruptDeals = new ArrayList<>();
        corruptDeals.add(corruptDeal);
        List<String> corruptDealString = corruptDeals.stream().map(deal1 -> deal1.toString()).collect(Collectors.toList());
        Mockito.when(service.getAllDeals("deals.csv")).thenReturn(dealList);
        Mockito.when(service.getAllCorruptDeals("deals.csv")).thenReturn(corruptDeals);
        this.mvc.perform(get("/files/deals.csv"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("deals"))
                .andExpect(model().attributeExists("corruptDeals"))
                .andExpect(model().attribute("deals", dealString))
                .andExpect(model().attribute("corruptDeals", corruptDealString))
                .andExpect(view().name("fileDetails"));
    }

    @Test
    public void shouldRedirectToLandingPageAfterFileLoadPostRequest() throws Exception {
        Mockito.when(service.getAllFiles()).thenReturn(new ArrayList<FileStatus>());
        Mockito.doNothing().when(service).load((MultipartFile) anyObject());
        MockMultipartFile file = new MockMultipartFile("file", "filename.csv", "text/plain", "some csv".getBytes());
        this.mvc.perform(fileUpload("/")
                .file(file))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attributeExists("files"));
    }
}