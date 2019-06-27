package com.yoeki.kalpnay.frdinventry.Api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yoeki.kalpnay.frdinventry.MRN.Model.StickerSeq;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Preference {
    private static final String SHARED_PREF_NAME = "frdInventory";
    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";
    private static final String PASSWORD = "password";
    private static final String CHANGEPASSWORD = "1";
    private static final String ReloadedDatabase = "reloadedDatabase";
    private static final String TempSeqList = "tempSeqList";

//    private static final String Bank_Details = "bank_detail";
//    private static final String Count_Details = "count_details";
//    private static final String Total_Validate_Account = "total_validate_accounts";

    private static Preference mInstance;
    private static Context mCtx;

    public Preference(Context context) {
        mCtx = context;
    }

    public static synchronized Preference getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Preference(context);
        }
        return mInstance;
    }

    public boolean setPswrdChange(String valuePswd) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CHANGEPASSWORD, valuePswd);
        editor.apply();
        return true;
    }

    public String getPswrdChange() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CHANGEPASSWORD, CHANGEPASSWORD);
    }

    public boolean saveuserId(String user_id) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, user_id);
        editor.apply();
        return true;
    }

    public String getUserId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_ID, "");
    }

    public boolean LogOut() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        editor.apply();
        return true;
    }

    public boolean savepswd(String password) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PASSWORD, password);
        editor.apply();
        return true;
    }

    public boolean forDashboardDatabase(String reloaedDatabase) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ReloadedDatabase, reloaedDatabase);
        editor.apply();
        return true;
    }

    public void ClearforDashboardDatabase() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(ReloadedDatabase).commit();

    }


    public String getPswd() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PASSWORD, null);
    }

    public boolean saveuserName(String username) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, username);
        editor.apply();
        return true;
    }

   /* public boolean savebankDetails(List<Bank> bankdetails) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(bankdetails);
        editor.putString(Count_Details, json);
        editor.apply();
        return true;
    }

    public void saveCounts(ArrayList<String> counts) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(counts);
        editor.putString(Bank_Details, json);
        editor.apply();
    }

    public ArrayList<String> getSaveCounts() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(Bank_Details, null);
        Type type = new TypeToken<List<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }*/



    public String forDatabase() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ReloadedDatabase, "");
    }

    public void clearuserSession() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        Log.e("SharedPrefManager", "session cleared...");
    }

    public void saveTempSeqList(List<StickerSeq> tempSeqList) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String strJson = gson.toJson(tempSeqList);
        editor.putString(TempSeqList, strJson);
        editor.apply();
    }

    public ArrayList<StickerSeq> getTempSeqList() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(TempSeqList, null);
        Type type = new TypeToken<List<StickerSeq>>() {}.getType();
        return gson.fromJson(json, type);
    }

    /*public void totalValidateAccounts(ArrayList<TotalValidate> totalValidatesLS, String key) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(totalValidatesLS);
        editor.putString(key, json);
        editor.apply();
    }

    public ArrayList<TotalValidate> getTotalValidateAccounts(String key) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        Type type = new TypeToken<List<TotalValidate>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void totalSuccess(ArrayList<TotalValidate> successesLS, String key) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(successesLS);
        editor.putString(key, json);
        editor.apply();
    }

    public ArrayList<TotalValidate> getTotalSuccess(String key) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        Type type = new TypeToken<List<TotalValidate>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
    public void totalUnsuccess(ArrayList<TotalValidate> unSuccessesLS, String key) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(unSuccessesLS);
        editor.putString(key, json);
        editor.apply();
    }

    public ArrayList<TotalValidate> getTotalUnsuccess(String key) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        Type type = new TypeToken<List<TotalValidate>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
    public void accClosed(ArrayList<TotalValidate> accountclosedLS, String key) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(accountclosedLS);
        editor.putString(key, json);
        editor.apply();
    }

    public ArrayList<TotalValidate> getaccClosed(String key) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        Type type = new TypeToken<List<TotalValidate>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void accInactive(ArrayList<TotalValidate> accountInactiveLS, String key) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(accountInactiveLS);
        editor.putString(key, json);
        editor.apply();
    }

    public ArrayList<TotalValidate> getaccInactive(String key) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        Type type = new TypeToken<List<TotalValidate>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void accInvalid(ArrayList<TotalValidate> accountInvalidLS, String key) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(accountInvalidLS);
        editor.putString(key, json);
        editor.apply();
    }

    public ArrayList<TotalValidate> getaccInvalid(String key) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        Type type = new TypeToken<List<TotalValidate>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
    public void techIssue(ArrayList<TotalValidate> technicalIssueLS, String key) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(technicalIssueLS);
        editor.putString(key, json);
        editor.apply();
    }

    public ArrayList<TotalValidate> gettechIssue(String key) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        Type type = new TypeToken<List<TotalValidate>>() {
        }.getType();
        return gson.fromJson(json, type);
    }*/
}