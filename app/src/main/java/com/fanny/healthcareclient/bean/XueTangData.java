package com.fanny.healthcareclient.bean;

/**
 * Created by Fanny on 17/7/25.
 */

public class XueTangData {
    private float glu;
    private float ua;
    private float chol;
    private String time;

    public XueTangData() {
    }

    public XueTangData(float glu, float ua, float chol,String time) {
        this.glu = glu;
        this.ua = ua;
        this.chol = chol;
        this.time=time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getGlu() {
        return glu;
    }

    public float getUa() {
        return ua;
    }

    public void setGlu(float glu) {
        this.glu = glu;
    }

    public void setUa(float ua) {
        this.ua = ua;
    }

    public void setChol(float chol) {
        this.chol = chol;
    }

    public float getChol() {
        return chol;

    }
}
