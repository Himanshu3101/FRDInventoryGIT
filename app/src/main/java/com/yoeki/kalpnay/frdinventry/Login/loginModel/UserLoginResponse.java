package com.yoeki.kalpnay.frdinventry.Login.loginModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLoginResponse {

@SerializedName("UserId")
@Expose
private String userId;
@SerializedName("UserPin")
@Expose
private String userPin;
@SerializedName("UserRole")
@Expose
private String userRole;

public String getUserId() {
return userId;
}

public void setUserId(String userId) {
this.userId = userId;
}

public String getUserPin() {
return userPin;
}

public void setUserPin(String userPin) {
this.userPin = userPin;
}

public String getUserRole() {
    return userRole;
}

public void setUserRole(String userRole) {
    this.userRole = userRole;
}

}
