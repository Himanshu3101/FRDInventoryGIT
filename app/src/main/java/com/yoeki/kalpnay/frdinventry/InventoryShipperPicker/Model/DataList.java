package com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataList {

@SerializedName("RequisitionNo")
@Expose
private String requisitionNo;
@SerializedName("Location")
@Expose
private String location;

public String getRequisitionNo() {
return requisitionNo;
}

public void setRequisitionNo(String requisitionNo) {
this.requisitionNo = requisitionNo;
}

public String getLocation() {
return location;
}

public void setLocation(String location) {
this.location = location;
}

}


