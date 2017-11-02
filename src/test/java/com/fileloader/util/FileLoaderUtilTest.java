package com.fileloader.util;

import com.fileloader.models.Deal;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class FileLoaderUtilTest {

    FileLoaderUtil util;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setup(){
        util = new FileLoaderUtil();
    }

    @Test
    public void testParseStringToDate() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        Date parsed = format.parse("2016-02-03 00:00:00.0");
        Timestamp timestamp = new Timestamp(parsed.getTime());
        assertEquals(timestamp,util.parseStringToDate("2016-02-03 00:00:00.0"));
    }

    @Test()
    public void testParseStringToDateForException() throws Exception {
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("Error occurred while parsing timestamp");
        util.parseStringToDate("2016-02-03 00:00:");
    }

    @Test
    public void testCurrencyCodeParsing() throws Exception {
        assertEquals("USD", util.parseCurrencyCode("USD"));
    }

    @Test
    public void testCurrencyCodeParsingException() throws Exception {
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("Error occurred while parsing currency code");
        util.parseCurrencyCode("ABC");
    }

    @Test
    public void testAmountParsing() throws Exception {
        assertEquals(new Double(100), util.parseAmount("100"));
    }

    @Test
    public void testAmountParsingException() throws Exception {
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("Error occurred while parsing amount");
        util.parseAmount("12q");
    }

    @Test
    public void testIdParsing() throws Exception {
        assertEquals(new Integer(100), util.parseId("100"));
    }

    @Test
    public void testIdParsingException() throws Exception {
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("Error occurred while parsing id");
        util.parseId("12q");
    }

    @Test
    public void testPrepareDeal() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        Date parsed = format.parse("2016-02-03 00:00:00.0");
        Timestamp timestamp = new Timestamp(parsed.getTime());
        Deal deal = new Deal(1,"USD","GBP",timestamp,100d,"deals.csv");
        String[] items = new String[]{"1","USD","GBP","2016-02-03 00:00:00.0", "100"};
        util.syncPrepareDeal(items, "deals.csv");
    }
}