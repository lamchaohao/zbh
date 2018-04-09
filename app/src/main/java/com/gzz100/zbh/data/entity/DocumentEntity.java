package com.gzz100.zbh.data.entity;

import java.util.List;

/**
 * Created by Lam on 2018/3/27.
 */

public class DocumentEntity {

    /**
     * readLimit : 1
     * documentType : pptx
     * downloadLimit : 1
     * documentId : 1336
     * documentName : 1117公司例会.pptx
     * documentSize : 566333
     * pictureList : ["http://192.168.1.168:8777/zbh-document/static/destFile/1/201803261742179691088467781/幻灯片1.JPG","http://192.168.1.168:8777/zbh-document/static/destFile/1/201803261742179691088467781/幻灯片2.JPG","http://192.168.1.168:8777/zbh-document/static/destFile/1/201803261742179691088467781/幻灯片3.JPG","http://192.168.1.168:8777/zbh-document/static/destFile/1/201803261742179691088467781/幻灯片4.JPG","http://192.168.1.168:8777/zbh-document/static/destFile/1/201803261742179691088467781/幻灯片5.JPG","http://192.168.1.168:8777/zbh-document/static/destFile/1/201803261742179691088467781/幻灯片6.JPG","http://192.168.1.168:8777/zbh-document/static/destFile/1/201803261742179691088467781/幻灯片7.JPG","http://192.168.1.168:8777/zbh-document/static/destFile/1/201803261742179691088467781/幻灯片8.JPG"]
     * documentPath : http://192.168.1.168:8777/zbh-document/static/destFile/1/201803261742179691088467781.pptx
     */

    private int readLimit;
    private String documentType;
    private int downloadLimit;
    private String documentId;
    private String documentName;
    private int documentSize;
    private String documentPath;
    private List<String> pictureList;

    public int getReadLimit() {
        return readLimit;
    }

    public void setReadLimit(int readLimit) {
        this.readLimit = readLimit;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public int getDownloadLimit() {
        return downloadLimit;
    }

    public void setDownloadLimit(int downloadLimit) {
        this.downloadLimit = downloadLimit;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public int getDocumentSize() {
        return documentSize;
    }

    public void setDocumentSize(int documentSize) {
        this.documentSize = documentSize;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public List<String> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<String> pictureList) {
        this.pictureList = pictureList;
    }
}
