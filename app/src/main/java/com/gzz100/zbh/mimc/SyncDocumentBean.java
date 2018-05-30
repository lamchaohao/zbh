package com.gzz100.zbh.mimc;

/**
 * Created by Lam on 2018/5/8.
 */

public class SyncDocumentBean extends BaseSync{

    public static final int ACTION_DOC = 104;
    public static final int ACTION_PIC = 105;

    public SyncDocumentBean(int actionType) {
        this.actionType = actionType;
    }

    private String picUrl;
    private String docUrl;
    private String docId;
    private String docType;
    private int actionType;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
}
