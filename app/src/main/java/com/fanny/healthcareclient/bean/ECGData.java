package com.fanny.healthcareclient.bean;

/**
 * Created by Fanny on 17/7/25.
 */

public class ECGData {
    private int ecg;
    private String time;

    public ECGData() {
    }

    public ECGData(int ecg, String time) {
        this.ecg = ecg;
        this.time = time;
    }

    public void setEcg(int ecg) {
        this.ecg = ecg;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getEcg() {
        return ecg;
    }

    public String getTime() {
        return time;
    }
}
