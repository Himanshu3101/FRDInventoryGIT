package com.yoeki.kalpnay.frdinventry.InventoryCounting;

import com.yoeki.kalpnay.frdinventry.QRDetails.ResponseBodyQRDetails;

import java.util.List;

public class RequestBodyIVCDetails {

    String JounralNo;
    String UserId;
    String Location;
    List<ResponseBodyQRDetails> ItemList;

    public RequestBodyIVCDetails(String JounralNo, String UserId, String Location, List<ResponseBodyQRDetails> ItemList) {
        this.JounralNo = JounralNo;
        this.UserId = UserId;
        this.Location = Location;
        this.ItemList = ItemList;
    }
}
