package com.yoeki.kalpnay.frdinventry.InventoryShipperPicker;

import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.StickerList;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.commonReceivingShippingDetailDataList;

import java.util.List;

public class SaveDataTemp{
    List<commonReceivingShippingDetailDataList> commonReceivingShippingDetailDataLists;
    String reqNmbr;
    List<StickerList> sequenceQRNumber;

    public SaveDataTemp(List<commonReceivingShippingDetailDataList> commonReceivingShippingDetailDataLists, String reqNmbr, List<StickerList> sequenceQRNumber) {
        this.commonReceivingShippingDetailDataLists = commonReceivingShippingDetailDataLists;
        this.reqNmbr = reqNmbr;
        this.sequenceQRNumber = sequenceQRNumber;
    }

    public List<StickerList> getSequenceQRNumber() {
        return sequenceQRNumber;
    }

    public void setSequenceQRNumber(List<StickerList> sequenceQRNumber) {
        this.sequenceQRNumber = sequenceQRNumber;
    }

    public String getReqNmbr() {
        return reqNmbr;
    }

    public void setReqNmbr(String reqNmbr) {
        this.reqNmbr = reqNmbr;
    }

    public List<commonReceivingShippingDetailDataList> getCommonReceivingShippingDetailDataLists() {
        return commonReceivingShippingDetailDataLists;
    }

    public void setCommonReceivingShippingDetailDataLists(List<commonReceivingShippingDetailDataList> commonReceivingShippingDetailDataLists) {
        this.commonReceivingShippingDetailDataLists = commonReceivingShippingDetailDataLists;
    }
}
