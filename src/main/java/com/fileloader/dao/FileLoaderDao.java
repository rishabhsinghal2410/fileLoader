package com.fileloader.dao;

import com.fileloader.models.CorruptDeal;
import com.fileloader.models.CurrencyDealCount;
import com.fileloader.models.Deal;
import com.fileloader.models.FileStatus;
import com.fileloader.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class FileLoaderDao {

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private CorruptDealRepository corruptDealRepository;

    @Autowired
    private BulkUpdateRepository bulkUpdateRepository;

    @Autowired
    private CurrencyDealCountRepository currencyDealCountRepository;

    @Autowired
    private FileStatusRepository fileStatusRepository;

    public void uploadDealData(HashMap<String, List<Deal>> currencyDeals, List<CorruptDeal> corruptDeals, FileStatus fileStatus) throws ExecutionException, InterruptedException {
        fileStatus.setStatus("DONE");
        Future<String> result = null;
        for(List<Deal> deals : currencyDeals.values()){
            result = bulkUpdateRepository.bulkSave(deals, fileStatus);
        }
        bulkUpdateRepository.bulkSave(corruptDeals, null);
        //fileStatus.setStatus(result == null ? "DONE" : result.get());
        //uploadFileStatus(fileStatus);
    }

    public Iterable<Deal> getAllDeals(){
        return dealRepository.findAll();
    }

    public List<CorruptDeal> getAllCorruptDeals(String fileName){
        return corruptDealRepository.dealsByFileName(fileName);
    }

    public List<Deal> getAllDeals(String fileName){
        return dealRepository.dealsByFileName(fileName);
    }

    public List<FileStatus> getAllFiles(){
        return (List<FileStatus>) fileStatusRepository.findAll();
    }

    @Async
    public Future<List<CurrencyDealCount>> getCurrencyDealsCount(){
        return new AsyncResult<List<CurrencyDealCount>>((List<CurrencyDealCount>) currencyDealCountRepository.findAll());
    }

    @Async
    public void uploadDealCounts(HashMap<String, List<Deal>> currencyDeals, List<CurrencyDealCount> existingCurrencyDealCounts){
        for (Map.Entry<String, List<Deal>> entry : currencyDeals.entrySet()) {
            String currency = entry.getKey();
            Integer count = entry.getValue().size();
            if(existingCurrencyDealCounts == null || existingCurrencyDealCounts.isEmpty()){
                currencyDealCountRepository.save(new CurrencyDealCount(currency.toUpperCase(), count));
            }else {
                boolean foundEntry = false;
                for (CurrencyDealCount currencyDealCount : existingCurrencyDealCounts) {
                    if (currencyDealCount.getOrderingCurrency().toUpperCase().equalsIgnoreCase(currency.toUpperCase())) {
                        currencyDealCount.setCount(currencyDealCount.getCount() + count);
                        currencyDealCountRepository.save(currencyDealCount);
                        foundEntry = true;
                        break;
                    }
                }
                if (!foundEntry){
                    currencyDealCountRepository.save(new CurrencyDealCount(currency.toUpperCase(), count));
                }
            }
        }
    }

    //@Async
    public void uploadFileStatus(FileStatus fileStatus){
        fileStatusRepository.save(fileStatus);
    }
}
