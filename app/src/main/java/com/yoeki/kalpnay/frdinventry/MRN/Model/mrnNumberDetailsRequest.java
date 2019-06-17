package com.yoeki.kalpnay.frdinventry.MRN.Model;

public class mrnNumberDetailsRequest {
    public String MNRNumber;
    public String UserId;

    public mrnNumberDetailsRequest(String MNRNumber,String UserId) {
        this.MNRNumber = MNRNumber;
        this.UserId = UserId;
    }
}
