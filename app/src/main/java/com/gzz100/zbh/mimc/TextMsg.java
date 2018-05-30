package com.gzz100.zbh.mimc;

/**
 * Created by Lam on 2018/5/8.
 */

public class TextMsg extends BaseSync{
    private String content;

    public TextMsg(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
