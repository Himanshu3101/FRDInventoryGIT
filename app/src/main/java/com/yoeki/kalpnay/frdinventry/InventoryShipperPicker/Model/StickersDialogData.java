package com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model;

public class StickersDialogData {
    private String stickerSeq;
    private String batchNo;
    private String itemId;

    public StickersDialogData(String stickerSeq, String batchNo, String itemId) {
        this.stickerSeq = stickerSeq;
        this.batchNo = batchNo;
        this.itemId = itemId;
    }

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

    public String getStickerSeq() {
        return stickerSeq;
    }

    public void setStickerSeq(String stickerSeq) {
        this.stickerSeq = stickerSeq;
    }
}
