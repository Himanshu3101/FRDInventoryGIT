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
    @SerializedName("StickerSequenceId")
    @Expose
    private String stickerSequenceId;

    private String mrnNo;

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

    public String getMrnNo() {
        return mrnNo;
    }

    public void setMrnNo(String mrnNo) {
        this.mrnNo = mrnNo;
    }

    public String getStickerSequenceId() {
        return stickerSequenceId;
    }

    public void setStickerSequenceId(String stickerSequenceId) {
        this.stickerSequenceId = stickerSequenceId;
    }
}