package com.yoeki.kalpnay.frdinventry.InventoryShipperPicker;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yoeki.kalpnay.frdinventry.Api.Api;
import com.yoeki.kalpnay.frdinventry.Api.ApiInterface;
import com.yoeki.kalpnay.frdinventry.Api.Preference;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.InventoryPendingModel;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.ParticularRequisitionDetails;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.RequestShipList;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.ResponseShippingDetails;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.detailDataList;
import com.yoeki.kalpnay.frdinventry.Items.ItemsModel;
import com.yoeki.kalpnay.frdinventry.Items.RequestedItemsFragment;
import com.yoeki.kalpnay.frdinventry.Items.SelectedItemsFragment;
import com.yoeki.kalpnay.frdinventry.Items.requestControlDetailList;
import com.yoeki.kalpnay.frdinventry.MRN.Adapter.MRN_Dashboard_AdapterDetails;
import com.yoeki.kalpnay.frdinventry.QRDetails.QrDetailActivity;
import com.yoeki.kalpnay.frdinventry.QRDetails.RequestBodyQRDetails;
import com.yoeki.kalpnay.frdinventry.QRDetails.ResponseBodyQRDetails;
import com.yoeki.kalpnay.frdinventry.R;
import com.yoeki.kalpnay.frdinventry.dashboardInventoryRequisition;

import org.json.JSONArray;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequisitionControlDetails extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rcy_items;
    AppCompatTextView reqNo, rcd_Date, branchName_RCD;
    ArrayList<ItemsModel> arraylist;
    AppCompatButton shipBtn, languageChangeRCD, img_back;
    String wareHouse,qrDetails;
    AppCompatAutoCompleteTextView scanQR;
    List<detailDataList> listRCDDetailsList ;
    ArrayList<detailDataList> forShipping;
    requestControlDetailList adapter;
    int languageChangeVisible = 1;
    ArrayList<String> sequenceQRNumber= new ArrayList<>();
    String valueRem = "";
    String valueElseRem = "";
    String reqNmbr="", locationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_control_detail);
        setupWindowAnimations();
        arraylist = new ArrayList<>();
        Initialize();
        reqNmbr = getIntent().getStringExtra("RequisitionNo");
        wareHouse = getIntent().getStringExtra("wareHouse");
        locationId = getIntent().getStringExtra("locationCode");
        reqNo.setText(reqNmbr);
        addlistdata();

        img_back.setOnClickListener(this);
        shipBtn.setOnClickListener(this);
        languageChangeRCD.setOnClickListener(this);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                qrDetails = String.valueOf(editable);
                if (qrDetails.length() >= 10) {
                    if (sequenceQRNumber.contains(qrDetails)) {
                        invalidItemDialog();
                    } else {
                        sequenceQRNumber.add(qrDetails);
                        qrRCDDetails();
                    }
                }
            }
        };
        scanQR.addTextChangedListener(textWatcher);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void Initialize() {
        reqNo = findViewById(R.id.reqNo);
        rcd_Date = findViewById(R.id.rcd_Date);
        branchName_RCD = findViewById(R.id.branchName_RCD);
        scanQR = findViewById(R.id.scanQR);
        shipBtn = findViewById(R.id.shipBtn);
        img_back = findViewById(R.id.img_back);
        languageChangeRCD = findViewById(R.id.languageChangeRCD);
        rcy_items = findViewById(R.id.rcy_items);
    }

    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(2000);
        getWindow().setEnterTransition(fade);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                Intent intent = new Intent(getApplicationContext(), InventoryPending.class);
                startActivity(intent);
                finish();
                break;
            case R.id.shipBtn:
                shipping();
