package com.yoeki.kalpnay.frdinventry.InventoryCountingNew.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataList {
    @SerializedName("ItemId")
    @Expose
    private String itemId;
    @SerializedName("ItemName")
    @Expose
    private String itemName;
    @SerializedName("ItemnameArabic")
    @Expose
    private String itemnameArabic;
    @SerializedName("BatchId")
    @Expose
    private String batchId;
    @SerializedName("StickerQty")
    @Expose
    private String stickerQty;
    @SerializedName("Config")
    @Expose
    private String config;

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

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getStickerQty() {
        return stickerQty;
    }

    public void setStickerQty(String stickerQty) {
        this.stickerQty = stickerQty;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

}
