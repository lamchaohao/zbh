package com.gzz100.zbh.account.eventEntity;

/**
 * Created by Lam on 2018/8/4.
 */

public class RegStepEvent {

    private int pageClick;
    private String message;


    public RegStepEvent(int pageClick, String message) {
        this.pageClick = pageClick;
        this.message = message;
    }

    public int getPageClick() {
        return pageClick;
    }

    public String getMessage() {
        return message;
    }
}
