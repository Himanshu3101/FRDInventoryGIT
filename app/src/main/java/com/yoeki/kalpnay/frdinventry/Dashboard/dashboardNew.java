package com.yoeki.kalpnay.frdinventry.Dashboard;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yoeki.kalpnay.frdinventry.Api.Api;
import com.yoeki.kalpnay.frdinventry.Api.ApiInterface;
import com.yoeki.kalpnay.frdinventry.Api.JournalList;
import com.yoeki.kalpnay.frdinventry.Api.Preference;
import com.yoeki.kalpnay.frdinventry.Api.UpdateJournalList;
import com.yoeki.kalpnay.frdinventry.InventoryCounting.inventory_Counting;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.InventoryPending;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.UserIDModel;
import com.yoeki.kalpnay.frdinventry.Login.LoginActivity;
import com.yoeki.kalpnay.frdinventry.Login.loginModel.AccessRight;
import com.yoeki.kalpnay.frdinventry.MRN.MRN_Dashboard;
import com.yoeki.kalpnay.frdinventry.MRN.Model.PostingJsonResponse;
import com.yoeki.kalpnay.frdinventry.QRDetails.QrDetailActivity;
import com.yoeki.kalpnay.frdinventry.QRDetails.ResponseBodyQRDetails;
import com.yoeki.kalpnay.frdinventry.QRDetailsQuantityUpdate.QrDetailQuantityUpdate;
import com.yoeki.kalpnay.frdinventry.R;
import com.yoeki.kalpnay.frdinventry.banchInventoryReceiving.CompletedReceiving;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class dashboardNew extends AppCompatActivity implements View.OnClickListener {
    LinearLayout linearLayoutMaterialReciving, linearLayoutInventoryShipperPicker, linearLayoutBranchInventoryReciving, linearLayoutInventoryCounting, linearLayoutGlobalQrScan,
            linearLayoutQuantityUpdate;
    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView MRNCount, shipperCount, birCount/*, inventoryCount*/;
    Toolbar toolbar_top;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_new);

        initialize();

        List<AccessRight> accessRightsList = new ArrayList<>();
        accessRightsList = Preference.getInstance(getApplicationContext()).getAccessRight();

        linearLayoutMaterialReciving.setOnClickListener(this);
        linearLayoutQuantityUpdate.setOnClickListener(this);
        linearLayoutGlobalQrScan.setOnClickListener(this);
        linearLayoutBranchInventoryReciving.setOnClickListener(this);
        linearLayoutInventoryCounting.setOnClickListener(this);
        linearLayoutInventoryShipperPicker.setOnClickListener(this);

        if (accessRightsList.get(0).getMRN().equals("True")) {
            linearLayoutMaterialReciving.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }
        if (accessRightsList.get(0).getQuantityUpdateDetails().equals("True")) {
            linearLayoutQuantityUpdate.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }
        if (accessRightsList.get(0).getQRDetails().equals("True")) {
            linearLayoutGlobalQrScan.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }
        if (accessRightsList.get(0).getBranchinventoryReceiving().equals("True")) {
            linearLayoutBranchInventoryReciving.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }
        if (accessRightsList.get(0).getInventoryCounting().equals("True")) {
            linearLayoutInventoryCounting.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }
        if (accessRightsList.get(0).getShipperList().equals("True")) {
            linearLayoutInventoryShipperPicker.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }
        countingSet();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.user_details:
                try {
                    profileApi();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.logout_menu:
                openLogOutDialog(R.style.DialogAnimation, "");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initialize() {
        linearLayoutMaterialReciving = findViewById(R.id.linearLayoutMaterialReciving);
        linearLayoutInventoryShipperPicker = findViewById(R.id.linearLayoutInventoryShipperPicker);
        linearLayoutBranchInventoryReciving = findViewById(R.id.linearLayoutBranchInventoryReciving);
        linearLayoutInventoryCounting = findViewById(R.id.linearLayoutInventoryCounting);
        linearLayoutGlobalQrScan = findViewById(R.id.linearLayoutGlobalQrScan);
        linearLayoutQuantityUpdate = findViewById(R.id.linearLayoutQuantityUpdate);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefreshDashboard);
        MRNCount = findViewById(R.id.MRNCount);
        shipperCount = findViewById(R.id.shipperCount);
        birCount = findViewById(R.id.birCount);

        toolbar_top = findViewById(R.id.toolbar_top);
        setSupportActionBar(toolbar_top);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                countingSet();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void countingSet() {
        final ProgressDialog progressDialog = new ProgressDialog(dashboardNew.this);
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        progressDialog.setCancelable(false); // set cancelable to false
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        userId = Preference.getInstance(getApplicationContext()).getUserId();

        UserIDModel detailsRequest = new UserIDModel(userId);


        final List<JournalList> deleteJournalList = new ArrayList<>();

        Call<ResponseBodyQRDetails> call = apiInterface.inventoryCounting(detailsRequest);
        call.enqueue(new Callback<ResponseBodyQRDetails>() {
            @Override
            public void onResponse(Call<ResponseBodyQRDetails> call, Response<ResponseBodyQRDetails> response) {
                try {
                    progressDialog.dismiss();
                    if (response.body().getStatus().equals("Success")) {

                        List<ResponseBodyQRDetails> responseINVTempList = new ArrayList<>();
                        responseINVTempList = Preference.getInstance(getApplicationContext()).getInvenrtoryCounting();
                        List<ResponseBodyQRDetails> deleteList = new ArrayList<>();

                        if (responseINVTempList != null && responseINVTempList.size() != 0) {
                            deleteList.addAll(responseINVTempList);
                        } else {
                            responseINVTempList = new ArrayList<>();
                        }
                        if (response.body().getJounralNOList().size() != 0) {
                            for (int i = 0; i <= response.body().getJounralNOList().size() - 1; i++) {
                                String ResponseJournal = response.body().getJounralNOList().get(i).getJounralID();
                                for (int j = 0; j <= responseINVTempList.size() - 1; j++) {
                                    String prefrenceJournal = responseINVTempList.get(j).getJournal();
                                    if (ResponseJournal.equals(prefrenceJournal)) {
                                        try {
                                            deleteList.remove(responseINVTempList.get(j));
                                            responseINVTempList.get(j).setDeleteStatus("1");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                            Preference.getInstance(getApplicationContext()).saveInvenrtoryCounting(deleteList);
                            deleteList.clear();
                            UpdateJournalList updateJournalLists = new UpdateJournalList();
                            List<JournalList> journalLists = new ArrayList<>();

                            for (int i = 0; i <= responseINVTempList.size() - 1; i++) {
                                if (responseINVTempList.get(i).getDeleteStatus().equals("1")) {
                                    JournalList journalList = new JournalList();
                                    journalList.setJounralNo(responseINVTempList.get(i).getJournal());
                                    journalLists.add(journalList);
                                    updateJournalLists.setItemList(journalLists);
                                }

                            }
                            if (response.body().getJounralNOList() != null && response.body().getJounralNOList().size() != 0) {
                                deletedJournal(updateJournalLists);
                            }
                        }


                        if (response.body().getReasonList().size() != 0) {
                            Preference.getInstance(getApplicationContext()).saveReasonList(response.body().getReasonList());
                        }


                        birCount.setText(response.body().getTotalcomplete());
                        MRNCount.setText(response.body().getTotalMrn());
                        shipperCount.setText(response.body().getTotalPending());
                    } else {
                        Toast.makeText(dashboardNew.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(dashboardNew.this, "Service Unavailable.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBodyQRDetails> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(dashboardNew.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deletedJournal(final UpdateJournalList deleteList) {
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);

        Call<PostingJsonResponse> call = apiInterface.deleteJournal(deleteList);
        call.enqueue(new Callback<PostingJsonResponse>() {
            @Override
            public void onResponse(Call<PostingJsonResponse> call, Response<PostingJsonResponse> response) {
                try {
                    if (response.body().getStatus().equals("Success")) {
//                    Preference.getInstance(getApplicationContext()).saveInvenrtoryCounting(fordeletion);
                    } else {
                        deletedJournal(deleteList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<PostingJsonResponse> call, Throwable t) {
                deletedJournal(deleteList);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearLayoutMaterialReciving:
                int color = ((ColorDrawable) v.getBackground()).getColor();
                if (color == -14118778) {
                    Intent intent1 = new Intent(getApplicationContext(), MRN_Dashboard.class);
                    startActivity(intent1);
                    finish();
                } else {
                    Toast.makeText(this, "You are not Authorized Person.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.linearLayoutInventoryShipperPicker:
                int colorS = ((ColorDrawable) v.getBackground()).getColor();
                if (colorS == -14118778) {
                    Intent intent = new Intent(getApplicationContext(), InventoryPending.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "You are not Authorized Person.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.linearLayoutBranchInventoryReciving:
                int colorI = ((ColorDrawable) v.getBackground()).getColor();
                if (colorI == -14118778) {
                    Intent intent2 = new Intent(getApplicationContext(), CompletedReceiving.class);
                    startActivity(intent2);
                    finish();
                } else {
                    Toast.makeText(this, "You are not Authorized Person.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.linearLayoutInventoryCounting:
                int colorIC = ((ColorDrawable) v.getBackground()).getColor();
                if (colorIC == -14118778) {
                    Intent intent3 = new Intent(getApplicationContext(), inventory_Counting.class);
                    startActivity(intent3);
                    finish();
                } else {
                    Toast.makeText(this, "You are not Authorized Person.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.linearLayoutGlobalQrScan:
                int colorIG = ((ColorDrawable) v.getBackground()).getColor();
                if (colorIG == -14118778) {
                    Intent intent3 = new Intent(getApplicationContext(), QrDetailActivity.class);
                    startActivity(intent3);
                    finish();
                } else {
                    Toast.makeText(this, "You are not Authorized Person.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.linearLayoutQuantityUpdate:
                int colorQU = ((ColorDrawable) v.getBackground()).getColor();
                if (colorQU == -14118778) {
                    startActivity(new Intent(dashboardNew.this, QrDetailQuantityUpdate.class));
                    finish();
                } else {
                    Toast.makeText(this, "You are not Authorized Person.", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    @Override
    public void onBackPressed() {
        openLogOutDialog(R.style.DialogAnimation, "");
    }

    private void openLogOutDialog(int dialogAnimation, String s) {
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.getWindow().getAttributes().windowAnimations = dialogAnimation;

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);

        AppCompatButton button_yes = dialog.findViewById(R.id.button_yes);
        AppCompatButton button_no = dialog.findViewById(R.id.button_no);

        button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openRef = new Intent(dashboardNew.this, LoginActivity.class);
                startActivity(openRef);
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }

    public void profileApi() {
        final ProgressDialog progressDialog = new ProgressDialog(dashboardNew.this);
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        progressDialog.setCancelable(false); // set cancelable to false
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);

        UserIDModel userIDModel = new UserIDModel(userId);
        Call<UserProfile> call = apiInterface.getUserProfile(userIDModel);
        call.enqueue(new Callback<UserProfile>() {

            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                progressDialog.dismiss();
                if (response.body().getStatus().equals("Success")) {
                    dialogProfile(response);
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(dashboardNew.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void dialogProfile(Response<UserProfile> response) {
        final Dialog profileDialog = new Dialog(this);
        profileDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        profileDialog.setContentView(getLayoutInflater().inflate(R.layout.profile_dialog, null));

        AppCompatTextView Uname = profileDialog.findViewById(R.id.Uname);
        AppCompatTextView Pin = profileDialog.findViewById(R.id.Pin);
        AppCompatTextView email = profileDialog.findViewById(R.id.email);
        AppCompatTextView wareHouse = profileDialog.findViewById(R.id.wareHouse);
        AppCompatButton buttonOk = profileDialog.findViewById(R.id.buttonOk);
        Uname.setText(response.body().getUserProfileResponse().get(0).getUserName());
        Pin.setText(response.body().getUserProfileResponse().get(0).getUserPin());
        email.setText(response.body().getUserProfileResponse().get(0).getEmailId());
        wareHouse.setText(response.body().getUserProfileResponse().get(0).getWareHouse());

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileDialog.dismiss();
            }
        });

        profileDialog.show();
    }
}
