package com.yoeki.kalpnay.frdinventry.QRDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BatchList {

    private String batchIdList;

    public String getBatchIdList() {
        return batchIdList;
    }

    public void setBatchIdList(String batchIdList) {
        this.batchIdList = batchIdList;
    }

}




/*
public class BatchList {

    private String batchIdList;
    private List<ResponseBodyQRDetails> responseBodyQRDetailsList = new ArrayList<>();


    public String getBatchIdList() {
        return batchIdList;
    }

    public void setBatchIdList(String batchIdList) {
        this.batchIdList = batchIdList;
    }

    public List<ResponseBodyQRDetails> getresponseBodyQRDetailsList() {
        return responseBodyQRDetailsList;
    }

    public void setresponseBodyQRDetailsList(List<ResponseBodyQRDetails> responseBodyQRDetailsList) {
        this.responseBodyQRDetailsList = responseBodyQRDetailsList;
    }



}
*/
