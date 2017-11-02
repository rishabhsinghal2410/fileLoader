package com.fileloader.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
public class Deal implements Serializable{

    @Id
    private Integer id;
    private String fromCurrencyISOcode;
    private String toCurrencyISOcode;
    private Timestamp dealTime;
    private Double amount;
    private String fileName;

    public Deal() {
        this(null, null,null,null,null,null);
    }

    public Deal(Integer id, String fromCurrencyISOcode, String toCurrencyISOcode, Timestamp dealTime, Double amount, String fileName) {
        this.id = id;
        this.fromCurrencyISOcode = fromCurrencyISOcode;
        this.toCurrencyISOcode = toCurrencyISOcode;
        this.dealTime = dealTime;
        this.amount = amount;
        this.fileName = fileName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFromCurrencyISOcode() {
        return fromCurrencyISOcode;
    }

    public void setFromCurrencyISOcode(String fromCurrencyISOcode) {
        this.fromCurrencyISOcode = fromCurrencyISOcode;
    }

    public String getToCurrencyISOcode() {
        return toCurrencyISOcode;
    }

    public void setToCurrencyISOcode(String toCurrencyISOcode) {
        this.toCurrencyISOcode = toCurrencyISOcode;
    }

    public Timestamp getDealTime() {
        return dealTime;
    }

    public void setDealTime(Timestamp dealTime) {
        this.dealTime = dealTime;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "Deal{" +
                " id:" + id +
                ", fromCurrencyISOcode='" + fromCurrencyISOcode + '\'' +
                ", toCurrencyISOcode='" + toCurrencyISOcode + '\'' +
                ", dealTime=" + dealTime +
                ", amount=" + amount +
                ", fileName='" + fileName + '\'' +
                " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Deal deal = (Deal) o;

        if (id != null ? !id.equals(deal.id) : deal.id != null) return false;
        if (fromCurrencyISOcode != null ? !fromCurrencyISOcode.equals(deal.fromCurrencyISOcode) : deal.fromCurrencyISOcode != null)
            return false;
        if (toCurrencyISOcode != null ? !toCurrencyISOcode.equals(deal.toCurrencyISOcode) : deal.toCurrencyISOcode != null)
            return false;
        if (dealTime != null ? !dealTime.equals(deal.dealTime) : deal.dealTime != null) return false;
        if (amount != null ? !amount.equals(deal.amount) : deal.amount != null) return false;
        return fileName != null ? fileName.equals(deal.fileName) : deal.fileName == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (fromCurrencyISOcode != null ? fromCurrencyISOcode.hashCode() : 0);
        result = 31 * result + (toCurrencyISOcode != null ? toCurrencyISOcode.hashCode() : 0);
        result = 31 * result + (dealTime != null ? dealTime.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        return result;
    }
}
