package com.yoeki.kalpnay.frdinventry.Dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfileResponse {

    @SerializedName("EmailId")
    @Expose
    private String emailId;
    @SerializedName("UserPin")
    @Expose
    private String userPin;
    @SerializedName("WareHouse")
    @Expose
    private String wareHouse;
    @SerializedName("UserName")
    @Expose
    private String userName;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUserPin() {
        return userPin;
    }

    public void setUserPin(String userPin) {
        this.userPin = userPin;
    }

    public String getWareHouse() {
        return wareHouse;
    }

    public void setWareHouse(String wareHouse) {
        this.wareHouse = wareHouse;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
