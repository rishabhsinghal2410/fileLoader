package com.fileloader.util;

import com.fileloader.models.Deal;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.concurrent.Future;

@Component
public class FileLoaderUtil {

    private static final HashSet<String> CURRENCY_CODE;

    static{
        CURRENCY_CODE = new HashSet<>();
        CURRENCY_CODE.add("USD");
        CURRENCY_CODE.add("EUR");
        CURRENCY_CODE.add("GBP");
        CURRENCY_CODE.add("JPY");
        CURRENCY_CODE.add("AUD");
        CURRENCY_CODE.add("CAD");
    }

    public static HashSet<String> getCurrencyCode(){
        return CURRENCY_CODE;
    }

    // sample pattern 2016-02-03 00:00:00.0
    private static final String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss.S";

    public Timestamp parseStringToDate(String date) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat(TIMESTAMP_PATTERN);
        try {
            java.util.Date parsed = format.parse(date);
            return new java.sql.Timestamp(parsed.getTime());
        }catch(Exception exception){
            throw new Exception("Error occurred while parsing timestamp");
        }
    }

    public String parseCurrencyCode(String currencyCode) throws Exception {
        if(CURRENCY_CODE.contains(currencyCode.toUpperCase())){
            return currencyCode.toUpperCase();
        }else{
            throw new Exception("Error occurred while parsing currency code");
        }
    }

    public Double parseAmount(String amount) throws Exception {
        try{
            return Double.parseDouble(amount);
        }catch(Exception exception){
            throw new Exception("Error occurred while parsing amount");
        }
    }

    public Integer parseId(String id) throws Exception {
        try{
            return Integer.parseInt(id);
        }catch(Exception exception){
            throw new Exception("Error occurred while parsing id");
        }
    }

    @Async
    public Future<Deal> asyncPrepareDeal(String[] items, String fileName) throws Exception {
        Integer id = this.parseId(items[CSVFileFormat.ID.getSequence()]);
        String fromCurrency = this.parseCurrencyCode(items[CSVFileFormat.FROM_CURRENCY.getSequence()]);
        String toCurrency = this.parseCurrencyCode(items[CSVFileFormat.TO_CURRENCY.getSequence()]);
        Timestamp timeStamp = this.parseStringToDate(items[CSVFileFormat.DEAL_TIMESTAMP.getSequence()]);
        Double amount = this.parseAmount(items[CSVFileFormat.DEAL_AMOUNT.getSequence()]);
        return new AsyncResult<Deal> (new Deal(id, fromCurrency, toCurrency, timeStamp, amount, fileName));
    }

    public Deal syncPrepareDeal(String[] items, String fileName) throws Exception {
        Integer id = this.parseId(items[CSVFileFormat.ID.getSequence()]);
        String fromCurrency = this.parseCurrencyCode(items[CSVFileFormat.FROM_CURRENCY.getSequence()]);
        String toCurrency = this.parseCurrencyCode(items[CSVFileFormat.TO_CURRENCY.getSequence()]);
        Timestamp timeStamp = this.parseStringToDate(items[CSVFileFormat.DEAL_TIMESTAMP.getSequence()]);
        Double amount = this.parseAmount(items[CSVFileFormat.DEAL_AMOUNT.getSequence()]);
        return new Deal(id, fromCurrency, toCurrency, timeStamp, amount, fileName);
    }
}
