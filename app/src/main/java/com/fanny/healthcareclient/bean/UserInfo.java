package com.fanny.healthcareclient.bean;

import java.util.Date;

/**
 * Created by Fanny on 17/7/28.
 */

public class UserInfo {
    public String UserName;
    private String UserID;
    private Boolean Sex;
    private String UserID_Card;
    private int Age;

    public UserInfo(String userName, Boolean sex, int age, String userID_Card, String userID) {
        UserName = userName;
        UserID = userID;
        Sex = sex;
        UserID_Card = userID_Card;
        Age = age;
    }

    public String getUserName() {
        return UserName;
    }

    public String getUserID() {
        return UserID;
    }

    public Boolean getSex() {
        return Sex;
    }

    public String getUserID_Card() {
        return UserID_Card;
    }

    public int getAge() {
        return Age;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public void setSex(Boolean sex) {
        Sex = sex;
    }

    public void setUserID_Card(String userID_Card) {
        UserID_Card = userID_Card;
    }

    public void setAge(int age) {
        Age = age;
    }
}
