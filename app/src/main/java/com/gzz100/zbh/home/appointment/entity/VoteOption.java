package com.gzz100.zbh.home.appointment.entity;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Lam on 2018/2/9.
 * 在VoteWrap类中，VoteWrap类实现了Parcelable接口, VoteWrap类需要读或者写VoteOption类数据，
 * VoteOption类需要实现Serializable序列化接口。
 * 不然会出现java.lang.RuntimeException: Parcel: unable to marshal value错误！
 */

public class VoteOption implements Serializable{

    private String voteOptionName;
    private File picFile;
    private String fileMd5;
    public String getOptionName() {
        return voteOptionName;
    }

    public void setOptionName(String optionName) {
        this.voteOptionName = optionName;
    }

    public File getPicFile() {
        return picFile;
    }

    public void setPicFile(File picFile) {
        this.picFile = picFile;
    }

    public String getFileMD5Value() {
        return fileMd5;
    }

    public void setFileMD5Value(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    @Override
    public String toString() {
        return "VoteOption{" +
                "voteOptionName='" + voteOptionName + '\'' +
                ", picFile=" + (picFile==null?"null":picFile.getAbsolutePath()) +
                '}';
    }
}
