package com.yoeki.kalpnay.frdinventry.InventoryShipperPicker;

import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.StickerList;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.commonReceivingShippingDetailDataList;

import java.util.List;

public class RequestBodyShipDetails {
    String RequisitionNo;
    String FromLocation;
    String ToLocation;
    String WebUser;
    List<commonReceivingShippingDetailDataList> ItemList;
    List<StickerList> StickerList;

    public RequestBodyShipDetails(String RequisitionNo, String FromLocation, String ToLocation, String WebUser,  List<commonReceivingShippingDetailDataList> ItemList, List<StickerList> StickerList) {
        this.RequisitionNo = RequisitionNo;
        this.FromLocation = FromLocation;
        this.ToLocation = ToLocation;
        this.WebUser = WebUser;
        this.ItemList = ItemList;
        this.StickerList = StickerList;
    }
}
