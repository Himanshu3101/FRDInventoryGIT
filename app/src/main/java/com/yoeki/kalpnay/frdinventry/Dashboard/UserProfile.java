package com.yoeki.kalpnay.frdinventry.Dashboard;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfile {

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("UserProfileResponse")
    @Expose
    private List<UserProfileResponse> userProfileResponse = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UserProfileResponse> getUserProfileResponse() {
        return userProfileResponse;
    }

    public void setUserProfileResponse(List<UserProfileResponse> userProfileResponse) {
        this.userProfileResponse = userProfileResponse;
    }

}