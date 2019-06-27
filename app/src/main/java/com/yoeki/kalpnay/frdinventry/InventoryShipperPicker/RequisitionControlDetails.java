package com.yoeki.kalpnay.frdinventry.InventoryShipperPicker;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yoeki.kalpnay.frdinventry.Api.Api;
import com.yoeki.kalpnay.frdinventry.Api.ApiInterface;
import com.yoeki.kalpnay.frdinventry.Api.Preference;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.InventoryPendingModel;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.ParticularRequisitionDetails;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.ResponseShippingDetails;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.detailDataList;
import com.yoeki.kalpnay.frdinventry.Items.ItemsModel;
import com.yoeki.kalpnay.frdinventry.Items.requestControlDetailList;
import com.yoeki.kalpnay.frdinventry.QRDetails.RequestBodyQRDetails;
import com.yoeki.kalpnay.frdinventry.QRDetails.ResponseBodyQRDetails;
import com.yoeki.kalpnay.frdinventry.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequisitionControlDetails extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    RecyclerView rcy_items;
    AppCompatTextView reqNo, rcd_Date, branchName_RCD;
    ArrayList<ItemsModel> arraylist;
    AppCompatButton shipBtn, languageChangeRCD, img_back;
    CheckBox checkUpdateQty;
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
        checkUpdateQty.setOnCheckedChangeListener(this);

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
                        Toast.makeText(RequisitionControlDetails.this, "QR is already Scanned.", Toast.LENGTH_SHORT).show();
                        scanQR.setText("");
