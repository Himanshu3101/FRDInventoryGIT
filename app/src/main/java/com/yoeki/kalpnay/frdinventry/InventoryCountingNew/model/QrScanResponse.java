package com.yoeki.kalpnay.frdinventry.InventoryCountingNew.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yoeki.kalpnay.frdinventry.QRDetails.ResponseBodyQRDetails;

public class QrScanResponse {

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("dataList")
    @Expose
    private List<QrDetail> dataList = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<QrDetail> getDataList() {
        return dataList;
    }

    public void setDataList(List<QrDetail> dataList) {
        this.dataList = dataList;
    }

}

