package com.yoeki.kalpnay.frdinventry.banchInventoryReceiving;

import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.commonReceivingShippingDetailDataList;

import java.util.ArrayList;
import java.util.List;

public class RequestBodyReceiveDetails {
    String RequisitionNo;
    String UserId;
    List<commonReceivingShippingDetailDataList> ItemList;

    public RequestBodyReceiveDetails(String RequisitionNo, String UserId, ArrayList<commonReceivingShippingDetailDataList> ItemList) {
        this.RequisitionNo = RequisitionNo;
        this.UserId = UserId;
        this.ItemList = ItemList;
    }
}
