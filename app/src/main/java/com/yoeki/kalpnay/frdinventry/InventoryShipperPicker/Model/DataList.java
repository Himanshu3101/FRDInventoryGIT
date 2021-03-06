package com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataList implements Comparable<DataList> {

@SerializedName("RequisitionNo")
@Expose
private String requisitionNo;
@SerializedName("LocationId")
@Expose
private String location;
@SerializedName("WareHouseName")
@Expose
private String wareHouseName;

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

public String getwareHouseName() {
return wareHouseName;
}

public void setwareHouseName(String wareHouseName) {
this.wareHouseName = wareHouseName;
}

    @Override
    public int compareTo(DataList o) {
        String[] oD1 = o.getRequisitionNo().split("-");
        String[] oD2 = getRequisitionNo().split("-");
        return (oD1[1].compareTo(oD2[1]));
    }
}


