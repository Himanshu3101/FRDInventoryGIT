package com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BatchListReciver {
    @SerializedName("BatchNo")
    @Expose
    private String batchNo;
    @SerializedName("ReturnQty")
    @Expose
    private String returnQty;

    @SerializedName("Config")
    @Expose
    private String config;

    @SerializedName("StickerQty")
    @Expose
    private String stickerQty;

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(String returnQty) {
        this.returnQty = returnQty;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getStickerQty() {
        return stickerQty;
    }

    public void setStickerQty(String stickerQty) {
        this.stickerQty = stickerQty;
    }
}
