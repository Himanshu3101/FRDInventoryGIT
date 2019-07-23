package com.yoeki.kalpnay.frdinventry.banchInventoryReceiving;

import com.yoeki.kalpnay.frdinventry.Api.Api;
import com.yoeki.kalpnay.frdinventry.Api.ApiInterface;
import com.yoeki.kalpnay.frdinventry.Api.Preference;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.BatchListReciver;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.BatchNoList;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.InventoryPendingModel;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.ParticularRequisitionDetails;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.StickerList;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.commonReceivingShippingDetailDataList;
import com.yoeki.kalpnay.frdinventry.Items.commonReceivingShippingDetailList;
import com.yoeki.kalpnay.frdinventry.MRN.Model.PostingJsonResponse;
import com.yoeki.kalpnay.frdinventry.QRDetails.RequestBodyQRDetails;
import com.yoeki.kalpnay.frdinventry.QRDetails.ResponseBodyQRDetails;
import com.yoeki.kalpnay.frdinventry.R;
import com.yoeki.kalpnay.frdinventry.banchInventoryReceiving.model.SequenceQuanitiy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventoryControlDetail extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerViewItems;
    AppCompatAutoCompleteTextView scanQR;
    CheckBox checkUpdateQty;
    AppCompatTextView requisitionNo, date, receivedFromBranch;
    AppCompatButton receivedBtn, languageChange, backInventoryControl;
    String reqNmbr = "", wareHouse, locationId, qrDetails;
    int sequenceAddorNot = 0;
    commonReceivingShippingDetailList adapter;
    int languageChangeVisible = 1;
    ArrayList<StickerList> sequenceQRNumber = new ArrayList<>();
    List<commonReceivingShippingDetailDataList> commonReceivingShippingDetailDataLists;
    ArrayList<commonReceivingShippingDetailDataList> forReceiving;
    List<SequenceQuanitiy> sequenceQuantityLists = new ArrayList<>();
    String QRSequenceNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_control_detail);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initUi();
        reqNmbr = getIntent().getStringExtra("RequisitionNo");
        wareHouse = getIntent().getStringExtra("wareHouse");
        locationId = getIntent().getStringExtra("locationCode");
        requisitionNo.setText(reqNmbr);
        addlistdata();

        backInventoryControl.setOnClickListener(this);
        receivedBtn.setOnClickListener(this);
        languageChange.setOnClickListener(this);

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

                    for (int i = 0; i <= sequenceQRNumber.size() - 1; i++) {
                        String qrNumber = sequenceQRNumber.get(i).getStickerSeq();
                        if (qrNumber.equals(qrDetails)) {
                            QRSequenceNo = qrDetails;
                            qrRCDDetailsWithoutCheckBox();



                            sequenceQRNumber.remove(i);
                            scanQR.setText("");
                            sequenceAddorNot = 1;
                        }
                    }

                    if (sequenceAddorNot == 0) {
                        Toast.makeText(InventoryControlDetail.this, "QR is not Valid.", Toast.LENGTH_SHORT).show();
                        scanQR.setText("");
                    }
                }
            }
        };
        scanQR.addTextChangedListener(textWatcher);
    }

    public void initUi() {
        requisitionNo = findViewById(R.id.requisitionNo);
        date = findViewById(R.id.date);
        receivedFromBranch = findViewById(R.id.receivedFromBranch);
        scanQR = findViewById(R.id.scanQR);
        receivedBtn = findViewById(R.id.receivedBtn);
        backInventoryControl = findViewById(R.id.backInventoryControl);
        languageChange = findViewById(R.id.languageChange);
        recyclerViewItems = findViewById(R.id.recyclerViewItems);
        checkUpdateQty = findViewById(R.id.checkUpdateQty);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backInventoryControl:
                startActivity(new Intent(getApplicationContext(), CompletedReceiving.class));
                finish();
                break;
            case R.id.receivedBtn:
                receiving();
                break;
            case R.id.languageChange:
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                adapter = new commonReceivingShippingDetailList(InventoryControlDetail.this, commonReceivingShippingDetailDataLists, languageChangeVisible);
                recyclerViewItems.setLayoutManager(layoutManager);
                recyclerViewItems.setAdapter(adapter);
                if (languageChangeVisible == 0) {
                    languageChangeVisible = 1;
                } else {
                    languageChangeVisible = 0;
                }
                break;
        }
    }

    public void receiving() {
        try {
            int scanall =0;
            int forrecevingrun = 0;
            int forReason;
            forReceiving = new ArrayList<commonReceivingShippingDetailDataList>();
            for (int i = 0; i <= commonReceivingShippingDetailDataLists.size() - 1; i++) {
                float shipped = Float.parseFloat(commonReceivingShippingDetailDataLists.get(i).getShippedQty());
                if (commonReceivingShippingDetailDataLists.get(i).getpickededQty() != null) {
                    if (commonReceivingShippingDetailDataLists.get(i).getpickededQty() != "0") {
                        float picked = Float.parseFloat(commonReceivingShippingDetailDataLists.get(i).getpickededQty());
                        float remaining = Float.parseFloat(commonReceivingShippingDetailDataLists.get(i).getremainingQty());
                        float totalQty = (picked) + (remaining);
                        if (shipped == (picked)) {


                            forrecevingrun++;
                            forReceiving.add(commonReceivingShippingDetailDataLists.get(i));
                            if (commonReceivingShippingDetailDataLists.size() == forrecevingrun) {
                                serviceRunReceived();
                            }

                            //For return Work

                            /*forReason = 0;
                            for (int j = 0; j < commonReceivingShippingDetailDataLists.get(i).getBatchListReceiver().size(); j++) {
                                if (commonReceivingShippingDetailDataLists.get(i).getBatchListReceiver().get(j).getReturnQty() != null) {
                                    if (!commonReceivingShippingDetailDataLists.get(i).getBatchListReceiver().get(j).getReturnQty().equals("0")) {
                                        if (commonReceivingShippingDetailDataLists.get(i).getReason().equals("")) {
                                            Toast.makeText(this, "Please select reason of Item ID: " + commonReceivingShippingDetailDataLists.get(i).getItemId(), Toast.LENGTH_SHORT).show();
                                            break;
                                        } else if (commonReceivingShippingDetailDataLists.get(i).getReason().equals("Select Reason")) {
                                            Toast.makeText(this, "Please select reason of Item ID: " + commonReceivingShippingDetailDataLists.get(i).getItemId(), Toast.LENGTH_SHORT).show();
                                            break;
                                        } else {
                                            forReason = 1;
                                        }
                                    }
                                } else {
                                    commonReceivingShippingDetailDataLists.get(i).setReason("");
                                    commonReceivingShippingDetailDataLists.get(i).getBatchListReceiver().get(j).setReturnQty("0");
                                }
                            }
                            if (forReason == 1) {
                                forReceiving.add(commonReceivingShippingDetailDataLists.get(i));
                                if (commonReceivingShippingDetailDataLists.size() == i + 1) {
                                    serviceRunReceived();
                                }
                            } else {
                                forReceiving.add(commonReceivingShippingDetailDataLists.get(i));
                                if (commonReceivingShippingDetailDataLists.get(i).getReason().equals("Select Reason")) {
                                    commonReceivingShippingDetailDataLists.get(i).setReason("");
                                    break;
                                } else if (commonReceivingShippingDetailDataLists.get(i).getReason() == null) {
                                    commonReceivingShippingDetailDataLists.get(i).setReason("");
                                    break;
                                }
                                if (commonReceivingShippingDetailDataLists.size() == i + 1) {
                                    serviceRunReceived();
                                }
                            }*/
                            //For return Work




                        }else{
                            Toast.makeText(this, "Please Scan all items.", Toast.LENGTH_SHORT).show();
                            scanall = 1;
                        }

                        //For return Work
                        /*else if (shipped == (totalQty)) {
                            forReason = 0;
                            if (commonReceivingShippingDetailDataLists.get(i).getReason().equals("Select Reason")) {
                                String ItemNo = commonReceivingShippingDetailDataLists.get(i).getItemId();
                                Toast.makeText(this, "Please select reason of Item ID: " + ItemNo, Toast.LENGTH_SHORT).show();
                                forReceiving.clear();
                                break;
                            } else {
                                for (int j = 0; j < commonReceivingShippingDetailDataLists.get(i).getBatchListReceiver().size(); j++) {
                                    if (!commonReceivingShippingDetailDataLists.get(i).getBatchListReceiver().get(j).getReturnQty().equals("0")) {
                                        if (commonReceivingShippingDetailDataLists.get(i).getReason().equals("Select Reason")) {
                                            Toast.makeText(this, "Please select reason of Item ID: " + commonReceivingShippingDetailDataLists.get(i).getItemId(), Toast.LENGTH_SHORT).show();
                                            break;
                                        } else if (commonReceivingShippingDetailDataLists.get(i).getReason().equals("")) {
                                            Toast.makeText(this, "Please select reason of Item ID: " + commonReceivingShippingDetailDataLists.get(i).getItemId(), Toast.LENGTH_SHORT).show();
                                            break;
                                        } else {
                                            forReason = 1;
                                        }
                                    }
                                }
                                if (forReason == 1) {
                                    forReceiving.add(commonReceivingShippingDetailDataLists.get(i));
                                    if (commonReceivingShippingDetailDataLists.size() == i + 1) {
                                        serviceRunReceived();
                                    }
                                } else {
                                    forReceiving.add(commonReceivingShippingDetailDataLists.get(i));
                                    if (commonReceivingShippingDetailDataLists.get(i).getReason().equals("Select Reason")) {
                                        commonReceivingShippingDetailDataLists.get(i).setReason("");
                                        break;
                                    } else if (commonReceivingShippingDetailDataLists.get(i).getReason() == "") {
                                        commonReceivingShippingDetailDataLists.get(i).setReason("");
                                        break;
                                    }
                                    if (commonReceivingShippingDetailDataLists.size() == i + 1) {
                                        serviceRunReceived();
                                    }
                                }
                            }
                        }*/
                        //For return Work


                    } else {
                        Toast.makeText(this, "Picked Qty not be zero of " + commonReceivingShippingDetailDataLists.get(i).getItemId(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(scanall!=1){
                        Toast.makeText(this, "Please Scan Item - " + commonReceivingShippingDetailDataLists.get(i).getItemId(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void serviceRunReceived() {
        final ProgressDialog progressDialog = new ProgressDialog(InventoryControlDetail.this);
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        progressDialog.setCancelable(false); // set cancelable to false
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);

        String userId = Preference.getInstance(getApplicationContext()).getUserId();

        RequestBodyReceiveDetails requestBodyShipDetails = new RequestBodyReceiveDetails(reqNmbr, userId, forReceiving);

        Call<PostingJsonResponse> call = apiInterface.forBranchInventoryReceiving(requestBodyShipDetails);
        call.enqueue(new Callback<PostingJsonResponse>() {
            @Override
            public void onResponse(Call<PostingJsonResponse> call, Response<PostingJsonResponse> response) {
                progressDialog.dismiss();
                if (response.body().getStatus().equals("success")) {
                    Toast.makeText(InventoryControlDetail.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), CompletedReceiving.class);
                    startActivity(intent);
                    finish();
                } else {
                    alertDialog(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<PostingJsonResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(InventoryControlDetail.this, "Time Out. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), CompletedReceiving.class));
        finish();
    }

    public void addlistdata() {
        final ProgressDialog progressDialog = new ProgressDialog(InventoryControlDetail.this);
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        progressDialog.setCancelable(false); // set cancelable to false
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);

        String userId = Preference.getInstance(getApplicationContext()).getUserId();

        ParticularRequisitionDetails userIDModeParticularRequisitionDetailsl = new ParticularRequisitionDetails(userId, reqNmbr);

        Call<InventoryPendingModel> call = apiInterface.inventoryComplete(userIDModeParticularRequisitionDetailsl);
        call.enqueue(new Callback<InventoryPendingModel>() {
            @Override
            public void onResponse(Call<InventoryPendingModel> call, Response<InventoryPendingModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.body().getStatus().equals("Success")) {

                        if (response.body().getDataList().size() != 0) {
                            commonReceivingShippingDetailDataLists = response.body().getDataList();
                            String[] Date = response.body().getDataList().get(0).getReqestDate().split("\\s+");
                            date.setText(Date[0]);
                            receivedFromBranch.setText(response.body().getDataList().get(0).getBranch());

                            sequenceQRNumber = response.body().getStickerList();
                            recyclerViewItems.setLayoutManager(new LinearLayoutManager(InventoryControlDetail.this, LinearLayoutManager.VERTICAL, false));
                            recyclerViewItems.setItemAnimator(new DefaultItemAnimator());

                            adapter = new commonReceivingShippingDetailList(InventoryControlDetail.this, commonReceivingShippingDetailDataLists, "BIReceiving");

                            recyclerViewItems.setAdapter(adapter);
                            recyclerViewItems.scheduleLayoutAnimation();
                        } else {
                            alertDialog("Data Not Available.");
//                            Toast.makeText(InventoryControlDetail.this, "Data Not Available.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        alertDialog(response.body().getMessage());
                    }
                } catch (Exception e) {
                    Toast.makeText(InventoryControlDetail.this, "Service Unavailable.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InventoryPendingModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(InventoryControlDetail.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void alertDialog(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(message);
        dialog.setTitle("Error");
        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    public void qrRCDDetailsWithoutCheckBox() {
        final ProgressDialog progressDialog = new ProgressDialog(InventoryControlDetail.this);
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


                        SequenceQuanitiy sequenceQuanitiy = new SequenceQuanitiy();
                        sequenceQuanitiy.setBatchNumber(response.body().getBatchId());
                        sequenceQuanitiy.setSequenceQty(response.body().getStickerQty());
                        sequenceQuantityLists.add(sequenceQuanitiy);
                        Preference.getInstance(getApplicationContext()).saveSequenceQuantity(sequenceQuantityLists);
//                        List<SequenceQuanitiy> reasonList = Preference.getInstance(getApplicationContext()).getSequenceQuantity();




                        sequenceAddorNot = 0;
                        Boolean itemMatchedOrNot = false;
                        String responseItemID = response.body().getItemId();

                        for (int position = 0; position <= commonReceivingShippingDetailDataLists.size() - 1; position++) {
                            String listRCDItemid = commonReceivingShippingDetailDataLists.get(position).getItemId();

                            if (responseItemID.equals(listRCDItemid)) {
                                itemMatchedOrNot = true;
                                String qrDataStickerqty = response.body().getStickerQty();

                                commonReceivingShippingDetailDataLists.get(position).setConfig(response.body().getConfig());
                                String[] expdate = response.body().getExpdate().split("\\s+");
                                commonReceivingShippingDetailDataLists.get(position).setExpdate(expdate[0]);
                                commonReceivingShippingDetailDataLists.get(position).setBatchId(response.body().getBatchId());

                                if (checkUpdateQty.isChecked()) {
                                    update_Qty(qrDataStickerqty, position);
                                } else {
                                    if (commonReceivingShippingDetailDataLists.get(position).getpickededQty() == null) {

                                        if (commonReceivingShippingDetailDataLists.get(position).getShippedQty().equals(qrDataStickerqty)) {

                                            commonReceivingShippingDetailDataLists.get(position).setremainingQty("0");
                                            commonReceivingShippingDetailDataLists.get(position).setpickededQty(qrDataStickerqty);
                                            commonReceivingShippingDetailDataLists.get(position).setReason("");
                                            commonReceivingShippingDetailDataLists.get(position).setUnitId(response.body().getUnitId());

                                            List<BatchListReciver> batchListRecivers = new ArrayList<>();
                                            String batchNo = commonReceivingShippingDetailDataLists.get(position).getBatchId();
                                            if (commonReceivingShippingDetailDataLists.get(position).getReturnQty() == null) {
                                                commonReceivingShippingDetailDataLists.get(position).setReturnQty("0");
                                            }
                                            String returnQty = commonReceivingShippingDetailDataLists.get(position).getReturnQty();
                                            String config = commonReceivingShippingDetailDataLists.get(position).getConfig();
                                            float stickerQty = Float.parseFloat(response.body().getStickerQty());
                                            if (commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver() == null) {
                                                BatchListReciver batchListReciver = new BatchListReciver();
                                                batchListReciver.setBatchNo(batchNo);
                                                batchListReciver.setReturnQty(returnQty);
                                                batchListReciver.setConfig(config);
                                                batchListReciver.setStickerQty(String.valueOf(stickerQty));
                                                batchListRecivers.add(batchListReciver);
                                                commonReceivingShippingDetailDataLists.get(position).setBatchListReceiver(batchListRecivers);
                                            }
                                            forRefreshList(position);
                                        } else {
                                            float shippqty = Float.parseFloat(commonReceivingShippingDetailDataLists.get(position).getShippedQty());
                                            float qrDataQty = Float.parseFloat(qrDataStickerqty);
                                            float remQty = shippqty - qrDataQty;
                                            String test = String.format("%.03f", remQty);

                                            commonReceivingShippingDetailDataLists.get(position).setUnitId(response.body().getUnitId());
                                            if (remQty >= 0) {
                                                commonReceivingShippingDetailDataLists.get(position).setremainingQty(test);
                                                commonReceivingShippingDetailDataLists.get(position).setpickededQty(qrDataStickerqty);

                                                if (remQty == 0) {
                                                    commonReceivingShippingDetailDataLists.get(position).setReason("");
                                                }

                                                List<BatchListReciver> batchListRecivers = new ArrayList<>();
                                                String batchNo = commonReceivingShippingDetailDataLists.get(position).getBatchId();
                                                if (commonReceivingShippingDetailDataLists.get(position).getReturnQty() == null) {
                                                    commonReceivingShippingDetailDataLists.get(position).setReturnQty("0");
                                                }
                                                String returnQty = commonReceivingShippingDetailDataLists.get(position).getReturnQty();
                                                String config = commonReceivingShippingDetailDataLists.get(position).getConfig();
                                                float stickerQty = Float.parseFloat(response.body().getStickerQty());

                                                if (commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver() == null) {
                                                    BatchListReciver batchListReciver = new BatchListReciver();
                                                    batchListReciver.setBatchNo(batchNo);
                                                    batchListReciver.setReturnQty(returnQty);
                                                    batchListReciver.setConfig(config);
                                                    batchListReciver.setStickerQty(String.valueOf(stickerQty));
                                                    batchListRecivers.add(batchListReciver);
                                                    commonReceivingShippingDetailDataLists.get(position).setBatchListReceiver(batchListRecivers);

                                                } else if (commonReceivingShippingDetailDataLists.get(position).getBatchNoList().size() == 0) {
                                                    BatchListReciver batchListReciver = new BatchListReciver();
                                                    batchListReciver.setBatchNo(batchNo);
                                                    batchListReciver.setReturnQty(returnQty);
                                                    batchListReciver.setConfig(config);
                                                    batchListReciver.setStickerQty(String.valueOf(stickerQty));
                                                    batchListRecivers.add(batchListReciver);
                                                    commonReceivingShippingDetailDataLists.get(position).setBatchListReceiver(batchListRecivers);
                                                } else {
                                                    boolean batchB = false;
                                                    boolean confB = false;
                                                    if (commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver() != null) {

                                                        for (int j = 0; j < commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().size(); j++) {
                                                            String batch = commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().get(j).getBatchNo();
                                                            if (batchNo.equals(batch)) {
                                                                batchB = true;
                                                                for (int k = 0; k < commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().size(); k++) {
                                                                    String configur = commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().get(k).getConfig();
                                                                    if (configur.equals(response.body().getConfig())) {
                                                                        confB = true;
                                                                        String confBatch = commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().get(k).getBatchNo();
                                                                        if (batch.equals(confBatch)) {
                                                                            float prevBatchQty = Float.parseFloat(commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().get(k).getStickerQty());
                                                                            float total = prevBatchQty + stickerQty;
                                                                            commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().get(k).setStickerQty(String.valueOf(total));
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        if (confB == false) {
                                                            BatchListReciver batchListReciver = new BatchListReciver();
                                                            batchListReciver.setBatchNo(batchNo);
                                                            batchListReciver.setReturnQty(returnQty);
                                                            batchListReciver.setConfig(config);
                                                            batchListReciver.setStickerQty(String.valueOf(stickerQty));
                                                            batchListRecivers.add(batchListReciver);
                                                            int size = commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().size();
                                                            commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().add(batchListReciver);
                                                            forRefreshList(position);
                                                            break;
                                                        }

                                                        if (batchB == false) {
                                                            BatchListReciver batchListReciver = new BatchListReciver();
                                                            batchListReciver.setBatchNo(batchNo);
                                                            batchListReciver.setReturnQty(returnQty);
                                                            batchListReciver.setConfig(config);
                                                            batchListReciver.setStickerQty(String.valueOf(stickerQty));
                                                            batchListRecivers.add(batchListReciver);
                                                            int size = commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().size();
                                                            commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().add(batchListReciver);
                                                            forRefreshList(position);
                                                            break;
                                                        }
                                                    }
                                                }

                                                forRefreshList(position);
                                            } else {
                                                Toast.makeText(InventoryControlDetail.this, "Picked Qty. is not more then Shipped Qty.", Toast.LENGTH_SHORT).show();
                                                sequenceQRNumber.remove(qrDetails);
                                                scanQR.setText("");
                                            }
                                        }
                                    } else {
                                        float prePickedQty = Float.parseFloat(commonReceivingShippingDetailDataLists.get(position).getpickededQty());
                                        float QRDataStickerqty = Float.parseFloat(qrDataStickerqty);
                                        float totalpickedQty = prePickedQty + QRDataStickerqty;
                                        float shippeddQty = Float.parseFloat(commonReceivingShippingDetailDataLists.get(position).getShippedQty());

                                        if (shippeddQty == totalpickedQty) {
                                            commonReceivingShippingDetailDataLists.get(position).setremainingQty("0");
                                            commonReceivingShippingDetailDataLists.get(position).setpickededQty(String.valueOf(totalpickedQty));
                                            commonReceivingShippingDetailDataLists.get(position).setReason("");
                                            commonReceivingShippingDetailDataLists.get(position).setUnitId(response.body().getUnitId());

                                            List<BatchListReciver> batchListRecivers = new ArrayList<>();
                                            String batchNo = commonReceivingShippingDetailDataLists.get(position).getBatchId();
                                            if (commonReceivingShippingDetailDataLists.get(position).getReturnQty() == null) {
                                                commonReceivingShippingDetailDataLists.get(position).setReturnQty("0");
                                            }
                                            String returnQty = commonReceivingShippingDetailDataLists.get(position).getReturnQty();
                                            String config = commonReceivingShippingDetailDataLists.get(position).getConfig();
                                            float stickerQty = Float.parseFloat(response.body().getStickerQty());
                                            if (commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver() == null) {
                                                BatchListReciver batchListReciver = new BatchListReciver();
                                                batchListReciver.setBatchNo(batchNo);
                                                batchListReciver.setReturnQty("0");
                                                batchListReciver.setConfig(config);
                                                batchListReciver.setStickerQty(String.valueOf(stickerQty));
                                                batchListRecivers.add(batchListReciver);
                                                commonReceivingShippingDetailDataLists.get(position).setBatchListReceiver(batchListRecivers);

                                            } else if (commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().size() == 0) {
                                                BatchListReciver batchListReciver = new BatchListReciver();
                                                batchListReciver.setBatchNo(batchNo);
                                                batchListReciver.setReturnQty("0");
                                                batchListReciver.setConfig(config);
                                                batchListReciver.setStickerQty(String.valueOf(stickerQty));
                                                batchListRecivers.add(batchListReciver);
                                                commonReceivingShippingDetailDataLists.get(position).setBatchListReceiver(batchListRecivers);

                                            } else {
                                                boolean batchB = false;
                                                boolean confB = false;
                                                if (commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver() != null) {

                                                    for (int j = 0; j < commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().size(); j++) {
                                                        String batch = commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().get(j).getBatchNo();
                                                        if (batchNo.equals(batch)) {
                                                            batchB = true;
                                                            for (int k = 0; k < commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().size(); k++) {
                                                                String configur = commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().get(k).getConfig();
                                                                if (configur.equals(response.body().getConfig())) {
                                                                    confB = true;
                                                                    String confBatch = commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().get(k).getBatchNo();
                                                                    if (batch.equals(confBatch)) {
                                                                        float prevBatchQty = Float.parseFloat(commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().get(k).getStickerQty());
                                                                        float total = prevBatchQty + stickerQty;
                                                                        commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().get(k).setStickerQty(String.valueOf(total));
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }

                                                    if (confB == false) {
                                                        BatchListReciver batchListReciver = new BatchListReciver();
                                                        batchListReciver.setBatchNo(batchNo);
                                                        batchListReciver.setReturnQty(returnQty);
                                                        batchListReciver.setConfig(config);
                                                        batchListReciver.setStickerQty(String.valueOf(stickerQty));
                                                        batchListRecivers.add(batchListReciver);
                                                        int size = commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().size();
                                                        commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().add(batchListReciver);
                                                        forRefreshList(position);
                                                        break;
                                                    }

                                                    if (batchB == false) {
                                                        BatchListReciver batchListReciver = new BatchListReciver();
                                                        batchListReciver.setBatchNo(batchNo);
                                                        batchListReciver.setReturnQty(returnQty);
                                                        batchListReciver.setConfig(config);
                                                        batchListReciver.setStickerQty(String.valueOf(stickerQty));
                                                        batchListRecivers.add(batchListReciver);
                                                        int size = commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().size();
                                                        commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().add(batchListReciver);
                                                        forRefreshList(position);
                                                        break;
                                                    }
                                                }
                                            }
                                            forRefreshList(position);
                                        } else {
                                            float remqty = Float.parseFloat(commonReceivingShippingDetailDataLists.get(position).getShippedQty()) - totalpickedQty;
                                            String test = String.format("%.03f", remqty);
                                            float stickQty = Float.parseFloat(response.body().getStickerQty());

                                            commonReceivingShippingDetailDataLists.get(position).setUnitId(response.body().getUnitId());
                                            if (remqty >= 0) {
                                                commonReceivingShippingDetailDataLists.get(position).setremainingQty(test);
                                                commonReceivingShippingDetailDataLists.get(position).setpickededQty(String.valueOf(totalpickedQty));

                                                if (remqty == 0.0) {
                                                    commonReceivingShippingDetailDataLists.get(position).setReason("");
                                                }

                                                List<BatchListReciver> batchListRecivers = new ArrayList<>();
                                                String batchNo = commonReceivingShippingDetailDataLists.get(position).getBatchId();
                                                if (commonReceivingShippingDetailDataLists.get(position).getReturnQty() == null) {
                                                    commonReceivingShippingDetailDataLists.get(position).setReturnQty("0");
                                                }
                                                String returnQty = commonReceivingShippingDetailDataLists.get(position).getReturnQty();
                                                String config = commonReceivingShippingDetailDataLists.get(position).getConfig();
                                                String stickerQty = response.body().getStickerQty();
                                                if (commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver() == null) {
                                                    BatchListReciver batchListReciver = new BatchListReciver();
                                                    batchListReciver.setBatchNo(batchNo);
                                                    batchListReciver.setReturnQty("0");
                                                    batchListReciver.setConfig(config);
                                                    batchListReciver.setStickerQty(stickerQty);
                                                    batchListRecivers.add(batchListReciver);
                                                    commonReceivingShippingDetailDataLists.get(position).setBatchListReceiver(batchListRecivers);

                                                } else if (commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().size() == 0) {
                                                    BatchListReciver batchListReciver = new BatchListReciver();
                                                    batchListReciver.setBatchNo(batchNo);
                                                    batchListReciver.setReturnQty("0");
                                                    batchListReciver.setConfig(config);
                                                    batchListReciver.setStickerQty(stickerQty);
                                                    batchListRecivers.add(batchListReciver);
                                                    commonReceivingShippingDetailDataLists.get(position).setBatchListReceiver(batchListRecivers);

                                                } else {
                                                    boolean batchB = false;
                                                    boolean confB = false;
                                                    if (commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver() != null) {

                                                        for (int j = 0; j < commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().size(); j++) {
                                                            String batch = commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().get(j).getBatchNo();
                                                            if (batchNo.equals(batch)) {
                                                                batchB = true;
                                                                for (int k = 0; k < commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().size(); k++) {
                                                                    String configur = commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().get(k).getConfig();
                                                                    if (configur.equals(response.body().getConfig())) {
                                                                        confB = true;
                                                                        String confBatch = commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().get(k).getBatchNo();
                                                                        if (batch.equals(confBatch)) {
                                                                            float prevBatchQty = Float.parseFloat(commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().get(k).getStickerQty());
                                                                            float total = Float.parseFloat(prevBatchQty + stickerQty);
                                                                            commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().get(k).setStickerQty(String.valueOf(total));
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        if (confB == false) {
                                                            BatchListReciver batchListReciver = new BatchListReciver();
                                                            batchListReciver.setBatchNo(batchNo);
                                                            batchListReciver.setReturnQty(returnQty);
                                                            batchListReciver.setConfig(config);
                                                            batchListReciver.setStickerQty(String.valueOf(stickerQty));
                                                            batchListRecivers.add(batchListReciver);
                                                            int size = commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().size();
                                                            commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().add(batchListReciver);
                                                            forRefreshList(position);
                                                            break;
                                                        }

                                                        if (batchB == false) {
                                                            BatchListReciver batchListReciver = new BatchListReciver();
                                                            batchListReciver.setBatchNo(batchNo);
                                                            batchListReciver.setReturnQty(returnQty);
                                                            batchListReciver.setConfig(config);
                                                            batchListReciver.setStickerQty(String.valueOf(stickerQty));
                                                            batchListRecivers.add(batchListReciver);
                                                            int size = commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().size();
                                                            commonReceivingShippingDetailDataLists.get(position).getBatchListReceiver().add(batchListReciver);
                                                            forRefreshList(position);
                                                            break;
                                                        }
                                                    }
                                                }

                                                forRefreshList(position);
                                            } else {
                                                Toast.makeText(InventoryControlDetail.this, "Picked Qty. is not more then Shipped Qty.", Toast.LENGTH_SHORT).show();
                                                sequenceQRNumber.remove(qrDetails);
                                                scanQR.setText("");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (itemMatchedOrNot == false) {
                            Toast.makeText(InventoryControlDetail.this, "QR is not matched", Toast.LENGTH_SHORT).show();
                            sequenceQRNumber.remove(qrDetails);
                            scanQR.setText("");
                        }
                    } else {
                        Toast.makeText(InventoryControlDetail.this, "QR is not available", Toast.LENGTH_SHORT).show();
                        sequenceQRNumber.remove(qrDetails);
                        scanQR.setText("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBodyQRDetails> call, Throwable t) {
                progressDialog.dismiss();
                scanQR.setText("");
                sequenceQRNumber.remove(qrDetails);
                Toast.makeText(InventoryControlDetail.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static BigDecimal roundToPlaces(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd;
    }

    public void forRefreshList(int position) {
        commonReceivingShippingDetailDataList tempRcdDetail = new commonReceivingShippingDetailDataList();
        tempRcdDetail = commonReceivingShippingDetailDataLists.get(position);
        commonReceivingShippingDetailDataLists.remove(position);
        adapter.notifyItemRemoved(position);
        commonReceivingShippingDetailDataLists.add(0, tempRcdDetail);
        adapter.notifyItemInserted(0);
        recyclerViewItems.smoothScrollToPosition(0);
        scanQR.setText("");
    }

    public void update_Qty(String pckQty, final int position) {
        final Dialog dialog = new Dialog(InventoryControlDetail.this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.update_qty);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;

        final AppCompatAutoCompleteTextView pckd_qty = dialog.findViewById(R.id.pckd_qty);
        final AppCompatAutoCompleteTextView updte_qty = dialog.findViewById(R.id.updte_qty);
        pckd_qty.setText(pckQty);
        AppCompatButton submitBtn = dialog.findViewById(R.id.submitBtn);
        AppCompatButton cncld = dialog.findViewById(R.id.cncld);

        cncld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i <= sequenceQRNumber.size() - 1; i++) {
                    String qrNUmber = sequenceQRNumber.get(i).getStickerSeq();
                    if (qrNUmber.equals(qrDetails)) {
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
                if (update == 0) {
                    Toast.makeText(InventoryControlDetail.this, "Please Input Update Quantity.", Toast.LENGTH_SHORT).show();
                } else {


                    float UpdateQty = packed * update;
                    float shippedQty = Float.parseFloat(commonReceivingShippingDetailDataLists.get(position).getShippedQty());


                    if (commonReceivingShippingDetailDataLists.get(position).getpickededQty() == null) {
                        if (UpdateQty == shippedQty) {
                            commonReceivingShippingDetailDataLists.get(position).setremainingQty("0");
                            commonReceivingShippingDetailDataLists.get(position).setpickededQty(String.valueOf(UpdateQty));
                            commonReceivingShippingDetailDataLists.get(position).setReason("");
                            commonReceivingShippingDetailDataList tempRcdDetail = new commonReceivingShippingDetailDataList();
                            tempRcdDetail = commonReceivingShippingDetailDataLists.get(position);
                            commonReceivingShippingDetailDataLists.remove(position);
                            adapter.notifyItemRemoved(position);
                            commonReceivingShippingDetailDataLists.add(0, tempRcdDetail);
                            adapter.notifyItemInserted(0);
                            recyclerViewItems.smoothScrollToPosition(0);
                            scanQR.setText("");

                        } else if (UpdateQty < shippedQty) {
                            float remaining = shippedQty - UpdateQty;
                            commonReceivingShippingDetailDataLists.get(position).setremainingQty(String.valueOf(remaining));
                            commonReceivingShippingDetailDataLists.get(position).setpickededQty(String.valueOf(UpdateQty));

                            if (remaining == 0) {
                                commonReceivingShippingDetailDataLists.get(position).setReason("");
                            }

                            commonReceivingShippingDetailDataList tempRcdDetail = new commonReceivingShippingDetailDataList();
                            tempRcdDetail = commonReceivingShippingDetailDataLists.get(position);
                            commonReceivingShippingDetailDataLists.remove(position);
                            adapter.notifyItemRemoved(position);
                            commonReceivingShippingDetailDataLists.add(0, tempRcdDetail);
                            adapter.notifyItemInserted(0);
                            recyclerViewItems.smoothScrollToPosition(0);
                            scanQR.setText("");
                        } else {
                            for (int i = 0; i <= sequenceQRNumber.size() - 1; i++) {
                                String qrNUmber = sequenceQRNumber.get(i).getStickerSeq();
                                if (qrNUmber.equals(qrDetails)) {
                                    sequenceQRNumber.remove(i);
                                }
                            }
                            Toast.makeText(InventoryControlDetail.this, "Picked Qty not greater then Approved Qty.", Toast.LENGTH_SHORT).show();
                            scanQR.setText("");
                        }
                    } else {

                        float prePackedQty = Float.parseFloat(commonReceivingShippingDetailDataLists.get(position).getpickededQty());
                        float finalUpdateQty = UpdateQty + prePackedQty;

                        if (finalUpdateQty == shippedQty) {
                            commonReceivingShippingDetailDataLists.get(position).setremainingQty("0");
                            commonReceivingShippingDetailDataLists.get(position).setpickededQty(String.valueOf(finalUpdateQty));
                            commonReceivingShippingDetailDataLists.get(position).setReason("");
                            commonReceivingShippingDetailDataList tempRcdDetail = new commonReceivingShippingDetailDataList();
                            tempRcdDetail = commonReceivingShippingDetailDataLists.get(position);
                            commonReceivingShippingDetailDataLists.remove(position);
                            adapter.notifyItemRemoved(position);
                            commonReceivingShippingDetailDataLists.add(0, tempRcdDetail);
                            adapter.notifyItemInserted(0);
                            recyclerViewItems.smoothScrollToPosition(0);
                            scanQR.setText("");

                        } else if (finalUpdateQty < shippedQty) {
                            float remaining = shippedQty - UpdateQty;
                            commonReceivingShippingDetailDataLists.get(position).setremainingQty(String.valueOf(remaining));
                            commonReceivingShippingDetailDataLists.get(position).setpickededQty(String.valueOf(finalUpdateQty));

                            if (remaining == 0) {
                                commonReceivingShippingDetailDataLists.get(position).setReason("");
                            }

                            commonReceivingShippingDetailDataList tempRcdDetail = new commonReceivingShippingDetailDataList();
                            tempRcdDetail = commonReceivingShippingDetailDataLists.get(position);
                            commonReceivingShippingDetailDataLists.remove(position);
                            adapter.notifyItemRemoved(position);
                            commonReceivingShippingDetailDataLists.add(0, tempRcdDetail);
                            adapter.notifyItemInserted(0);
                            recyclerViewItems.smoothScrollToPosition(0);
                            scanQR.setText("");
                        } else {
                            for (int i = 0; i <= sequenceQRNumber.size() - 1; i++) {
                                String qrNUmber = sequenceQRNumber.get(i).getStickerSeq();
                                if (qrNUmber.equals(qrDetails)) {
                                    sequenceQRNumber.remove(i);
                                }
                            }
                            Toast.makeText(InventoryControlDetail.this, "Picked Qty not greater then Approved Qty.", Toast.LENGTH_SHORT).show();
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
}
