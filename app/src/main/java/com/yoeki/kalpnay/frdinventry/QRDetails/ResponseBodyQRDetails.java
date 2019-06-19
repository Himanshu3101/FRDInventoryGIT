package com.yoeki.kalpnay.frdinventry.QRDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseBodyQRDetails {

@SerializedName("Status")
@Expose
private String status;
@SerializedName("Message")
@Expose
private String message;
@SerializedName("VendorId")
@Expose
private String vendorId;
@SerializedName("PONumber")
@Expose
private String pONumber;
@SerializedName("ItemId")
@Expose
private String itemId;
@SerializedName("ItemName")
@Expose
private String itemName;
@SerializedName("ItemnameArabic")
@Expose
private String itemnameArabic;
@SerializedName("Expdate")
@Expose
private String expdate;
@SerializedName("BatchId")
@Expose
private String batchId;
@SerializedName("Config")
@Expose
private String config;
@SerializedName("VendorName")
@Expose
private String vendorName;
@SerializedName("StickerQty")
@Expose
private String stickerQty;

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

public String getVendorId() {
return vendorId;
}

public void setVendorId(String vendorId) {
this.vendorId = vendorId;
}

public String getPONumber() {
return pONumber;
}

public void setPONumber(String pONumber) {
this.pONumber = pONumber;
}

public String getItemId() {
return itemId;
}

public void setItemId(String itemId) {
this.itemId = itemId;
}

public String getItemName() {
return itemName;
}

public void setItemName(String itemName) {
this.itemName = itemName;
}

public String getItemnameArabic() {
return itemnameArabic;
}

public void setItemnameArabic(String itemnameArabic) {
this.itemnameArabic = itemnameArabic;
}

public String getExpdate() {
return expdate;
}

public void setExpdate(String expdate) {
this.expdate = expdate;
}

public String getBatchId() {
return batchId;
}

public void setBatchId(String batchId) {
this.batchId = batchId;
}

public String getConfig() {
return config;
}

public void setConfig(String config) {
this.config = config;
}

public String getVendorName() {
return vendorName;
}

public void setVendorName(String vendorName) {
this.vendorName = vendorName;
}

public String getStickerQty() {
return stickerQty;
}

public void setStickerQty(String stickerQty) {
this.stickerQty = stickerQty;
}

}