package com.gzz100.zbh.mimc;

/**
 * Created by Lam on 2018/5/8.
 */

public class BaseSync {
    protected long groupId;
    protected String fromAccount;

    public long getGroupId() {
        return groupId;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }
}
