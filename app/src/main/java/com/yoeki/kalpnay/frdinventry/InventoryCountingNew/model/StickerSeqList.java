package com.yoeki.kalpnay.frdinventry.InventoryCountingNew.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StickerSeqList {
    @SerializedName("StickerSeq")
    @Expose
    private String stickerSeq;

    @SerializedName("StickerSeqId")
    @Expose
    private String stickerSeqId;

    public StickerSeqList(String stickerSeq, String stickerSeqId) {
        this.stickerSeq = stickerSeq;
        this.stickerSeqId = stickerSeqId;
    }

    public String getStickerSeq() {
        return stickerSeq;
    }

    public void setStickerSeq(String stickerSeq) {
        this.stickerSeq = stickerSeq;
    }

    public String getStickerSeqId() {
        return stickerSeqId;
    }

    public void setStickerSeqId(String stickerSeqId) {
        this.stickerSeqId = stickerSeqId;
    }
}
