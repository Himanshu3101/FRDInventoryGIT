package com.yoeki.kalpnay.frdinventry.Dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReasonModule {
    @SerializedName("Reason")
    @Expose
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }}
