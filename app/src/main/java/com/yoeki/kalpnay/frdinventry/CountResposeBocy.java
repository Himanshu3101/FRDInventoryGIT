package com.yoeki.kalpnay.frdinventry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountResposeBocy {

@SerializedName("Status")
@Expose
private String status;
@SerializedName("Message")
@Expose
private String message;
@SerializedName("TotalMrn")
@Expose
private String totalMrn;
@SerializedName("TotalPending")
@Expose
private String totalPending;
@SerializedName("Totalcomplete")
@Expose
private String totalcomplete;

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

public String getTotalMrn() {
return totalMrn;
}

public void setTotalMrn(String totalMrn) {
this.totalMrn = totalMrn;
}

public String getTotalPending() {
return totalPending;
}

public void setTotalPending(String totalPending) {
this.totalPending = totalPending;
}

public String getTotalcomplete() {
return totalcomplete;
}

public void setTotalcomplete(String totalcomplete) {
this.totalcomplete = totalcomplete;
}

}