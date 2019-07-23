package com.yoeki.kalpnay.frdinventry.Login.loginModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccessRight {

    @SerializedName("MRN")
    @Expose
    private String mRN;
    @SerializedName("shipperList")
    @Expose
    private String shipperList;
    @SerializedName("BranchinventoryReceiving")
    @Expose
    private String branchinventoryReceiving;
    @SerializedName("inventoryCounting")
    @Expose
    private String inventoryCounting;
    @SerializedName("QRDetails")
    @Expose
    private String qRDetails;
    @SerializedName("QuantityUpdateDetails")
    @Expose
    private String quantityUpdateDetails;

    public String getMRN() {
        return mRN;
    }

    public void setMRN(String mRN) {
        this.mRN = mRN;
    }

    public String getShipperList() {
        return shipperList;
    }

    public void setShipperList(String shipperList) {
        this.shipperList = shipperList;
    }

    public String getBranchinventoryReceiving() {
        return branchinventoryReceiving;
    }

    public void setBranchinventoryReceiving(String branchinventoryReceiving) {
        this.branchinventoryReceiving = branchinventoryReceiving;
    }

    public String getInventoryCounting() {
        return inventoryCounting;
    }

    public void setInventoryCounting(String inventoryCounting) {
        this.inventoryCounting = inventoryCounting;
    }

    public String getQRDetails() {
        return qRDetails;
    }

    public void setQRDetails(String qRDetails) {
        this.qRDetails = qRDetails;
    }

    public String getQuantityUpdateDetails() {
        return quantityUpdateDetails;
    }

    public void setQuantityUpdateDetails(String quantityUpdateDetails) {
        this.quantityUpdateDetails = quantityUpdateDetails;
    }
}
