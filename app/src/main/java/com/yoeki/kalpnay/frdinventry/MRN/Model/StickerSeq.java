package com.yoeki.kalpnay.frdinventry.MRN.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StickerSeq {

    @SerializedName("StickerSequence")
    @Expose
    private String stickerSequence;
    @SerializedName("StickerQty")
    @Expose
    private String stickerQty;

    public String getStickerSequence() {
        return stickerSequence;
    }

    public void setStickerSequence(String stickerSequence) {
        this.stickerSequence = stickerSequence;
    }

    public String getStickerQty() {
        return stickerQty;
    }

    public void setStickerQty(String stickerQty) {
        this.stickerQty = stickerQty;
    }

}