package com.yoeki.kalpnay.frdinventry.MRN.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MrnNumberDetailResponse {

@SerializedName("Status")
@Expose
private String status;
@SerializedName("Message")
@Expose
private String message;
@SerializedName("MRNDetailsList")
@Expose
private List<MRNDetailsList> mRNDetailsList = null;

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

public List<MRNDetailsList> getMRNDetailsList() {
return mRNDetailsList;
}

public void setMRNDetailsList(List<MRNDetailsList> mRNDetailsList) {
this.mRNDetailsList = mRNDetailsList;
}

}
