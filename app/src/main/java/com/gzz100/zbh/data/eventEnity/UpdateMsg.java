package com.gzz100.zbh.data.eventEnity;

/**
 * Created by Lam on 2018/4/27.
 */

public class UpdateMsg {

    private Action action;

    public enum Action{
        updateMeeting,
        updatePhoneNum,
        updateCache,
        updatePsw
    }

    public UpdateMsg(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }
}