//                        invalidItemDialog();
                    } else {
                        sequenceQRNumber.add(qrDetails);
                        if(checkUpdateQty.isChecked()){
                            qrRCDDetailsWithCheckBox();
                        }else{
                            qrRCDDetailsWithoutCheckBox();
                        }
                    }
                }
            }
        };
        scanQR.addTextChangedListener(textWatcher);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void qrRCDDetailsWithoutCheckBox(){
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
                try {
                    if (response.body().getStatus().equals("Success")) {

                        Boolean itemMatchedOrNot = false;
                        String responseItemID = response.body().getItemId();
                        for (int position = 0; position <= listRCDDetailsList.size() - 1; position++) {
                            String listRCDItemid = listRCDDetailsList.get(position).getItemId();
                            if(responseItemID.equals(listRCDItemid)) {
                                itemMatchedOrNot = true;
                                String qrDataStickerqty = response.body().getStickerQty();

                                if(listRCDDetailsList.get(position).getpickededQty()==null){
                                    if(listRCDDetailsList.get(position).getApprovedQty().equals(qrDataStickerqty)){
                                        listRCDDetailsList.get(position).setremainingQty("0");
                                        listRCDDetailsList.get(position).setpickededQty(qrDataStickerqty);
                                        detailDataList tempRcdDetail = new detailDataList();
                                        tempRcdDetail = listRCDDetailsList.get(position);
                                        listRCDDetailsList.remove(position);
                                        adapter.notifyItemRemoved(position);
                                        listRCDDetailsList.add(0, tempRcdDetail);
                                        adapter.notifyItemInserted(0);
                                        rcy_items.smoothScrollToPosition(0);
                                        scanQR.setText("");
                                    }else {
                                        float appqty = Float.parseFloat(listRCDDetailsList.get(position).getApprovedQty());
                                        float qrDataQty = Float.parseFloat(qrDataStickerqty);
                                        float remQty = appqty - qrDataQty;
                                        if (remQty >= 0) {
                                            listRCDDetailsList.get(position).setremainingQty(String.valueOf(remQty));
                                            listRCDDetailsList.get(position).setpickededQty(qrDataStickerqty);
                                            detailDataList tempRcdDetail = new detailDataList();
                                            tempRcdDetail = listRCDDetailsList.get(position);
                                            listRCDDetailsList.remove(position);
                                            adapter.notifyItemRemoved(position);
                                            listRCDDetailsList.add(0, tempRcdDetail);
                                            adapter.notifyItemInserted(0);
                                            rcy_items.smoothScrollToPosition(0);
                                            scanQR.setText("");
                                        } else {
                                            // check out later for run at the end or here
                                            Toast.makeText(RequisitionControlDetails.this, "Picked Qty. is not more then Approved Qty.", Toast.LENGTH_SHORT).show();
                                            scanQR.setText("");
                                        }
                                    }
                                }else{
                                    float prePickedQty = Float.parseFloat(listRCDDetailsList.get(position).getpickededQty());
                                    float QRDataStickerqty = Float.parseFloat(qrDataStickerqty);
                                    float totalpickedQty = prePickedQty+QRDataStickerqty;

                                    if(listRCDDetailsList.get(position).getApprovedQty().equals(String.valueOf(totalpickedQty))){
                                        listRCDDetailsList.get(position).setremainingQty("0");
                                        listRCDDetailsList.get(position).setpickededQty(String.valueOf(totalpickedQty));
                                        detailDataList tempRcdDetail = new detailDataList();
                                        tempRcdDetail = listRCDDetailsList.get(position);
                                        listRCDDetailsList.remove(position);
                                        adapter.notifyItemRemoved(position);
                                        listRCDDetailsList.add(0, tempRcdDetail);
                                        adapter.notifyItemInserted(0);
                                        rcy_items.smoothScrollToPosition(0);
                                        scanQR.setText("");
                                    }else{
                                        float remqty = Float.parseFloat(listRCDDetailsList.get(position).getApprovedQty())-totalpickedQty;
                                        if(remqty>=0){
                                            listRCDDetailsList.get(position).setremainingQty(String.valueOf(remqty));
                                            listRCDDetailsList.get(position).setpickededQty(String.valueOf(totalpickedQty));
                                            detailDataList tempRcdDetail = new detailDataList();
                                            tempRcdDetail = listRCDDetailsList.get(position);
                                            listRCDDetailsList.remove(position);
                                            adapter.notifyItemRemoved(position);
                                            listRCDDetailsList.add(0, tempRcdDetail);
                                            adapter.notifyItemInserted(0);
                                            rcy_items.smoothScrollToPosition(0);
                                            scanQR.setText("");
                                        }else{
                                            Toast.makeText(RequisitionControlDetails.this, "Picked Qty. is not more then Approved Qty.", Toast.LENGTH_SHORT).show();
                                            scanQR.setText("");
                                        }
                                    }
                                }
                            }
                        }if (itemMatchedOrNot == false) {
                            Toast.makeText(RequisitionControlDetails.this, "QR is not matched", Toast.LENGTH_SHORT).show();
                            scanQR.setText("");
                        }

                    }else{
                        Toast.makeText(RequisitionControlDetails.this, "QR is not available", Toast.LENGTH_SHORT).show();
                        scanQR.setText("");
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBodyQRDetails> call, Throwable t) {
                progressDialog.dismiss();
                scanQR.setText("");
                Toast.makeText(RequisitionControlDetails.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void qrRCDDetailsWithCheckBox(){
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
                try {
                    if (response.body().getStatus().equals("Success")){
                        Boolean itemMatchedOrNot = false;
                        String responseItemID = response.body().getItemId();
                        for (int position = 0; position <= listRCDDetailsList.size() - 1; position++) {
                            String listRCDItemid = listRCDDetailsList.get(position).getItemId();
                            if(responseItemID.equals(listRCDItemid)) {
                                itemMatchedOrNot = true;
                                String qrDataWiseStickerQty = response.body().getStickerQty();


                                update_Qty(qrDataWiseStickerQty,position);


                            }
                        }if (itemMatchedOrNot == false) {
                            Toast.makeText(RequisitionControlDetails.this, "QR is not matched", Toast.LENGTH_SHORT).show();
                            scanQR.setText("");
                        }




                    }else{
                        Toast.makeText(RequisitionControlDetails.this, "QR is not available", Toast.LENGTH_SHORT).show();
                        scanQR.setText("");
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBodyQRDetails> call, Throwable t) {
                progressDialog.dismiss();
                scanQR.setText("");
                Toast.makeText(RequisitionControlDetails.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
            }
        });
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
        checkUpdateQty = findViewById(R.id.checkUpdateQty);
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

    public void shipping(){
        try {
            for (int i = 0; i <= listRCDDetailsList.size(); i++) {
                float approv = Float.parseFloat(listRCDDetailsList.get(i).getApprovedQty());
                if(listRCDDetailsList.get(i).getpickededQty()!=null){
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
                }else{
                    Toast.makeText(this, "Firstly, Scan all items.", Toast.LENGTH_SHORT).show();
                    forShipping.clear();
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

    public void update_Qty(String pckQty, final int position) {
        final Dialog dialog = new Dialog(RequisitionControlDetails.this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.update_qty);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        final AppCompatAutoCompleteTextView pckd_qty = dialog.findViewById(R.id.pckd_qty);
        final AppCompatAutoCompleteTextView updte_qty = dialog.findViewById(R.id.updte_qty);
        pckd_qty.setText(pckQty);
        AppCompatButton submitBtn = dialog.findViewById(R.id.submitBtn);
        AppCompatButton cncld = dialog.findViewById(R.id.cncld);

        cncld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i<=sequenceQRNumber.size() - 1;i++){
                    String qrNUmber = sequenceQRNumber.get(i);
                    if(qrNUmber.equals(qrDetails)){
                        sequenceQRNumber.remove(i);
                    }
                }
                dialog.dismiss();
                scanQR.setText("");
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float packed = Float.parseFloat(pckd_qty.getText().toString());
                float update = Float.parseFloat(updte_qty.getText().toString());
               if (update==0) {
                    Toast.makeText(RequisitionControlDetails.this, "Please Input Update Quantity.", Toast.LENGTH_SHORT).show();
                } else {


                    float UpdateQty = packed * update;
                    float approvQty = Float.parseFloat(listRCDDetailsList.get(position).getApprovedQty());


                    if(listRCDDetailsList.get(position).getpickededQty()==null){
                        if(UpdateQty==approvQty){
                            listRCDDetailsList.get(position).setremainingQty("0");
                            listRCDDetailsList.get(position).setpickededQty(String.valueOf(UpdateQty));
                            detailDataList tempRcdDetail = new detailDataList();
                            tempRcdDetail = listRCDDetailsList.get(position);
                            listRCDDetailsList.remove(position);
                            adapter.notifyItemRemoved(position);
                            listRCDDetailsList.add(0, tempRcdDetail);
                            adapter.notifyItemInserted(0);
                            rcy_items.smoothScrollToPosition(0);
                            scanQR.setText("");

                        }else if(UpdateQty < approvQty){
                            float remaining = approvQty-UpdateQty;
                            listRCDDetailsList.get(position).setremainingQty(String.valueOf(remaining));
                            listRCDDetailsList.get(position).setpickededQty(String.valueOf(UpdateQty));
                            detailDataList tempRcdDetail = new detailDataList();
                            tempRcdDetail = listRCDDetailsList.get(position);
                            listRCDDetailsList.remove(position);
                            adapter.notifyItemRemoved(position);
                            listRCDDetailsList.add(0, tempRcdDetail);
                            adapter.notifyItemInserted(0);
                            rcy_items.smoothScrollToPosition(0);
                            scanQR.setText("");
                        }else{
                            for(int i = 0; i<=sequenceQRNumber.size() - 1;i++){
                                String qrNUmber = sequenceQRNumber.get(i);
                                if(qrNUmber.equals(qrDetails)){
                                    sequenceQRNumber.remove(i);
                                }
                            }
                            Toast.makeText(RequisitionControlDetails.this, "Picked Qty not greater then Approved Qty.", Toast.LENGTH_SHORT).show();
                            scanQR.setText("");
                        }
                    }else{

                        float prePackedQty = Float.parseFloat(listRCDDetailsList.get(position).getpickededQty());
                        float finalUpdateQty = UpdateQty+prePackedQty;

                        if(finalUpdateQty==approvQty){
                            listRCDDetailsList.get(position).setremainingQty("0");
                            listRCDDetailsList.get(position).setpickededQty(String.valueOf(finalUpdateQty));
                            detailDataList tempRcdDetail = new detailDataList();
                            tempRcdDetail = listRCDDetailsList.get(position);
                            listRCDDetailsList.remove(position);
                            adapter.notifyItemRemoved(position);
                            listRCDDetailsList.add(0, tempRcdDetail);
                            adapter.notifyItemInserted(0);
                            rcy_items.smoothScrollToPosition(0);
                            scanQR.setText("");

                        }else if(finalUpdateQty < approvQty){
                            float remaining = approvQty-UpdateQty;
                            listRCDDetailsList.get(position).setremainingQty(String.valueOf(remaining));
                            listRCDDetailsList.get(position).setpickededQty(String.valueOf(finalUpdateQty));
                            detailDataList tempRcdDetail = new detailDataList();
                            tempRcdDetail = listRCDDetailsList.get(position);
                            listRCDDetailsList.remove(position);
                            adapter.notifyItemRemoved(position);
                            listRCDDetailsList.add(0, tempRcdDetail);
                            adapter.notifyItemInserted(0);
                            rcy_items.smoothScrollToPosition(0);
                            scanQR.setText("");
                        }else{
                            for(int i = 0; i<=sequenceQRNumber.size() - 1;i++){
                                String qrNUmber = sequenceQRNumber.get(i);
                                if(qrNUmber.equals(qrDetails)){
                                    sequenceQRNumber.remove(i);
                                }
                            }
                            Toast.makeText(RequisitionControlDetails.this, "Picked Qty not greater then Approved Qty.", Toast.LENGTH_SHORT).show();
                            scanQR.setText("");
                        }

                    }
                    dialog.dismiss();
                }
            }
        });
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }




    //It's not use anywhere
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
                        String responseItemID = response.body().getItemId();
                        for(int position = 0; position <=listRCDDetailsList.size()-1; position++){
                            String idItem = listRCDDetailsList.get(position).getItemId();
                            if(responseItemID.equals(idItem)){
                                String qty = response.body().getStickerQty();
//                                listRCDDetailsList.get(position).setConfig(response.body().getConfig());
//                                String[] expdate = response.body().getExpdate().split("\\s+");
//                                listRCDDetailsList.get(position).setExpdate(expdate[0]);
//                                listRCDDetailsList.get(position).setBatchId(response.body().getBatchId());

                                if(listRCDDetailsList.get(position).getpickededQty()==null){
                                    // For setting the number on picked Qty
//                                    listRCDDetailsList.get(position).setpickededQty(qty);

                                    if(qty.equals(listRCDDetailsList.get(position).getApprovedQty())){
                                        listRCDDetailsList.get(position).setremainingQty("0");

                                        detailDataList tempRcdDetail = new detailDataList();
                                        tempRcdDetail = listRCDDetailsList.get(position);
                                        listRCDDetailsList.remove(position);
                                        adapter.notifyItemRemoved(position);
                                        listRCDDetailsList.add(0, tempRcdDetail);
                                        adapter.notifyItemInserted(0);
                                        rcy_items.smoothScrollToPosition(0);

                                        scanQR.setText("");

                                    }else{
                                        String appQty = listRCDDetailsList.get(position).getApprovedQty();
                                        float remqty = Float.parseFloat(appQty)-Float.parseFloat(qty);
                                        //
                                        if(remqty>0){
//                                            listRCDDetailsList.get(position).setpickededQty("");
                                            listRCDDetailsList.get(position).setremainingQty(valueRem);

                                            detailDataList tempRcdDetail = new detailDataList();
                                            tempRcdDetail = listRCDDetailsList.get(position);
                                            listRCDDetailsList.remove(position);
                                            adapter.notifyItemRemoved(position);
                                            listRCDDetailsList.add(0, tempRcdDetail);
                                            adapter.notifyItemInserted(0);
                                            rcy_items.smoothScrollToPosition(0);

                                            scanQR.setText("");
//                                            invalidItemDialog();
                                            break;
                                        }
                                        listRCDDetailsList.get(position).setremainingQty(String.valueOf(remqty));
                                        valueRem = String.valueOf(remqty);
                                    }

                                    if(checkUpdateQty.isChecked()){
                                        String pckQty = response.body().getStickerQty();
                                        update_Qty(pckQty, position);
                                    }

                                }else{
                                    String picqty = listRCDDetailsList.get(position).getpickededQty();
                                    float totqty = Float.parseFloat(picqty)+Float.parseFloat(qty);
                                    String totalQty = String.valueOf(totqty);
                                    String previousPickedQty = listRCDDetailsList.get(position).getpickededQty();
                                    listRCDDetailsList.get(position).setpickededQty(String.valueOf(totalQty));
                                    if(totalQty.equals(listRCDDetailsList.get(position).getApprovedQty())){
                                        listRCDDetailsList.get(position).setremainingQty("0");
                                        detailDataList tempRcdDetail = new detailDataList();
                                        tempRcdDetail = listRCDDetailsList.get(position);
                                        listRCDDetailsList.remove(position);
                                        adapter.notifyItemRemoved(position);
                                        listRCDDetailsList.add(0, tempRcdDetail);
                                        adapter.notifyItemInserted(0);
                                        rcy_items.smoothScrollToPosition(0);

                                        scanQR.setText("");

                                    }else{
                                        String appQty = listRCDDetailsList.get(position).getApprovedQty();
                                        float remqty = Float.parseFloat(appQty)-Float.parseFloat(totalQty);
                                        if(remqty<0){
                                            listRCDDetailsList.get(position).setpickededQty(previousPickedQty);
                                            if(valueElseRem==""){}else{}
                                            listRCDDetailsList.get(position).setremainingQty(valueElseRem);

                                            detailDataList tempRcdDetail = new detailDataList();
                                            tempRcdDetail = listRCDDetailsList.get(position);
                                            listRCDDetailsList.remove(position);
                                            adapter.notifyItemRemoved(position);
                                            listRCDDetailsList.add(0, tempRcdDetail);
                                            adapter.notifyItemInserted(0);
                                            rcy_items.smoothScrollToPosition(0);

                                            scanQR.setText("");
                                            invalidItemDialog();
                                            break;
                                        }
                                        listRCDDetailsList.get(position).setremainingQty(String.valueOf(remqty));
                                        valueElseRem = String.valueOf(remqty);
                                    }

                                    if(checkUpdateQty.isChecked()){
                                        String pckQty = response.body().getStickerQty();
                                        update_Qty(pckQty, position);
                                    }
                                }
                            }
                        }
                        String qr = scanQR.getText().toString();
                        if(!qr.equals("")){
                            invalidItemDialog();
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
}