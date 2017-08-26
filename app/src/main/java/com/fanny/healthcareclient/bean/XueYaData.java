package com.fanny.healthcareclient.bean;

/**
 * Created by Fanny on 17/7/24.
 */

public class XueYaData {
    private int sys;
    private int dia;
    private String time;
    private int plus;
    private int map;

    public XueYaData(int sys, int dia, String time, int plus, int map) {
        this.sys = sys;
        this.dia = dia;
        this.time = time;
        this.plus = plus;
        this.map = map;
    }
    public XueYaData(){}

    public int getSys() {
        return sys;
    }

    public int getDia() {
        return dia;
    }

    public String getTime() {
        return time;
    }

    public int getPlus() {
        return plus;
    }

    public int getMap() {
        return map;
    }

    public void setSys(int sys) {
        this.sys = sys;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPlus(int plus) {
        this.plus = plus;
    }

    public void setMap(int map) {
        this.map = map;
    }
}
