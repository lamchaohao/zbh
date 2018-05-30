package com.gzz100.zbh.mimc;

/**
 * Created by Lam on 2018/5/7.
 */

public class SyncCanvasBean extends BaseSync{

    private int e;
    private float x;
    private float y;


    public SyncCanvasBean(int MOTION_EVENT_ACTION, float xPiot, float yPiot) {
        this.e = MOTION_EVENT_ACTION;
        this.x = xPiot;
        this.y = yPiot;
    }

    public int getAction() {
        return e;
    }

    public void setAction(int action) {
        this.e = action;
    }

    public float getxPiot() {
        return x;
    }

    public void setxPiot(float xPiot) {
        this.x = xPiot;
    }

    public float getyPiot() {
        return y;
    }

    public void setyPiot(float yPiot) {
        this.y = yPiot;
    }

}
