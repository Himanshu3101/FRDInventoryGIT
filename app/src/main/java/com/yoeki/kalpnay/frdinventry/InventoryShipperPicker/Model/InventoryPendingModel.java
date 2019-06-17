package com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InventoryPendingModel {

@SerializedName("Status")
@Expose
private String status;
@SerializedName("Message")
@Expose
private String message;
@SerializedName("dataList")
@Expose
private ArrayList<detailDataList> dataList = null;

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

public ArrayList<detailDataList> getDataList() {
return dataList;
}

public void setDataList(ArrayList<detailDataList> dataList) {
this.dataList = dataList;
}

}
