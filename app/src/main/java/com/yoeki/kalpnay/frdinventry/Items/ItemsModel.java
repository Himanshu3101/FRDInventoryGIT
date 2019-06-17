package com.yoeki.kalpnay.frdinventry.Items;

public class ItemsModel {
    String Itemno;
    String itemname;
    String approvedqty;
    String pickedqty;
    String remainingqty;
    String reason;
    String request;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getItemno() {
        return Itemno;
    }

    public void setItemno(String itemno) {
        Itemno = itemno;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getApprovedqty() {
        return approvedqty;
    }

    public void setApprovedqty(String approvedqty) {
        this.approvedqty = approvedqty;
    }

    public String getPickedqty() {
        return pickedqty;
    }

    public void setPickedqty(String pickedqty) {
        this.pickedqty = pickedqty;
    }

    public String getRemainingqty() {
        return remainingqty;
    }

    public void setRemainingqty(String remainingqty) {
        this.remainingqty = remainingqty;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
