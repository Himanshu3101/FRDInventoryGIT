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

}
