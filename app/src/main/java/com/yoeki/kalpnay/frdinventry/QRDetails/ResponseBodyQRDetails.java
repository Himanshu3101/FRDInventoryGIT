package com.yoeki.kalpnay.frdinventry.QRDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yoeki.kalpnay.frdinventry.Api.UpdateJournalList;
import com.yoeki.kalpnay.frdinventry.Dashboard.ReasonModule;

import java.util.List;

public class ResponseBodyQRDetails {

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("VendorId")
    @Expose
    private String vendorId;
    @SerializedName("PONumber")
    @Expose
    private String pONumber;
    @SerializedName("ItemId")
    @Expose
    private String itemId;
    @SerializedName("ItemName")
    @Expose
    private String itemName;
    @SerializedName("ItemnameArabic")
    @Expose
    private String itemnameArabic;
    @SerializedName("Expdate")
    @Expose
    private String expdate;
    @SerializedName("BatchId")
    @Expose
    private String batchId;
    @SerializedName("Config")
    @Expose
    private String config;
    @SerializedName("VendorName")
    @Expose
    private String vendorName;
    @SerializedName("StickerQty")
    @Expose
    private String stickerQty;
    @SerializedName("BatchAutoIncreeId")
    @Expose
    private String batchAutoIncreeId;
    @SerializedName("UnitId")
    @Expose
    private String unitId;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }


    public String getBatchAutoIncreeId() {
        return batchAutoIncreeId;
    }

    public void setBatchAutoIncreeId(String batchAutoIncreeId) {
        batchAutoIncreeId = batchAutoIncreeId;
    }



    @SerializedName("Journal")
    @Expose
    private String journal;
    @SerializedName("JounralID")
    @Expose
    private String jounralID;

    @SerializedName("JounralNO")
    @Expose
    private List<ResponseBodyQRDetails> jounralNOList = null;







    @SerializedName("Reason")
    @Expose
    private List<ReasonModule> reason = null;


    public List<ReasonModule> getReasonList() {
        return reason;
    }

    public void setReasonList(List<ReasonModule> reason) {
        this.reason = reason;
    }












    public String getDeleteStatus() {
        return DeleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        DeleteStatus = deleteStatus;
    }

    private String DeleteStatus;


    @SerializedName("WareHouse")
    @Expose
    private String wareHouse;
    @SerializedName("StickerSeqNo")
    @Expose
    private String sequenceNo;
    @SerializedName("ConsumeQty")
    @Expose
    private String consumeQty;
    @SerializedName("TotalMrn")
    @Expose
    private String totalMrn;
    @SerializedName("TotalPending")
    @Expose
    private String totalPending;
    @SerializedName("Totalcomplete")
    @Expose
    private String totalcomplete;


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

    public String getVendorId() {
    return vendorId;
    }

    public void setVendorId(String vendorId) {
    this.vendorId = vendorId;
    }

    public String getPONumber() {
    return pONumber;
    }

    public void setPONumber(String pONumber) {
    this.pONumber = pONumber;
    }

    public String getItemId() {
    return itemId;
    }

    public void setItemId(String itemId) {
    this.itemId = itemId;
    }

    public String getItemName() {
    return itemName;
    }

    public void setItemName(String itemName) {
    this.itemName = itemName;
    }

    public String getItemnameArabic() {
    return itemnameArabic;
    }

    public void setItemnameArabic(String itemnameArabic) {
    this.itemnameArabic = itemnameArabic;
    }

    public String getExpdate() {
    return expdate;
    }

    public void setExpdate(String expdate) {
    this.expdate = expdate;
    }

    public String getBatchId() {
    return batchId;
    }

    public void setBatchId(String batchId) {
    this.batchId = batchId;
    }

    public String getConfig() {
    return config;
    }

    public void setConfig(String config) {
    this.config = config;
    }

    public String getVendorName() {
    return vendorName;
    }

    public void setVendorName(String vendorName) {
    this.vendorName = vendorName;
    }

    public String getStickerQty() {
    return stickerQty;
    }

    public void setStickerQty(String stickerQty) {
    this.stickerQty = stickerQty;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getWareHouse() {
        return wareHouse;
    }

    public void setWareHouse(String wareHouse) {
        this.wareHouse = wareHouse;
    }

    public String getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(String sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getConsumeQty() {
        return consumeQty;
    }

    public void setConsumeQty(String consumeQty) {
        this.consumeQty = consumeQty;
    }

    public String getTotalMrn() {
        return totalMrn;
    }

    public void setTotalMrn(String totalMrn) {
        this.totalMrn = totalMrn;
    }

    public String getTotalPending() {
        return totalPending;
    }

    public void setTotalPending(String totalPending) {
        this.totalPending = totalPending;
    }

    public String getTotalcomplete() {
        return totalcomplete;
    }

    public void setTotalcomplete(String totalcomplete) {
        this.totalcomplete = totalcomplete;
    }

    public List<ResponseBodyQRDetails> getJounralNOList() {
        return jounralNOList;
    }

    public void setJounralNOList(List<ResponseBodyQRDetails> jounralNOList) {
        this.jounralNOList = jounralNOList;
    }

    public String getJounralID() {
        return jounralID;
    }

    public void setJounralID(String jounralID) {
        this.jounralID = jounralID;
    }

}






