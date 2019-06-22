package com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseShippingDetails {

@SerializedName("Status")
@Expose
private String status;
@SerializedName("Message")
@Expose
private String message;
@SerializedName("TONumber")
@Expose
private String tONumber;

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

public String getTONumber() {
return tONumber;
}

public void setTONumber(String tONumber) {
this.tONumber = tONumber;
}

}