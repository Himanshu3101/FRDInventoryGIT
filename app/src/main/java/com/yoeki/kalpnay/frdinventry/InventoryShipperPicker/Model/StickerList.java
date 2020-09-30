package com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StickerList {
    @SerializedName("StickerSeq")
    @Expose
    private String stickerSeq;

    public StickerList(String stickerSeq) {
        this.stickerSeq = stickerSeq;
    }

    public String getStickerSeq() {
        return stickerSeq;
    }

    public void setStickerSeq(String stickerSeq) {
        this.stickerSeq = stickerSeq;
    }
}
