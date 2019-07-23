package com.yoeki.kalpnay.frdinventry.InventoryCounting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JiournalResponse {

@SerializedName("Status")
@Expose
private String status;
@SerializedName("Message")
@Expose
private String message;
@SerializedName("InventJounralId")
@Expose
private String inventJounralId;

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

public String getInventJounralId() {
return inventJounralId;
}

public void setInventJounralId(String inventJounralId) {
this.inventJounralId = inventJounralId;
}

}