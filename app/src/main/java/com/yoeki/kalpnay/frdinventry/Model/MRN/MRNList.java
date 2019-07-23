package com.yoeki.kalpnay.frdinventry.Model.MRN;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MRNList implements Comparable<MRNList> {

    @SerializedName("MRNNumber")
    @Expose
    private String mRNNumber;
    @SerializedName("MRNDate")
    @Expose
    private String mRNDate;
    @SerializedName("ActivityNumber")
    @Expose
    private String activityNumber;

    public String getMRNNumber() {
        return mRNNumber;
    }

    public void setMRNNumber(String mRNNumber) {
        this.mRNNumber = mRNNumber;
    }

    public String getMRNDate() {
        return mRNDate;
    }

    public void setMRNDate(String mRNDate) {
        this.mRNDate = mRNDate;
    }

    public String getActivityNumber() {
        return activityNumber;
    }

    public void setActivityNumber(String activityNumber) {
        this.activityNumber = activityNumber;
    }

    @Override
    public int compareTo(MRNList o) {
        Date o1date = null;
        Date o2date = null;
        String oD1 = o.getMRNDate();
        String oD2 = getMRNDate();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm:ss");
        try {
            o1date = format.parse(oD1);
            o2date = format.parse(oD2);
            return (o1date.compareTo(o2date));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }


//    KMR infotech sec-63,noida
}
