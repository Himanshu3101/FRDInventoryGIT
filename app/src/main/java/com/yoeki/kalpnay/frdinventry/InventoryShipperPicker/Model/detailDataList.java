package com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class detailDataList {

@SerializedName("RequisitionNo")
@Expose
private String requisitionNo;
@SerializedName("ReqestDate")
@Expose
private String reqestDate;
@SerializedName("CreatedBy")
@Expose
private String createdBy;
@SerializedName("Branch")
@Expose
private String branch;
@SerializedName("ItemId")
@Expose
private String itemId;
@SerializedName("ItemNameArabic")
@Expose
private String itemNameArabic;
@SerializedName("ItemName")
@Expose
private String itemName;
@SerializedName("ApprovedQty")
@Expose
private String approvedQty;

public String getRequisitionNo() {
return requisitionNo;
}

public void setRequisitionNo(String requisitionNo) {
this.requisitionNo = requisitionNo;
}

public String getReqestDate() {
return reqestDate;
}

public void setReqestDate(String reqestDate) {
this.reqestDate = reqestDate;
}

public String getCreatedBy() {
return createdBy;
}

public void setCreatedBy(String createdBy) {
this.createdBy = createdBy;
}

public String getBranch() {
return branch;
}

public void setBranch(String branch) {
this.branch = branch;
}

public String getItemId() {
return itemId;
}

public void setItemId(String itemId) {
this.itemId = itemId;
}

public String getItemNameArabic() {
return itemNameArabic;
}

public void setItemNameArabic(String itemNameArabic) {
this.itemNameArabic = itemNameArabic;
}

public String getItemName() {
return itemName;
}

public void setItemName(String itemName) {
this.itemName = itemName;
}

public String getApprovedQty() {
return approvedQty;
}

public void setApprovedQty(String approvedQty) {
this.approvedQty = approvedQty;
}

}