//                transferOrderDialog();
                break;
            case R.id.languageChangeRCD:
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                adapter = new requestControlDetailList(RequisitionControlDetails.this, listRCDDetailsList, languageChangeVisible);
                rcy_items.setLayoutManager(layoutManager);
                rcy_items.setAdapter(adapter);
                if (languageChangeVisible == 0) {
                    languageChangeVisible = 1;
                } else {
                    languageChangeVisible = 0;
                }
                break;
        }
    }

    public void addlistdata() {
        final ProgressDialog progressDialog = new ProgressDialog(RequisitionControlDetails.this);
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        progressDialog.setCancelable(false); // set cancelable to false
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);

        String userId = Preference.getInstance(getApplicationContext()).getUserId();
        String requisition = getIntent().getStringExtra("RequisitionNo");

        ParticularRequisitionDetails userIDModeParticularRequisitionDetailsl = new ParticularRequisitionDetails(userId, requisition);
        Call<InventoryPendingModel> call = apiInterface.inventoryPicker(userIDModeParticularRequisitionDetailsl);
        call.enqueue(new Callback<InventoryPendingModel>() {
            @Override
            public void onResponse(Call<InventoryPendingModel> call, Response<InventoryPendingModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.body().getStatus().equals("Success")) {

                        listRCDDetailsList = response.body().getDataList();

                        String[] Date = response.body().getDataList().get(0).getReqestDate().split("\\s+");
                        rcd_Date.setText(Date[0]);
                        branchName_RCD.setText(response.body().getDataList().get(0).getBranch());
                        rcy_items.setLayoutManager(new LinearLayoutManager(RequisitionControlDetails.this, LinearLayoutManager.VERTICAL, false));
                        rcy_items.setItemAnimator(new DefaultItemAnimator());
                        adapter = new requestControlDetailList(RequisitionControlDetails.this, listRCDDetailsList);
                        rcy_items.setAdapter(adapter);
                    } else {
                        Toast.makeText(RequisitionControlDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(RequisitionControlDetails.this, "Service Unavailable.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InventoryPendingModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RequisitionControlDetails.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void transferOrderDialog(String toNumber) {
        final Dialog dialog = new Dialog(RequisitionControlDetails.this);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.transfer_order_detail);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        AppCompatTextView to_number = dialog.findViewById(R.id.to_number);
        AppCompatButton ok_toNumbr = dialog.findViewById(R.id.ok_toNumbr);
        to_number.setText(toNumber);
        ok_toNumbr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(),InventoryPending.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();
    }

    public void invalidItemDialog() {
        final Dialog dialog = new Dialog(RequisitionControlDetails.this);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.invalid_item_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        AppCompatButton submitBtn = dialog.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                scanQR.setText("");
            }
        });

        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), InventoryPending.class);
        startActivity(intent);
        finish();
    }

    public void qrRCDDetails(){
        final ProgressDialog progressDialog = new ProgressDialog(RequisitionControlDetails.this);
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        progressDialog.setCancelable(false); // set cancelable to false
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);

        RequestBodyQRDetails requestBodyQRDetails = new RequestBodyQRDetails(qrDetails);

        Call<ResponseBodyQRDetails> call = apiInterface.QRWiseData(requestBodyQRDetails);
        call.enqueue(new Callback<ResponseBodyQRDetails>() {
            @Override
            public void onResponse(Call<ResponseBodyQRDetails> call, Response<ResponseBodyQRDetails> response) {
                progressDialog.dismiss();
                try{
                    if (response.body().getStatus().equals("Success")) {
                        String itemID = response.body().getItemId();
                        for(int i =0;i<=listRCDDetailsList.size();i++){
                            String idItem = listRCDDetailsList.get(i).getItemId();
                            if(itemID.equals(idItem)){
                                String qty = response.body().getStickerQty();

                                listRCDDetailsList.get(i).setConfig(response.body().getConfig());
                                String[] expdate = response.body().getExpdate().split("\\s+");
                                listRCDDetailsList.get(i).setExpdate(expdate[0]);
                                listRCDDetailsList.get(i).setBatchId(response.body().getBatchId());
                                adapter.notifyDataSetChanged();
                                if(listRCDDetailsList.get(i).getpickededQty()==null){
                                    listRCDDetailsList.get(i).setpickededQty(qty);
                                    if(qty.equals(listRCDDetailsList.get(i).getApprovedQty())){
                                        listRCDDetailsList.get(i).setremainingQty("0");
                                    }else{
                                        String appQty = listRCDDetailsList.get(i).getApprovedQty();
                                        float remqty = Float.parseFloat(appQty)-Float.parseFloat(qty);
                                        if(remqty<0){
                                            listRCDDetailsList.get(i).setpickededQty("");
                                            listRCDDetailsList.get(i).setremainingQty(valueRem);
                                            adapter.notifyDataSetChanged();
                                            scanQR.setText("");
                                            invalidItemDialog();
                                            break;
                                        }
                                        listRCDDetailsList.get(i).setremainingQty(String.valueOf(remqty));
                                        valueRem = String.valueOf(remqty);
                                    }
                                    adapter.notifyDataSetChanged();
                                    scanQR.setText("");
                                }else{
                                    String picqty = listRCDDetailsList.get(i).getpickededQty();
                                    float totqty = Float.parseFloat(picqty)+Float.parseFloat(qty);
                                    String totalQty = String.valueOf(totqty);
                                    String previousPickedQty = listRCDDetailsList.get(i).getpickededQty();
                                    listRCDDetailsList.get(i).setpickededQty(String.valueOf(totalQty));
                                    if(totalQty.equals(listRCDDetailsList.get(i).getApprovedQty())){
                                        listRCDDetailsList.get(i).setremainingQty("0");
                                    }else{
                                        String appQty = listRCDDetailsList.get(i).getApprovedQty();
                                        float remqty = Float.parseFloat(appQty)-Float.parseFloat(totalQty);
                                        if(remqty<0){
                                            listRCDDetailsList.get(i).setpickededQty(previousPickedQty);
                                            if(valueElseRem==""){}else{}
                                            listRCDDetailsList.get(i).setremainingQty(valueElseRem);
                                            adapter.notifyDataSetChanged();
                                            scanQR.setText("");
                                            invalidItemDialog();
                                            break;
                                        }
                                        listRCDDetailsList.get(i).setremainingQty(String.valueOf(remqty));
                                        valueElseRem = String.valueOf(remqty);
                                    }
                                    adapter.notifyDataSetChanged();
                                    scanQR.setText("");
                                }
                            }else{
                                invalidItemDialog();
                            }
                        }
                    }else{
                        invalidItemDialog();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBodyQRDetails> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RequisitionControlDetails.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void shipping(){
        try {
            for (int i = 0; i <= listRCDDetailsList.size(); i++) {
                float approv = Float.parseFloat(listRCDDetailsList.get(i).getApprovedQty());
                float picked = Float.parseFloat(listRCDDetailsList.get(i).getpickededQty());
                float remaining = Float.parseFloat(listRCDDetailsList.get(i).getremainingQty());
                float totalQty = (picked) + (remaining);
                forShipping = new ArrayList<detailDataList>();
                if (approv==(picked)) {
                    forShipping.add(listRCDDetailsList.get(i));
                    if (listRCDDetailsList.size() == i+1) {
//                        Toast.makeText(this, "Now run Service", Toast.LENGTH_SHORT).show();
                        serviceRunShip();
                    }
                } else if (approv==(totalQty)) {
                    if (listRCDDetailsList.get(i).getReason().equals("Select Reason")) {
                        String ItemNo = listRCDDetailsList.get(i).getItemId();
                        Toast.makeText(this, "Please select reason of Item ID: " + ItemNo, Toast.LENGTH_SHORT).show();
                        forShipping.clear();
                        break;
                    } else {
                        forShipping.add(listRCDDetailsList.get(i));
                        if (listRCDDetailsList.size() == i+1) {
                            serviceRunShip();
//                            Toast.makeText(this, "Now run Service Pending+Remaining", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void serviceRunShip(){
        final ProgressDialog progressDialog = new ProgressDialog(RequisitionControlDetails.this);
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        progressDialog.setCancelable(false); // set cancelable to false
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);

        String finalLoc = "";
        String[] loc = forShipping.get(0).getItemId().split("-");
        if(loc[0].equals("RM")){
            finalLoc = "MWH";
        }else{
            finalLoc = "CK";
        }
        String userId = Preference.getInstance(getApplicationContext()).getUserId();

//        List<RequestShipList> requestShipLists = new ArrayList<RequestShipList>();
//        for(int i = 0; i <= forShipping.size();i++){
//            requestShipLists.get(i).setItemId(forShipping.get(i).getItemId());
//            requestShipLists.get(i).setScanqty(forShipping.get(i).getpickededQty());
//            requestShipLists.get(i).setConfig(forShipping.get(i).getConfig());
//            requestShipLists.get(i).setBatchId(forShipping.get(i).getBatchId());
//            requestShipLists.get(i).setRemainingQty(forShipping.get(i).getremainingQty());
//            requestShipLists.get(i).setReason(forShipping.get(i).getReason());
//        }

        Gson gson = new Gson();
        String arrayListToJson = gson.toJson(forShipping);

        RequestBodyShipDetails requestBodyShipDetails = new RequestBodyShipDetails(reqNmbr, finalLoc, locationId, userId, arrayListToJson);
        Call<ResponseShippingDetails> call = apiInterface.redShipping(requestBodyShipDetails);
        call.enqueue(new Callback<ResponseShippingDetails>() {
            @Override
            public void onResponse(Call<ResponseShippingDetails> call, Response<ResponseShippingDetails> response) {
                progressDialog.dismiss();
                if (response.body().getStatus().equals("success")) {
                    transferOrderDialog(response.body().getTONumber());
//                    Toast.makeText(RequisitionControlDetails.this, "Successfully Shipped", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RequisitionControlDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseShippingDetails> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RequisitionControlDetails.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
            }
        });


    }



}