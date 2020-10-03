package com.yoeki.kalpnay.frdinventry.InventoryCountingNew.model;

public class ScanQrRequest {
    String SID, ScheduleActivityNo, WareHouseNo, UserId, StickerSeq;

    public ScanQrRequest(String SID, String scheduleActivityNo, String wareHouseNo, String userId, String stickerSeq) {
        this.SID = SID;
        ScheduleActivityNo = scheduleActivityNo;
        WareHouseNo = wareHouseNo;
        UserId = userId;
        StickerSeq = stickerSeq;
    }

    public String getSID() {
        return SID;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }

    public String getScheduleActivityNo() {
        return ScheduleActivityNo;
    }

    public void setScheduleActivityNo(String scheduleActivityNo) {
        ScheduleActivityNo = scheduleActivityNo;
    }

    public String getWareHouseNo() {
        return WareHouseNo;
    }

    public void setWareHouseNo(String wareHouseNo) {
        WareHouseNo = wareHouseNo;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getStickerSeq() {
        return StickerSeq;
    }

    public void setStickerSeq(String stickerSeq) {
        StickerSeq = stickerSeq;
    }
}
