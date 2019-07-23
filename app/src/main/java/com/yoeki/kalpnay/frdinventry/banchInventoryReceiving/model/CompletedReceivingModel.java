package com.yoeki.kalpnay.frdinventry.banchInventoryReceiving.model;

public class CompletedReceivingModel {
    String requisitionNo;
    String location;

    public CompletedReceivingModel(String requisitionNo, String location) {
        this.requisitionNo = requisitionNo;
        this.location = location;
    }

    public String getRequisitionNo() {
        return requisitionNo;
    }

    public void setRequisitionNo(String requisitionNo) {
        this.requisitionNo = requisitionNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
