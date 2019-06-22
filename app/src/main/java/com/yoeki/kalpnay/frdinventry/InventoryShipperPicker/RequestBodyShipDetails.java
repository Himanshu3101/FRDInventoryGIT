package com.yoeki.kalpnay.frdinventry.InventoryShipperPicker;

public class RequestBodyShipDetails {
    String RequisitionNo;
    String FromLocation;
    String ToLocation;
    String WebUser;
    String ItemList;

    public RequestBodyShipDetails(String RequisitionNo, String FromLocation, String ToLocation, String WebUser, String ItemList) {
        this.RequisitionNo = RequisitionNo;
        this.FromLocation = FromLocation;
        this.ToLocation = ToLocation;
        this.WebUser = WebUser;
        this.ItemList = ItemList;
    }
}
