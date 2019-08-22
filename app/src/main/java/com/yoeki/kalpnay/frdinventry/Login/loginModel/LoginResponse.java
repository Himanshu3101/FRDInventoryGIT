package com.yoeki.kalpnay.frdinventry.Login.loginModel;



import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

@SerializedName("Status")
@Expose
private String status;
@SerializedName("Message")
@Expose
private String message;
@SerializedName("UserLoginResponse")
@Expose
private List<UserLoginResponse> userLoginResponse = null;
@SerializedName("WareHOuse")
@Expose
private List<wareHouseResponse> wareHouse = null;
@SerializedName("AccessRight")
@Expose
private List<AccessRight> accessRight = null;


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

public List<UserLoginResponse> getUserLoginResponse() {
return userLoginResponse;
}

public void setUserLoginResponse(List<UserLoginResponse> userLoginResponse) {
this.userLoginResponse = userLoginResponse;
}

public List<wareHouseResponse> getwareHouseResponse() {
    return wareHouse;
}

public void setwareHouseResponse(List<wareHouseResponse> wareHouse) {
        this.wareHouse = wareHouse;
    }

public List<AccessRight> getAccessRightList() {
    return accessRight;
}

public void setAccessRightList(List<AccessRight> accessRight) {
        this.accessRight = accessRight;
    }

}


