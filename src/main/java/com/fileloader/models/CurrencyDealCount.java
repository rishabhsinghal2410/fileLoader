package com.fileloader.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CurrencyDealCount {

    @Id
    private String orderingCurrency;
    private Integer count;

    public CurrencyDealCount() {
        this(null, null);
    }

    public CurrencyDealCount(String orderingCurrency, Integer count) {
        this.orderingCurrency = orderingCurrency;
        this.count = count;
    }

    public String getOrderingCurrency() {
        return orderingCurrency;
    }

    public void setOrderingCurrency(String orderingCurrency) {
        this.orderingCurrency = orderingCurrency;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "CurrencyDealCount{ " +
                "orderingCurrency='" + orderingCurrency + '\'' +
                ", count=" + count +
                " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrencyDealCount dealCount = (CurrencyDealCount) o;

        if (orderingCurrency != null ? !orderingCurrency.equals(dealCount.orderingCurrency) : dealCount.orderingCurrency != null)
            return false;
        return count != null ? count.equals(dealCount.count) : dealCount.count == null;
    }

    @Override
    public int hashCode() {
        int result = orderingCurrency != null ? orderingCurrency.hashCode() : 0;
        result = 31 * result + (count != null ? count.hashCode() : 0);
        return result;
    }
}
