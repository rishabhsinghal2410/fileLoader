package com.fileloader.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class FileStatus {

    @Id
    private String fileName;
    private String status;

    public FileStatus() {
        this(null, null);
    }

    public FileStatus(String fileName, String status) {
        this.fileName = fileName;
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileStatus that = (FileStatus) o;

        if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null) return false;
        return status != null ? status.equals(that.status) : that.status == null;
    }

    @Override
    public int hashCode() {
        int result = fileName != null ? fileName.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
