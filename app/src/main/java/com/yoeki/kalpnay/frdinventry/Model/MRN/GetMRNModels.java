package com.yoeki.kalpnay.frdinventry.Model.MRN;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetMRNModels {

@SerializedName("Status")
@Expose
private String status;
@SerializedName("Message")
@Expose
private String message;
@SerializedName("MRNList")
@Expose
private List<MRNList> mRNList = null;

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

public List<MRNList> getMRNList() {
return mRNList;
}

public void setMRNList(List<MRNList> mRNList) {
this.mRNList = mRNList;
}

}


