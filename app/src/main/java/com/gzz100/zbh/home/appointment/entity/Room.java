package com.gzz100.zbh.home.appointment.entity;

import java.util.List;

/**
 * Created by Lam on 2018/1/30.
 */

public class Room {
    private boolean enable;//是否可预约

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    private String roomName;
    private String imageUrl;
    private List<String> tagList;
    private int capcity;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public int getCapcity() {
        return capcity;
    }

    public void setCapcity(int capcity) {
        this.capcity = capcity;
    }
}
