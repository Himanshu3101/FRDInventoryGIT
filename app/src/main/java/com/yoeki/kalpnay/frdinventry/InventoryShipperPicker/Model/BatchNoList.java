package com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BatchNoList {
    @SerializedName("BatchNo")
    @Expose
    private String batchNo;
    @SerializedName("BatchQty")
    @Expose
    private String batchQty;
    @SerializedName("Config")
    @Expose
    private String config;
    @SerializedName("BatchAutoIncreeId")
    @Expose
    private String batchAutoIncreeId;

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getBatchQty() {
        return batchQty;
    }

    public void setBatchQty(String batchQty) {
        this.batchQty = batchQty;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getBatchAutoIncreeId() {
        return batchAutoIncreeId;
    }

    public void setBatchAutoIncreeId(String batchAutoIncreeId) {
        this.batchAutoIncreeId = batchAutoIncreeId;
    }
}
