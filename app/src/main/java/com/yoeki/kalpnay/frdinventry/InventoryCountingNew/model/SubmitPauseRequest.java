package com.yoeki.kalpnay.frdinventry.InventoryCountingNew.model;

import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.StickerList;

import java.util.ArrayList;

public class SubmitPauseRequest {
    String IsSubmit, UserId, ScheduleActivityNo, SID, WareHouseNo;
    ArrayList<StickerSeqList> StickerSequenceList;

    public SubmitPauseRequest(String isSubmit, String userId, String scheduleActivityNo, String SID, String wareHouseNo, ArrayList<StickerSeqList> stickerSequenceList) {
        IsSubmit = isSubmit;
        UserId = userId;
        ScheduleActivityNo = scheduleActivityNo;
        this.SID = SID;
        WareHouseNo = wareHouseNo;
        StickerSequenceList = stickerSequenceList;
    }

    public String getIsSubmit() {
        return IsSubmit;
    }

    public void setIsSubmit(String isSubmit) {
        IsSubmit = isSubmit;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getScheduleActivityNo() {
        return ScheduleActivityNo;
    }

    public void setScheduleActivityNo(String scheduleActivityNo) {
        ScheduleActivityNo = scheduleActivityNo;
    }

    public String getSID() {
        return SID;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }

    public String getWareHouseNo() {
        return WareHouseNo;
    }

    public void setWareHouseNo(String wareHouseNo) {
        WareHouseNo = wareHouseNo;
    }

    public ArrayList<StickerSeqList> getStickerSequenceList() {
        return StickerSequenceList;
    }

    public void setStickerSequenceList(ArrayList<StickerSeqList> stickerSequenceList) {
        StickerSequenceList = stickerSequenceList;
    }
}
