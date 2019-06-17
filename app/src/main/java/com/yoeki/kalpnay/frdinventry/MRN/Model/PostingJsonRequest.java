package com.yoeki.kalpnay.frdinventry.MRN.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostingJsonRequest {

    @SerializedName("MRNNumber")
    @Expose
    private String mRNNumber;
    @SerializedName("ActivityNo")
    @Expose
    private String activityNo;

    public PostingJsonRequest(String mRNNumber, String activityNo) {
        this.mRNNumber = mRNNumber;
        this.activityNo = activityNo;
    }

    public String getMRNNumber() {
        return mRNNumber;
    }

    public void setMRNNumber(String mRNNumber) {
        this.mRNNumber = mRNNumber;
    }

    public String getActivityNo() {
        return activityNo;
    }

    public void setActivityNo(String activityNo) {
        this.activityNo = activityNo;
    }

}