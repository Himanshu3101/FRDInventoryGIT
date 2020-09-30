package com.yoeki.kalpnay.frdinventry.QRDetails;

import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.StickerList;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.StickersDialogData;

import java.util.ArrayList;
import java.util.List;

public class RequisitionWiseQRDetail {
    String requisitionNo;
    ArrayList<ResponseBodyQRDetails> responses;
    List<StickerList> sequenceQRNumber;
    List<StickersDialogData> stickersDialogDataList;

    public RequisitionWiseQRDetail(String requisitionNo, ArrayList<ResponseBodyQRDetails> responses, List<StickerList> sequenceQRNumber, List<StickersDialogData> stickersDialogDataList) {
        this.requisitionNo = requisitionNo;
        this.responses = responses;
        this.sequenceQRNumber = sequenceQRNumber;
        this.stickersDialogDataList = stickersDialogDataList;
    }

    public String getRequisitionNo() {
        return requisitionNo;
    }

    public void setRequisitionNo(String requisitionNo) {
        this.requisitionNo = requisitionNo;
    }

    public ArrayList<ResponseBodyQRDetails> getResponses() {
        return responses;
    }

    public void setResponses(ArrayList<ResponseBodyQRDetails> responses) {
        this.responses = responses;
    }

    public List<StickerList> getSequenceQRNumber() {
        return sequenceQRNumber;
    }

    public void setSequenceQRNumber(List<StickerList> sequenceQRNumber) {
        this.sequenceQRNumber = sequenceQRNumber;
    }

    public List<StickersDialogData> getStickersDialogDataList() {
        return stickersDialogDataList;
    }

    public void setStickersDialogDataList(List<StickersDialogData> stickersDialogDataList) {
        this.stickersDialogDataList = stickersDialogDataList;
    }
}
