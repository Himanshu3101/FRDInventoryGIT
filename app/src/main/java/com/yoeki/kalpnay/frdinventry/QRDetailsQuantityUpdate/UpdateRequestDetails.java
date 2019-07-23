package com.yoeki.kalpnay.frdinventry.QRDetailsQuantityUpdate;

import com.yoeki.kalpnay.frdinventry.QRDetails.ResponseBodyQRDetails;

import java.util.List;

public class UpdateRequestDetails {
    String UserId;
    List<ResponseBodyQRDetails> DataList;
    public UpdateRequestDetails(String UserId, List<ResponseBodyQRDetails> DataList) {
        this.UserId=UserId;
        this.DataList = DataList;
    }
}
