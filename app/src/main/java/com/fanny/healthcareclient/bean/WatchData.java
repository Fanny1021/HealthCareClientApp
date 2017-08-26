package com.fanny.healthcareclient.bean;

/**
 * Created by Fanny on 17/7/25.
 */

public class WatchData {
    private int spo2;
    private int pr;
    private float pi;
    private String time;

    public WatchData(int spo2, int pr, float pi, String time) {
        this.spo2 = spo2;
        this.pr = pr;
        this.pi = pi;
        this.time = time;
    }

    public WatchData() {
    }

    public int getSpo2() {
        return spo2;
    }

    public int getPr() {
        return pr;
    }

    public float getPi() {
        return pi;
    }

    public String getTime() {
        return time;
    }

    public void setSpo2(int spo2) {
        this.spo2 = spo2;
    }

    public void setPr(int pr) {
        this.pr = pr;
    }

    public void setPi(float pi) {
        this.pi = pi;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
