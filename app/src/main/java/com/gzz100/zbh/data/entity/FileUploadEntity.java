package com.gzz100.zbh.data.entity;

/**
 * Created by Lam on 2018/3/22.
 */

public class FileUploadEntity {

    public FileUploadEntity(int readLimit, int downloadLimit, String fileMd5) {
        this.readLimit = readLimit;
        this.downloadLimit = downloadLimit;
        this.fileMd5 = fileMd5;
    }

    /**
     * readLimit : 1
     * downloadLimit : 1
     * fileMd5 : 445563321545
     */

    private int readLimit;
    private int downloadLimit;
    private String fileMd5;

    public int getReadLimit() {
        return readLimit;
    }

    public void setReadLimit(int readLimit) {
        this.readLimit = readLimit;
    }

    public int getDownloadLimit() {
        return downloadLimit;
    }

    public void setDownloadLimit(int downloadLimit) {
        this.downloadLimit = downloadLimit;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }
}
