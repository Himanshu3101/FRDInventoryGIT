package com.yoeki.kalpnay.frdinventry.Api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JournalList {

    @SerializedName("Journal")
    @Expose
    private String Journal;

    public String getJounralNo() {
        return Journal;
    }

    public void setJounralNo(String Journals) {
        Journal = Journals;
    }
}
