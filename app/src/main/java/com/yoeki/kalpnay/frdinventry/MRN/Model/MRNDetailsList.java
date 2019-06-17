package com.yoeki.kalpnay.frdinventry.MRN.Model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MRNDetailsList {

    @SerializedName("ItemId")
    @Expose
    private String itemId;
    @SerializedName("BatchNo")
    @Expose
    private String batchNo;
    @SerializedName("Config")
    @Expose
    private String config;
    @SerializedName("ExpiryDate")
    @Expose
    private String expiryDate;
    @SerializedName("ReceivedQuantity")
    @Expose
    private String receivedQuantity;
    @SerializedName("itemArabicName")
    @Expose
    private String itemArabicName;
    @SerializedName("itemname")
    @Expose
    private String itemname;
    @SerializedName("LineNo")
    @Expose
    private String lineNo;
    @SerializedName("ScanQty")
    @Expose
    private String scanQty;
    @SerializedName("StickerSeq")
    @Expose
    private List<StickerSeq> stickerSeq = null;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getReceivedQuantity() {
        return receivedQuantity;
    }

    public void setReceivedQuantity(String receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }

    public String getItemArabicName() {
        return itemArabicName;
    }

    public void setItemArabicName(String itemArabicName) {
        this.itemArabicName = itemArabicName;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getScanQty() {
        return scanQty;
    }

    public void setScanQty(String scanQty) {
        this.scanQty = scanQty;
    }

    public List<StickerSeq> getStickerSeq() {
        return stickerSeq;
    }

    public void setStickerSeq(List<StickerSeq> stickerSeq) {
        this.stickerSeq = stickerSeq;
    }

}