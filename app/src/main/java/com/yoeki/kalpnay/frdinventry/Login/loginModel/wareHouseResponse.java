package com.yoeki.kalpnay.frdinventry.Login.loginModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class wareHouseResponse {
    @SerializedName("WareHouseId")
    @Expose
    private String wareHouseId;
    @SerializedName("WareHouse")
    @Expose
    private String WareHouse;

    public String getWareHouseId() {
        return wareHouseId;
    }

    public void setWareHouseId(String wareHouseId) {
        this.wareHouseId = wareHouseId;
    }

    public String getWareHouse() {
        return WareHouse;
    }

    public void setWareHouse(String WareHouse) {
        this.WareHouse = WareHouse;
    }
}
