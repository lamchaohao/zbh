package com.gzz100.zbh.data.entity;

/**
 * Created by Lam on 2018/5/16.
 */

public class StarFileEntity {


    /**
     * documentType : docx
     * documentId : 3563
     * documentName : 大中型吹瓶机压缩机的使用规范-吹瓶机压缩机的日常保养.docx
     * documentPath : http://192.168.1.168:8777/zbh-document/static/destFile/0152479471393/2111/1525766814361/1525766814361.html
     */

    private String documentType;
    private String documentId;
    private String documentName;
    private String documentPath;
    private String documentDownloadPath;

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
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

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public String getDocumentDownloadPath() {
        return documentDownloadPath;
    }

    public void setDocumentDownloadPath(String documentDownloadPath) {
        this.documentDownloadPath = documentDownloadPath;
    }
}
