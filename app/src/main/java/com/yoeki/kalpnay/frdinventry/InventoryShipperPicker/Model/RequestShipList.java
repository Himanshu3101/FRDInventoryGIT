package com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model;

public class RequestShipList {
    private String itemId;
    private String Scanqty;
    private String Config;
    private String BatchId;
    private String RemainingQty;
    private String Reason;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getScanqty() {
        return Scanqty;
    }

    public void setScanqty(String scanqty) {
        Scanqty = scanqty;
    }

    public String getConfig() {
        return Config;
    }

    public void setConfig(String config) {
        Config = config;
    }

    public String getBatchId() {
        return BatchId;
    }

    public void setBatchId(String batchId) {
        BatchId = batchId;
    }

    public String getRemainingQty() {
        return RemainingQty;
    }

    public void setRemainingQty(String remainingQty) {
        RemainingQty = remainingQty;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }
}
