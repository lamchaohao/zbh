package com.gzz100.zbh.data.entity;

import java.util.List;

/**
 * Created by Lam on 2018/8/7.
 */

public class MessageWrapEntity {

    private List<MessageEntity> messageList;
    private int unReadCount;

    public List<MessageEntity> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageEntity> messageList) {
        this.messageList = messageList;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }
}
