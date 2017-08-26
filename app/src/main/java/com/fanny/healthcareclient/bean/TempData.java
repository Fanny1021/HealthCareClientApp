package com.fanny.healthcareclient.bean;

/**
 * Created by Fanny on 17/7/25.
 */

public class TempData {
    private float tempValue;
    private String time;
    private String UserId;
    private String UserName;

    public TempData(float tempValue, String time, String userId, String userName) {
        this.tempValue = tempValue;
        this.time = time;
        UserId = userId;
        UserName = userName;
    }

    public String getUserId() {
        return UserId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public float getTempValue() {
        return tempValue;
    }

    public String getTime() {
        return time;
    }

    public void setTempValue(float tempValue) {
        this.tempValue = tempValue;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public TempData() {
    }
}
