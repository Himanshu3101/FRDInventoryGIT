package com.yoeki.kalpnay.frdinventry.banchInventoryReceiving.model;

public class InventoryControlDetailModel {

    String itemNo, ItemName, approvedQty, receivedQty, remainingQty;

    public InventoryControlDetailModel(String itemNo, String itemName, String approvedQty, String receivedQty, String remainingQty) {
        this.itemNo = itemNo;
        ItemName = itemName;
        this.approvedQty = approvedQty;
        this.receivedQty = receivedQty;
        this.remainingQty = remainingQty;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getApprovedQty() {
        return approvedQty;
    }

    public void setApprovedQty(String approvedQty) {
        this.approvedQty = approvedQty;
    }

    public String getReceivedQty() {
        return receivedQty;
    }

    public void setReceivedQty(String receivedQty) {
        this.receivedQty = receivedQty;
    }

    public String getRemainingQty() {
        return remainingQty;
    }

    public void setRemainingQty(String remainingQty) {
        this.remainingQty = remainingQty;
    }
}
