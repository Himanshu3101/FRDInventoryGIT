package com.yoeki.kalpnay.frdinventry.Model.MRN;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MRNList {

@SerializedName("MRNNumber")
@Expose
private String mRNNumber;
@SerializedName("MRNDate")
@Expose
private String mRNDate;
@SerializedName("ActivityNumber")
@Expose
private String activityNumber;

public String getMRNNumber() {
return mRNNumber;
}

public void setMRNNumber(String mRNNumber) {
this.mRNNumber = mRNNumber;
}

public String getMRNDate() {
return mRNDate;
}

public void setMRNDate(String mRNDate) {
this.mRNDate = mRNDate;
}

public String getActivityNumber() {
return activityNumber;
}

public void setActivityNumber(String activityNumber) {
this.activityNumber = activityNumber;
}

}
