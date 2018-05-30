package com.gzz100.zbh.data.entity;

/**
 * Created by Lam on 2018/5/18.
 */

public class DownloadFileEntity {

    /**
     * documentType : docx
     * documentDownloadPath : http://192.168.1.119:8080/zbh-document/static/srcFile/1/1/1524105495925.docx
     * documentId : 1501
     * documentName : word2008.docx
     * documentPath : http://192.168.1.119:8080/zbh-document/static/destFile/1/1/1524105495925.html
     */

    private String documentType;
    private String documentDownloadPath;
    private String documentId;
    private String documentName;
    private String documentPath;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentDownloadPath() {
        return documentDownloadPath;
    }

    public void setDocumentDownloadPath(String documentDownloadPath) {
        this.documentDownloadPath = documentDownloadPath;
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
}
