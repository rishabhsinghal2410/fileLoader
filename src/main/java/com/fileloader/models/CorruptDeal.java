package com.fileloader.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class CorruptDeal implements Serializable{

    @Id
    private String dealData;
    private String errorMessage;
    private String fileName;

    public CorruptDeal() {
        this(null, null, null);
    }

    public CorruptDeal(String dealData, String fileName, String errorMessage) {
        this.dealData = dealData;
        this.fileName = fileName;
        this.errorMessage = errorMessage;
    }

    public String getDealData() {
        return dealData;
    }

    public void setDealData(String dealData) {
        this.dealData = dealData;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "CorruptDeal{" +
                " dealData='" + dealData + '\'' +
                ", fileName='" + fileName + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CorruptDeal that = (CorruptDeal) o;

        if (dealData != null ? !dealData.equals(that.dealData) : that.dealData != null) return false;
        if (errorMessage != null ? !errorMessage.equals(that.errorMessage) : that.errorMessage != null) return false;
        return fileName != null ? fileName.equals(that.fileName) : that.fileName == null;
    }

    @Override
    public int hashCode() {
        int result = dealData != null ? dealData.hashCode() : 0;
        result = 31 * result + (errorMessage != null ? errorMessage.hashCode() : 0);
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        return result;
    }
}
