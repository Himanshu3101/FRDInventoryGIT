package com.yoeki.kalpnay.frdinventry.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class changePswdResponse {
    @SerializedName("UserId")
    @Expose
    private Object userId;
    @SerializedName("UserPin")
    @Expose
    private Object userPin;
    @SerializedName("ResDescription")
    @Expose
    private String resDescription;
    @SerializedName("Status")
    @Expose
    private String status;

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public Object getUserPin() {
        return userPin;
    }

    public void setUserPin(Object userPin) {
        this.userPin = userPin;
    }

    public String getResDescription() {
        return resDescription;
    }

    public void setResDescription(String resDescription) {
        this.resDescription = resDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
