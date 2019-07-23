package com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class commonReceivingShippingDetailDataList {

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
    @SerializedName("PickedQty")
    @Expose
    private String pickededQty;
    @SerializedName("RemainingQty")
    @Expose
    private String remainingQty;
    @SerializedName("Config")
    @Expose
    private String config;
    @SerializedName("BatchId")
    @Expose
    private String batchId;
    @SerializedName("Expdate")
    @Expose
    private String expdate;
    @SerializedName("Reason")
    @Expose
    private String reason;
    @SerializedName("TONum")
    @Expose
    private String tONum;
    @SerializedName("Location")
    @Expose
    private String locationId;
    @SerializedName("ShippedQty")
    @Expose
    private String ShippedQty;

    @SerializedName("ReturnQty")
    @Expose
    private String ReturnQty;
    @SerializedName("RequisitionDetailId")
    @Expose
    private String requisitionDetailId;
    @SerializedName("UnitId")
    @Expose
    private String unitId;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }
















    @SerializedName("BatchNoList")
    @Expose
    private List<BatchNoList> batchNoList = null;
    @SerializedName("StickerList")
    @Expose
    private List<StickerList> stickerList = null;


    public List<BatchNoList> getBatchNoList() {
        return batchNoList;
    }

    public void setBatchNoList(List<BatchNoList> batchNoList) {
        this.batchNoList = batchNoList;
    }

    public List<StickerList> getStickerList() {
        return stickerList;
    }

    public void setStickerList(List<StickerList> stickerList) {
        this.stickerList = stickerList;
    }


    @SerializedName("BatchList")
    @Expose
    private List<BatchListReciver> BatchListReceiver = null;

    public List<BatchListReciver> getBatchListReceiver() {
        return BatchListReceiver;
    }

    public void setBatchListReceiver(List<BatchListReciver> BatchListReceiver) {
        this.BatchListReceiver = BatchListReceiver;
    }
















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

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getExpdate() {
        return expdate;
    }

    public void setExpdate(String expdate) {
        this.expdate = expdate;
    }

    public String getpickededQty() {
        return pickededQty;
    }

    public void setpickededQty(String pickededQty) {
        this.pickededQty = pickededQty;
    }

    public String getremainingQty() {
        return remainingQty;
    }

    public void setremainingQty(String remainingQty) {
        this.remainingQty = remainingQty;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTONum() {
        return tONum;
    }

    public void setTONum(String tONum) {
        this.tONum = tONum;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getShippedQty() {
        return ShippedQty;
    }

    public void setShippedQty(String ShippedQty) {
        this.ShippedQty = ShippedQty;
    }

    public String getReturnQty() {
        return ReturnQty;
    }

    public void setReturnQty(String returnQty) {
        ReturnQty = returnQty;
    }

    public String getRequisitionDetailId() {
        return requisitionDetailId;
    }

    public void setRequisitionDetailId(String requisitionDetailId) {
        this.requisitionDetailId = requisitionDetailId;
    }

}

