package com.yoeki.kalpnay.frdinventry.InventoryShipperPicker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.util.Log;
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
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.BatchNoList;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.InventoryPendingModel;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.ParticularRequisitionDetails;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.ResponseShippingDetails;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.StickerList;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.StickersDialogData;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.commonReceivingShippingDetailDataList;
import com.yoeki.kalpnay.frdinventry.Items.commonReceivingShippingDetailList;
import com.yoeki.kalpnay.frdinventry.QRDetails.RequestBodyQRDetails;
import com.yoeki.kalpnay.frdinventry.QRDetails.RequisitionWiseQRDetail;
import com.yoeki.kalpnay.frdinventry.QRDetails.ResponseBodyQRDetails;
import com.yoeki.kalpnay.frdinventry.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequisitionControlDetails extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    RecyclerView rcy_items;
    AppCompatTextView reqNo, rcd_Date, branchName_RCD;
    AppCompatButton shipBtn, languageChangeRCD, img_back, saveTempBtn;
    CheckBox checkUpdateQty;
    AppCompatAutoCompleteTextView scanQR;
    List<commonReceivingShippingDetailDataList> commonReceivingShippingDetailDataLists;
    ArrayList<commonReceivingShippingDetailDataList> forShipping;

    int sequenceAddorNot = 0;
    commonReceivingShippingDetailList adapter;
    int languageChangeVisible = 1;
    List<StickerList> sequenceQRNumber = new ArrayList<>();
    public List<StickersDialogData> stickersDialogDataList = new ArrayList<>();
    String reqNmbr = "", locationId, wareHouse, valueElseRem = "", valueRem = "", qrDetails, roleID;
    int batchListSize = 0;

    ArrayList<ResponseBodyQRDetails> responses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_control_detail);
        Initialize();


        reqNmbr = getIntent().getStringExtra("RequisitionNo");
        wareHouse = getIntent().getStringExtra("wareHouse");
        locationId = getIntent().getStringExtra("locationCode");
        reqNo.setText(reqNmbr);
        addlistdata();

        sequenceQRNumber.clear();
        stickersDialogDataList.clear();

        img_back.setOnClickListener(this);
        shipBtn.setOnClickListener(this);
        saveTempBtn.setOnClickListener(this);
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
                if (qrDetails.length() >= 8 && qrDetails.length() <= 18) {

                    if (sequenceQRNumber.size() == 0) {
                        StickerList stickerList = new StickerList("");
                        stickerList.setStickerSeq(qrDetails);
                        sequenceQRNumber.add(stickerList);
                        qrRCDDetailsWithoutCheckBox();
                    } else {
                        for (int i = 0; i <= sequenceQRNumber.size() - 1; i++) {
                            String sequence = sequenceQRNumber.get(i).getStickerSeq();
                            if (sequence.equals(qrDetails)) {
                                Toast.makeText(RequisitionControlDetails.this, "QR is already Scanned.", Toast.LENGTH_SHORT).show();
                                scanQR.setText("");
                                sequenceAddorNot = 1;
                                break;
                            }
                        }
                        if (sequenceAddorNot == 0) {
                            StickerList stickerList = new StickerList("");
                            stickerList.setStickerSeq(qrDetails);
                            sequenceQRNumber.add(stickerList);
                            qrRCDDetailsWithoutCheckBox();
                        }

                        sequenceAddorNot = 0;

                    }


//                    if (sequenceQRNumber.contains(qrDetails)) {
//                        Toast.makeText(RequisitionControlDetails.this, "QR is already Scanned.", Toast.LENGTH_SHORT).show();
//                        scanQR.setText("");
//                    } else {
//                        StickerList stickerList = new StickerList();
//                        stickerList.setStickerSeq(qrDetails);
//                        if (!sequenceQRNumber.contains(qrDetails)) {
//                            sequenceQRNumber.add(stickerList);
//                        }
//                        qrRCDDetailsWithoutCheckBox();
//                    }


                }
            }
        };
        scanQR.addTextChangedListener(textWatcher);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        roleID = Preference.getInstance(getApplicationContext()).getRole();
        if (roleID.equals("1") || roleID.equals("2")) {
            shipBtn.setVisibility(View.GONE);
        }
    }

    public void qrRCDDetailsWithoutCheckBox() {
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

                        for (int position = 0; position < commonReceivingShippingDetailDataLists.size(); position++) {
                            String listRCDItemid = commonReceivingShippingDetailDataLists.get(position).getItemId();

                            if (responseItemID.equals(listRCDItemid)) {
                                itemMatchedOrNot = true;

                                stickersDialogDataList.add(new StickersDialogData(qrDetails, response.body().getBatchId(), commonReceivingShippingDetailDataLists.get(position).getItemId()));

                                String qrDataStickerqty = response.body().getStickerQty();
                                commonReceivingShippingDetailDataLists.get(position).setConfig(response.body().getConfig());
                                String[] expdate = response.body().getExpdate().split("\\s+");
                                commonReceivingShippingDetailDataLists.get(position).setExpdate(expdate[0]);
                                commonReceivingShippingDetailDataLists.get(position).setBatchId(response.body().getBatchId());

                                if (checkUpdateQty.isChecked()) {
                                    update_Qty(qrDataStickerqty, position, response);
                                } else {
                                    if (commonReceivingShippingDetailDataLists.get(position).getpickededQty() == null) {
                                        if (commonReceivingShippingDetailDataLists.get(position).getApprovedQty().equals(qrDataStickerqty)) {
                                            responses.add(response.body());
                                            commonReceivingShippingDetailDataLists.get(position).setremainingQty("0");
                                            commonReceivingShippingDetailDataLists.get(position).setpickededQty(qrDataStickerqty);
                                            commonReceivingShippingDetailDataLists.get(position).setReason("");
                                            commonReceivingShippingDetailDataLists.get(position).setUnitId(response.body().getUnitId());

                                            List<BatchNoList> batchNoLists = new ArrayList<>();
                                            String batchNo = commonReceivingShippingDetailDataLists.get(position).getBatchId();
                                            String batchQty = commonReceivingShippingDetailDataLists.get(position).getpickededQty();
                                            String config = commonReceivingShippingDetailDataLists.get(position).getConfig();
                                            if (commonReceivingShippingDetailDataLists.get(position).getBatchNoList() == null) {
                                                BatchNoList batchNoList = new BatchNoList();
                                                batchNoList.setBatchNo(batchNo);
                                                batchNoList.setBatchQty(batchQty);
                                                batchNoList.setConfig(config);
                                                batchNoList.setBatchAutoIncreeId(response.body().getBatchAutoIncreeId());
                                                batchNoLists.add(batchNoList);
                                                commonReceivingShippingDetailDataLists.get(position).setBatchNoList(batchNoLists);
                                            }

                                            forRefreshList(position);

                                        } else {
                                            float appqty = Float.parseFloat(commonReceivingShippingDetailDataLists.get(position).getApprovedQty());
                                            float qrDataQtytemp = Float.parseFloat(qrDataStickerqty);
                                            float qrDataQty = roundToPlaces(qrDataQtytemp, 3);
                                            float remQty = roundToPlaces(appqty - qrDataQty, 3);

                                            String test = String.format("%.03f", remQty);
                                            commonReceivingShippingDetailDataLists.get(position).setUnitId(response.body().getUnitId());
                                            if (remQty >= 0) {
                                                responses.add(response.body());
                                                commonReceivingShippingDetailDataLists.get(position).setremainingQty(test);
                                                commonReceivingShippingDetailDataLists.get(position).setpickededQty(qrDataStickerqty);
                                                if (remQty == 0) {
                                                    commonReceivingShippingDetailDataLists.get(position).setReason("");
                                                }

                                                String batchNo = commonReceivingShippingDetailDataLists.get(position).getBatchId();
                                                String batchQty = commonReceivingShippingDetailDataLists.get(position).getpickededQty();
                                                String config = commonReceivingShippingDetailDataLists.get(position).getConfig();
                                                float stickQty = Float.parseFloat(response.body().getStickerQty());
                                                List<BatchNoList> batchNoLists = new ArrayList<>();
                                                if (commonReceivingShippingDetailDataLists.get(position).getBatchNoList() == null) {
                                                    BatchNoList batchNoList = new BatchNoList();
                                                    batchNoList.setBatchNo(batchNo);
                                                    batchNoList.setBatchQty(batchQty);
                                                    batchNoList.setConfig(config);
                                                    batchNoList.setBatchAutoIncreeId(response.body().getBatchAutoIncreeId());
                                                    batchNoLists.add(batchNoList);
                                                    commonReceivingShippingDetailDataLists.get(position).setBatchNoList(batchNoLists);

                                                } else if (commonReceivingShippingDetailDataLists.get(position).getBatchNoList().size() == 0) {
                                                    BatchNoList batchNoList = new BatchNoList();
                                                    batchNoList.setBatchNo(batchNo);
                                                    batchNoList.setBatchQty(batchQty);
                                                    batchNoList.setConfig(config);
                                                    batchNoList.setBatchAutoIncreeId(response.body().getBatchAutoIncreeId());
                                                    batchNoLists.add(batchNoList);
                                                    commonReceivingShippingDetailDataLists.get(position).setBatchNoList(batchNoLists);

                                                } else {
                                                    boolean batchB = false;
                                                    boolean confB = false;
                                                    if (commonReceivingShippingDetailDataLists.get(position).getBatchNoList() != null) {


                                                        for (int j = 0; j < commonReceivingShippingDetailDataLists.get(position).getBatchNoList().size(); j++) {
                                                            String batch = commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(j).getBatchNo();
                                                            if (batchNo.equals(batch)) {
                                                                batchB = true;
                                                                for (int k = 0; k < commonReceivingShippingDetailDataLists.get(position).getBatchNoList().size(); k++) {
                                                                    String configur = commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(k).getConfig();
                                                                    if (configur.equals(response.body().getConfig())) {
                                                                        String confBatch = commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(k).getBatchNo();
                                                                        if (batch.equals(confBatch)) {
                                                                            float prevBatchQty = Float.parseFloat(commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(k).getBatchQty());
                                                                            float total = prevBatchQty + stickQty;
                                                                            commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(k).setBatchQty(String.valueOf(total));
                                                                            confB = true;

                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        if (confB == false) {
                                                            BatchNoList batchNoList = new BatchNoList();
                                                            batchNoList.setBatchNo(batchNo);
                                                            batchNoList.setBatchQty(String.valueOf(stickQty));
                                                            batchNoList.setConfig(config);
                                                            batchNoList.setBatchAutoIncreeId(response.body().getBatchAutoIncreeId());
                                                            batchNoLists.add(batchNoList);
                                                            commonReceivingShippingDetailDataLists.get(position).getBatchNoList().add(batchNoList);
                                                            forRefreshList(position);
                                                            break;
                                                        }

                                                        if (batchB == false) {
                                                            BatchNoList batchNoList = new BatchNoList();
                                                            batchNoList.setBatchNo(batchNo);
                                                            batchNoList.setBatchQty(String.valueOf(stickQty));
                                                            batchNoList.setConfig(config);
                                                            batchNoList.setBatchAutoIncreeId(response.body().getBatchAutoIncreeId());
                                                            batchNoLists.add(batchNoList);
                                                            commonReceivingShippingDetailDataLists.get(position).getBatchNoList().add(batchNoList);
                                                            forRefreshList(position);
                                                            break;
                                                        }
                                                    }
                                                }

                                                forRefreshList(position);
                                            } else {
                                                Toast.makeText(RequisitionControlDetails.this, "Picked Qty. is not more then Approved Qty.", Toast.LENGTH_SHORT).show();
                                                evergreenLoop();
                                                scanQR.setText("");
                                            }

                                        }
                                    } else {
                                        float prePickedQty = Float.parseFloat(commonReceivingShippingDetailDataLists.get(position).getpickededQty());
                                        float QRDataStickerqty = Float.parseFloat(qrDataStickerqty);
                                        float totalpickedQty = roundToPlaces(prePickedQty + QRDataStickerqty, 3);
                                        float approvedQty = Float.parseFloat(commonReceivingShippingDetailDataLists.get(position).getApprovedQty());

                                        if (approvedQty == totalpickedQty) {
                                            responses.add(response.body());
                                            commonReceivingShippingDetailDataLists.get(position).setremainingQty("0");
                                            String tempPickQty = String.format("%.03f", totalpickedQty);
                                            commonReceivingShippingDetailDataLists.get(position).setpickededQty(tempPickQty);
                                            commonReceivingShippingDetailDataLists.get(position).setReason("");
                                            commonReceivingShippingDetailDataLists.get(position).setUnitId(response.body().getUnitId());
//                                            spinner_reason.setEnabled(false);

                                            List<BatchNoList> batchNoLists = new ArrayList<>();
                                            String batchNo = commonReceivingShippingDetailDataLists.get(position).getBatchId();
                                            String batchQty = commonReceivingShippingDetailDataLists.get(position).getpickededQty();
                                            float stickQty = Float.parseFloat(response.body().getStickerQty());
                                            String config = commonReceivingShippingDetailDataLists.get(position).getConfig();
                                            if (commonReceivingShippingDetailDataLists.get(position).getBatchNoList() == null) {
                                                BatchNoList batchNoList = new BatchNoList();
                                                batchNoList.setBatchNo(batchNo);
                                                batchNoList.setBatchQty(batchQty);
                                                batchNoList.setConfig(config);
                                                batchNoList.setBatchAutoIncreeId(response.body().getBatchAutoIncreeId());
                                                batchNoLists.add(batchNoList);
                                                commonReceivingShippingDetailDataLists.get(position).setBatchNoList(batchNoLists);

                                            } else if (commonReceivingShippingDetailDataLists.get(position).getBatchNoList().size() == 0) {
                                                BatchNoList batchNoList = new BatchNoList();
                                                batchNoList.setBatchNo(batchNo);
                                                batchNoList.setBatchQty(batchQty);
                                                batchNoList.setConfig(config);
                                                batchNoList.setBatchAutoIncreeId(response.body().getBatchAutoIncreeId());
                                                batchNoLists.add(batchNoList);
                                                commonReceivingShippingDetailDataLists.get(position).setBatchNoList(batchNoLists);

                                            } else {
                                                boolean batchB = false;
                                                boolean confB = false;
                                                if (commonReceivingShippingDetailDataLists.get(position).getBatchNoList() != null) {

                                                    for (int j = 0; j < commonReceivingShippingDetailDataLists.get(position).getBatchNoList().size(); j++) {
                                                        String batch = commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(j).getBatchNo();
                                                        if (batchNo.equals(batch)) {
                                                            batchB = true;
                                                            for (int k = 0; k < commonReceivingShippingDetailDataLists.get(position).getBatchNoList().size(); k++) {
                                                                String configur = commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(k).getConfig();
                                                                if (configur.equals(response.body().getConfig())) {
                                                                    String confBatch = commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(k).getBatchNo();
                                                                    if (batch.equals(confBatch)) {
                                                                        float prevBatchQty = Float.parseFloat(commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(k).getBatchQty());
                                                                        float total = prevBatchQty + stickQty;
                                                                        commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(k).setBatchQty(String.valueOf(total));
                                                                        confB = true;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }

                                                    if (confB == false) {
                                                        BatchNoList batchNoList = new BatchNoList();
                                                        batchNoList.setBatchNo(batchNo);
                                                        batchNoList.setBatchQty(String.valueOf(stickQty));
                                                        batchNoList.setConfig(config);
                                                        batchNoList.setBatchAutoIncreeId(response.body().getBatchAutoIncreeId());
                                                        batchNoLists.add(batchNoList);
                                                        commonReceivingShippingDetailDataLists.get(position).getBatchNoList().add(batchNoList);
                                                        forRefreshList(position);
                                                        break;
                                                    }

                                                    if (batchB == false) {
                                                        BatchNoList batchNoList = new BatchNoList();
                                                        batchNoList.setBatchNo(batchNo);
                                                        batchNoList.setBatchQty(String.valueOf(stickQty));
                                                        batchNoList.setConfig(config);
                                                        batchNoList.setBatchAutoIncreeId(response.body().getBatchAutoIncreeId());
                                                        batchNoLists.add(batchNoList);
                                                        commonReceivingShippingDetailDataLists.get(position).getBatchNoList().add(batchNoList);
                                                        forRefreshList(position);
                                                        break;
                                                    }
                                                }
                                            }
                                            forRefreshList(position);
                                        } else {
                                            float remqty = Float.parseFloat(commonReceivingShippingDetailDataLists.get(position).getApprovedQty()) - totalpickedQty;

                                            String test = String.format("%.03f", remqty);

                                            float stickQty = Float.parseFloat(response.body().getStickerQty());
                                            commonReceivingShippingDetailDataLists.get(position).setUnitId(response.body().getUnitId());
                                            if (remqty >= 0) {
                                                responses.add(response.body());
                                                commonReceivingShippingDetailDataLists.get(position).setremainingQty(test);
                                                String tempPickQty = String.format("%.03f", totalpickedQty);
                                                commonReceivingShippingDetailDataLists.get(position).setpickededQty(tempPickQty);

                                                if (remqty == 0.0) {
                                                    commonReceivingShippingDetailDataLists.get(position).setReason("");
                                                }

                                                List<BatchNoList> batchNoLists = new ArrayList<>();
                                                String batchNo = commonReceivingShippingDetailDataLists.get(position).getBatchId();
                                                String batchQty = commonReceivingShippingDetailDataLists.get(position).getpickededQty();
                                                String config = commonReceivingShippingDetailDataLists.get(position).getConfig();
                                                if (commonReceivingShippingDetailDataLists.get(position).getBatchNoList() == null) {
                                                    BatchNoList batchNoList = new BatchNoList();
                                                    batchNoList.setBatchNo(batchNo);
                                                    batchNoList.setBatchQty(batchQty);
                                                    batchNoList.setConfig(config);
                                                    batchNoList.setBatchAutoIncreeId(response.body().getBatchAutoIncreeId());
                                                    batchNoLists.add(batchNoList);
                                                    commonReceivingShippingDetailDataLists.get(position).setBatchNoList(batchNoLists);

                                                } else if (commonReceivingShippingDetailDataLists.get(position).getBatchNoList().size() == 0) {
                                                    BatchNoList batchNoList = new BatchNoList();
                                                    batchNoList.setBatchNo(batchNo);
                                                    batchNoList.setBatchQty(batchQty);
                                                    batchNoList.setConfig(config);
                                                    batchNoList.setBatchAutoIncreeId(response.body().getBatchAutoIncreeId());
                                                    batchNoLists.add(batchNoList);
                                                    commonReceivingShippingDetailDataLists.get(position).setBatchNoList(batchNoLists);

                                                } else {
//                                                    boolean confBatchB = false;
                                                    boolean batchB = false;
                                                    boolean confB = false;
                                                    if (commonReceivingShippingDetailDataLists.get(position).getBatchNoList() != null) {

                                                        for (int j = 0; j < commonReceivingShippingDetailDataLists.get(position).getBatchNoList().size(); j++) {
                                                            String batch = commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(j).getBatchNo();
                                                            if (batchNo.equals(batch)) {
                                                                batchB = true;
                                                                for (int k = 0; k < commonReceivingShippingDetailDataLists.get(position).getBatchNoList().size(); k++) {
                                                                    String configur = commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(k).getConfig();
                                                                    if (configur.equals(response.body().getConfig())) {
                                                                        String confBatch = commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(k).getBatchNo();
                                                                        if (batch.equals(confBatch)) {
                                                                            float prevBatchQty = Float.parseFloat(commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(k).getBatchQty());
                                                                            float total = prevBatchQty + stickQty;
                                                                            commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(k).setBatchQty(String.valueOf(total));
                                                                            confB = true;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        if (confB == false) {
                                                            BatchNoList batchNoList = new BatchNoList();
                                                            batchNoList.setBatchNo(batchNo);
                                                            batchNoList.setBatchQty(String.valueOf(stickQty));
                                                            batchNoList.setConfig(config);
                                                            batchNoList.setBatchAutoIncreeId(response.body().getBatchAutoIncreeId());
                                                            batchNoLists.add(batchNoList);
                                                            commonReceivingShippingDetailDataLists.get(position).getBatchNoList().add(batchNoList);
                                                            forRefreshList(position);
                                                            break;
                                                        }

                                                        if (batchB == false) {
                                                            BatchNoList batchNoList = new BatchNoList();
                                                            batchNoList.setBatchNo(batchNo);
                                                            batchNoList.setBatchQty(String.valueOf(stickQty));
                                                            batchNoList.setConfig(config);
                                                            batchNoList.setBatchAutoIncreeId(response.body().getBatchAutoIncreeId());
                                                            batchNoLists.add(batchNoList);
                                                            commonReceivingShippingDetailDataLists.get(position).getBatchNoList().add(batchNoList);
                                                            forRefreshList(position);
                                                            break;
                                                        }
                                                    }
                                                }
                                                forRefreshList(position);
                                            } else {
                                                Toast.makeText(RequisitionControlDetails.this, "Picked Qty. is not more then Approved Qty.", Toast.LENGTH_SHORT).show();
                                                evergreenLoop();
                                                scanQR.setText("");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (itemMatchedOrNot == false) {
                            Toast.makeText(RequisitionControlDetails.this, "QR is not matched", Toast.LENGTH_SHORT).show();
                            evergreenLoop();
                            scanQR.setText("");
                        }

                    } else {
                        Toast.makeText(RequisitionControlDetails.this, "QR is not available", Toast.LENGTH_SHORT).show();
                        evergreenLoop();
                        scanQR.setText("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBodyQRDetails> call, Throwable t) {
                progressDialog.dismiss();
                evergreenLoop();
                scanQR.setText("");
                Toast.makeText(RequisitionControlDetails.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void evergreenLoop() {
        for (int i = 0; i <= sequenceQRNumber.size() - 1; i++) {
            if (qrDetails.equals(sequenceQRNumber.get(i).getStickerSeq().toString())) {
                sequenceQRNumber.remove(i);
            }
        }
        for (int i = 0; i <= stickersDialogDataList.size() - 1; i++) {
            if (qrDetails.equals(stickersDialogDataList.get(i).getStickerSeq())) {
                stickersDialogDataList.remove(i);
            }
        }
    }

    public void forRefreshList(int position) {
        commonReceivingShippingDetailDataList tempRcdDetail = new commonReceivingShippingDetailDataList();
        tempRcdDetail = commonReceivingShippingDetailDataLists.get(position);
        commonReceivingShippingDetailDataLists.remove(position);
        adapter.notifyItemRemoved(position);
        commonReceivingShippingDetailDataLists.add(0, tempRcdDetail);
        adapter.notifyItemInserted(0);
        rcy_items.smoothScrollToPosition(0);
        scanQR.setText("");
    }

    public void Initialize() {
        reqNo = findViewById(R.id.reqNo);
        rcd_Date = findViewById(R.id.rcd_Date);
        branchName_RCD = findViewById(R.id.branchName_RCD);
        scanQR = findViewById(R.id.scanQR);
        shipBtn = findViewById(R.id.shipBtn);
        saveTempBtn = findViewById(R.id.saveTempBtn);
        img_back = findViewById(R.id.img_back);
        languageChangeRCD = findViewById(R.id.languageChangeRCD);
        rcy_items = findViewById(R.id.rcy_items);
        checkUpdateQty = findViewById(R.id.checkUpdateQty);
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
                break;
            case R.id.saveTempBtn:
                saveTempData();
                break;
            case R.id.languageChangeRCD:
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                adapter = new commonReceivingShippingDetailList(RequisitionControlDetails.this, commonReceivingShippingDetailDataLists, languageChangeVisible);
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

        ParticularRequisitionDetails userIDModeParticularRequisitionDetailsl = new ParticularRequisitionDetails(userId, reqNmbr);
        Call<InventoryPendingModel> call = apiInterface.inventoryPicker(userIDModeParticularRequisitionDetailsl);
        call.enqueue(new Callback<InventoryPendingModel>() {
            @Override
            public void onResponse(Call<InventoryPendingModel> call, Response<InventoryPendingModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.body().getStatus().equals("Success")) {

                        commonReceivingShippingDetailDataLists = response.body().getDataList();

                        String[] Date = response.body().getDataList().get(0).getReqestDate().split("\\s+");
                        rcd_Date.setText(Date[0]);
                        branchName_RCD.setText(response.body().getDataList().get(0).getBranch());
                        rcy_items.setLayoutManager(new LinearLayoutManager(RequisitionControlDetails.this, LinearLayoutManager.VERTICAL, false));
                        rcy_items.setItemAnimator(new DefaultItemAnimator());
                        adapter = new commonReceivingShippingDetailList(RequisitionControlDetails.this, commonReceivingShippingDetailDataLists, "Shipper");
                        rcy_items.setAdapter(adapter);
                        rcy_items.scheduleLayoutAnimation();
                        getTempData();
                    } else {
                        Toast.makeText(RequisitionControlDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
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
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.transfer_order_detail);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        AppCompatTextView to_number = dialog.findViewById(R.id.to_number);
        AppCompatButton ok_toNumbr = dialog.findViewById(R.id.ok_toNumbr);
        to_number.setText(toNumber);
        ok_toNumbr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), InventoryPending.class);
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
        lp.gravity = Gravity.CENTER;

        AppCompatButton submitBtn = dialog.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                evergreenLoop();
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

    public void shipping() {
        try {
            forShipping = new ArrayList<commonReceivingShippingDetailDataList>();
            for (int i = 0; i <= commonReceivingShippingDetailDataLists.size(); i++) {
                float approv = Float.parseFloat(commonReceivingShippingDetailDataLists.get(i).getApprovedQty());
                if (commonReceivingShippingDetailDataLists.get(i).getpickededQty() != null && commonReceivingShippingDetailDataLists.get(i).getpickededQty() != "0") {
                    float picked = Float.parseFloat(commonReceivingShippingDetailDataLists.get(i).getpickededQty());
                    float remaining = Float.parseFloat(commonReceivingShippingDetailDataLists.get(i).getremainingQty());
                    float totalQty = (picked) + (remaining);

                    if (approv == (picked)) {
                        commonReceivingShippingDetailDataLists.get(i).setReason("");
                        forShipping.add(commonReceivingShippingDetailDataLists.get(i));
                        if (commonReceivingShippingDetailDataLists.size() == i + 1) {
                            serviceRunShip();
                        }
                    } else if (approv == (totalQty)) {
                        if (commonReceivingShippingDetailDataLists.get(i).getReason().equals("Select Reason")) {
                            String ItemNo = commonReceivingShippingDetailDataLists.get(i).getItemId();
                            Toast.makeText(this, "Please select reason of Item ID: " + ItemNo, Toast.LENGTH_SHORT).show();
                            forShipping.clear();
                            break;
                        } else {
                            forShipping.add(commonReceivingShippingDetailDataLists.get(i));
                            if (commonReceivingShippingDetailDataLists.size() == i + 1) {
                                serviceRunShip();
                            }
                        }
                    }
                } else {
                    if (commonReceivingShippingDetailDataLists.get(i).getReason() == "Select Reason") {
                        Toast.makeText(this, "Select Reason of " + commonReceivingShippingDetailDataLists.get(i).getItemId(), Toast.LENGTH_SHORT).show();
                    } else {
                        commonReceivingShippingDetailDataLists.get(i).setpickededQty("0");
                        commonReceivingShippingDetailDataLists.get(i).setremainingQty(commonReceivingShippingDetailDataLists.get(i).getApprovedQty());
                        forShipping.add(commonReceivingShippingDetailDataLists.get(i));
                        if (commonReceivingShippingDetailDataLists.size() == i + 1) {
                            serviceRunShip();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void serviceRunShip() {
        shipBtn.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(RequisitionControlDetails.this);
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        progressDialog.setCancelable(false); // set cancelable to false
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);

        String finalLoc = "";
        String[] loc = forShipping.get(0).getItemId().split("-");
        if (loc[0].equals("RM")) {
            finalLoc = "MWH";
        } else {
//            finalLoc = "CK";          remove by satyendar on 31 oct 19
            finalLoc = "Cen-Kitc";
        }
        String userId = Preference.getInstance(getApplicationContext()).getUserId();

        RequestBodyShipDetails requestBodyShipDetails = new RequestBodyShipDetails(reqNmbr, finalLoc, locationId, userId, forShipping, sequenceQRNumber);
        String json = new Gson().toJson(requestBodyShipDetails);
        Log.d("json", json);
        Call<ResponseShippingDetails> call = apiInterface.redShipping(requestBodyShipDetails);
        call.enqueue(new Callback<ResponseShippingDetails>() {
            @Override
            public void onResponse(Call<ResponseShippingDetails> call, Response<ResponseShippingDetails> response) {
                progressDialog.dismiss();
                shipBtn.setEnabled(true);
                if (response.body().getStatus().equals("success")) {
                    transferOrderDialog(response.body().getTONumber());
                    deleteTempData();
                } else {
                    alertDialog(response.body().getMessage());
                }

            }

            @Override
            public void onFailure(Call<ResponseShippingDetails> call, Throwable t) {
                progressDialog.dismiss();
                shipBtn.setEnabled(true);
                Toast.makeText(RequisitionControlDetails.this, "Time Out. Please try again.", Toast.LENGTH_SHORT).show();
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

    public void update_Qty(String pckQty, final int position, final Response<ResponseBodyQRDetails> response) {
        final Dialog dialog = new Dialog(RequisitionControlDetails.this);
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
                dialog.dismiss();
                evergreenLoop();
                scanQR.setText("");
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float packed = Float.parseFloat(pckd_qty.getText().toString());
                float update = Float.parseFloat(updte_qty.getText().toString());
                if (update == 0) {
                    Toast.makeText(RequisitionControlDetails.this, "Please Input Update Quantity.", Toast.LENGTH_SHORT).show();
                } else {


                    float UpdateQty = packed * update;
                    float approvQty = Float.parseFloat(commonReceivingShippingDetailDataLists.get(position).getApprovedQty());


                    if (commonReceivingShippingDetailDataLists.get(position).getpickededQty() == null) {
                        if (UpdateQty == approvQty) {
                            commonReceivingShippingDetailDataLists.get(position).setremainingQty("0");
                            commonReceivingShippingDetailDataLists.get(position).setpickededQty(String.valueOf(UpdateQty));
                            commonReceivingShippingDetailDataLists.get(position).setReason("");

                            List<BatchNoList> batchNoLists = new ArrayList<>();
                            String batchNo = commonReceivingShippingDetailDataLists.get(position).getBatchId();
                            String batchQty = commonReceivingShippingDetailDataLists.get(position).getpickededQty();
                            String config = commonReceivingShippingDetailDataLists.get(position).getConfig();
                            if (commonReceivingShippingDetailDataLists.get(position).getBatchNoList() == null) {
                                BatchNoList batchNoList = new BatchNoList();
                                batchNoList.setBatchNo(batchNo);
                                batchNoList.setBatchQty(batchQty);
                                batchNoList.setConfig(config);
                                batchNoList.setBatchAutoIncreeId(response.body().getBatchAutoIncreeId());
                                batchNoLists.add(batchNoList);
                                commonReceivingShippingDetailDataLists.get(position).setBatchNoList(batchNoLists);
                            }
                            forRefreshList(position);


                        } else if (UpdateQty < approvQty) {
                            float remaining = approvQty - UpdateQty;
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
                            rcy_items.smoothScrollToPosition(0);
                            scanQR.setText("");
                        } else {
                            Toast.makeText(RequisitionControlDetails.this, "Picked Qty not greater then Approved Qty.", Toast.LENGTH_SHORT).show();
                            evergreenLoop();
                            scanQR.setText("");
                        }
                    } else {

                        float prePackedQty = Float.parseFloat(commonReceivingShippingDetailDataLists.get(position).getpickededQty());
                        float finalUpdateQty = UpdateQty + prePackedQty;

                        if (finalUpdateQty == approvQty) {
                            commonReceivingShippingDetailDataLists.get(position).setremainingQty("0");
                            commonReceivingShippingDetailDataLists.get(position).setpickededQty(String.valueOf(finalUpdateQty));
                            commonReceivingShippingDetailDataLists.get(position).setReason("");

                            commonReceivingShippingDetailDataList tempRcdDetail = new commonReceivingShippingDetailDataList();
                            tempRcdDetail = commonReceivingShippingDetailDataLists.get(position);
                            commonReceivingShippingDetailDataLists.remove(position);
                            adapter.notifyItemRemoved(position);
                            commonReceivingShippingDetailDataLists.add(0, tempRcdDetail);
                            adapter.notifyItemInserted(0);
                            rcy_items.smoothScrollToPosition(0);
                            scanQR.setText("");
                        } else if (finalUpdateQty < approvQty) {
                            float remaining = approvQty - UpdateQty;
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
                            rcy_items.smoothScrollToPosition(0);

                            evergreenLoop();
                        } else {
                            Toast.makeText(RequisitionControlDetails.this, "Picked Qty not greater then Approved Qty.", Toast.LENGTH_SHORT).show();
                            evergreenLoop();
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
    public void qrRCDDetails() {
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
                        String responseItemID = response.body().getItemId();
                        for (int position = 0; position <= commonReceivingShippingDetailDataLists.size() - 1; position++) {
                            String idItem = commonReceivingShippingDetailDataLists.get(position).getItemId();
                            if (responseItemID.equals(idItem)) {
                                String qty = response.body().getStickerQty();
//                                commonReceivingShippingDetailDataLists.get(position).setConfig(response.body().getConfig());
//                                String[] expdate = response.body().getExpdate().split("\\s+");
//                                commonReceivingShippingDetailDataLists.get(position).setExpdate(expdate[0]);
//                                commonReceivingShippingDetailDataLists.get(position).setBatchId(response.body().getBatchId());

                                if (commonReceivingShippingDetailDataLists.get(position).getpickededQty() == null) {
                                    // For setting the number on picked Qty
//                                    commonReceivingShippingDetailDataLists.get(position).setpickededQty(qty);

                                    if (qty.equals(commonReceivingShippingDetailDataLists.get(position).getApprovedQty())) {
                                        commonReceivingShippingDetailDataLists.get(position).setremainingQty("0");

                                        commonReceivingShippingDetailDataList tempRcdDetail = new commonReceivingShippingDetailDataList();
                                        tempRcdDetail = commonReceivingShippingDetailDataLists.get(position);
                                        commonReceivingShippingDetailDataLists.remove(position);
                                        adapter.notifyItemRemoved(position);
                                        commonReceivingShippingDetailDataLists.add(0, tempRcdDetail);
                                        adapter.notifyItemInserted(0);
                                        rcy_items.smoothScrollToPosition(0);

                                        scanQR.setText("");
                                    } else {
                                        String appQty = commonReceivingShippingDetailDataLists.get(position).getApprovedQty();
                                        float remqty = Float.parseFloat(appQty) - Float.parseFloat(qty);
                                        //
                                        if (remqty > 0) {
//                                            commonReceivingShippingDetailDataLists.get(position).setpickededQty("");
                                            commonReceivingShippingDetailDataLists.get(position).setremainingQty(valueRem);

                                            commonReceivingShippingDetailDataList tempRcdDetail = new commonReceivingShippingDetailDataList();
                                            tempRcdDetail = commonReceivingShippingDetailDataLists.get(position);
                                            commonReceivingShippingDetailDataLists.remove(position);
                                            adapter.notifyItemRemoved(position);
                                            commonReceivingShippingDetailDataLists.add(0, tempRcdDetail);
                                            adapter.notifyItemInserted(0);
                                            rcy_items.smoothScrollToPosition(0);

                                            scanQR.setText("");
                                            break;
                                        }
                                        commonReceivingShippingDetailDataLists.get(position).setremainingQty(String.valueOf(remqty));
                                        valueRem = String.valueOf(remqty);
                                    }

                                    if (checkUpdateQty.isChecked()) {
                                        String pckQty = response.body().getStickerQty();
                                        update_Qty(pckQty, position, response);
                                    }

                                } else {
                                    String picqty = commonReceivingShippingDetailDataLists.get(position).getpickededQty();
                                    float totqty = Float.parseFloat(picqty) + Float.parseFloat(qty);
                                    String totalQty = String.valueOf(totqty);
                                    String previousPickedQty = commonReceivingShippingDetailDataLists.get(position).getpickededQty();
                                    commonReceivingShippingDetailDataLists.get(position).setpickededQty(String.valueOf(totalQty));
                                    if (totalQty.equals(commonReceivingShippingDetailDataLists.get(position).getApprovedQty())) {
                                        commonReceivingShippingDetailDataLists.get(position).setremainingQty("0");
                                        commonReceivingShippingDetailDataList tempRcdDetail = new commonReceivingShippingDetailDataList();
                                        tempRcdDetail = commonReceivingShippingDetailDataLists.get(position);
                                        commonReceivingShippingDetailDataLists.remove(position);
                                        adapter.notifyItemRemoved(position);
                                        commonReceivingShippingDetailDataLists.add(0, tempRcdDetail);
                                        adapter.notifyItemInserted(0);
                                        rcy_items.smoothScrollToPosition(0);

                                        scanQR.setText("");
                                    } else {
                                        String appQty = commonReceivingShippingDetailDataLists.get(position).getApprovedQty();
                                        float remqty = Float.parseFloat(appQty) - Float.parseFloat(totalQty);
                                        if (remqty < 0) {
                                            commonReceivingShippingDetailDataLists.get(position).setpickededQty(previousPickedQty);
                                            if (valueElseRem == "") {
                                            } else {
                                            }
                                            commonReceivingShippingDetailDataLists.get(position).setremainingQty(valueElseRem);

                                            commonReceivingShippingDetailDataList tempRcdDetail = new commonReceivingShippingDetailDataList();
                                            tempRcdDetail = commonReceivingShippingDetailDataLists.get(position);
                                            commonReceivingShippingDetailDataLists.remove(position);
                                            adapter.notifyItemRemoved(position);
                                            commonReceivingShippingDetailDataLists.add(0, tempRcdDetail);
                                            adapter.notifyItemInserted(0);
                                            rcy_items.smoothScrollToPosition(0);

                                            scanQR.setText("");
                                            invalidItemDialog();
                                            break;
                                        }
                                        commonReceivingShippingDetailDataLists.get(position).setremainingQty(String.valueOf(remqty));
                                        valueElseRem = String.valueOf(remqty);
                                    }

                                    if (checkUpdateQty.isChecked()) {
                                        String pckQty = response.body().getStickerQty();
                                        update_Qty(pckQty, position, response);
                                    }
                                }
                            }
                        }
                        String qr = scanQR.getText().toString();
                        if (!qr.equals("")) {
                            invalidItemDialog();
                        }
                    } else {
                        invalidItemDialog();
                    }
                } catch (Exception e) {
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

    public void qrRCDDetailsWithCheckBox() {
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
                        for (int position = 0; position <= commonReceivingShippingDetailDataLists.size() - 1; position++) {
                            String listRCDItemid = commonReceivingShippingDetailDataLists.get(position).getItemId();
                            if (responseItemID.equals(listRCDItemid)) {
                                itemMatchedOrNot = true;
                                String qrDataWiseStickerQty = response.body().getStickerQty();


                            }
                        }
                        if (itemMatchedOrNot == false) {
                            Toast.makeText(RequisitionControlDetails.this, "QR is not matched", Toast.LENGTH_SHORT).show();
                            evergreenLoop();
                            scanQR.setText("");
                        }


                    } else {
                        Toast.makeText(RequisitionControlDetails.this, "QR is not available", Toast.LENGTH_SHORT).show();
                        evergreenLoop();
                        scanQR.setText("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBodyQRDetails> call, Throwable t) {
                progressDialog.dismiss();
                evergreenLoop();
                scanQR.setText("");
                Toast.makeText(RequisitionControlDetails.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static float roundToPlaces(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    private void saveTempData() {
        ArrayList<RequisitionWiseQRDetail> reqCtrlData = Preference.getInstance(getApplicationContext()).getReqCtrlData();

        boolean isReqFound = false;

        if (!responses.isEmpty()) {
            if (reqCtrlData != null) {
                for (int i = 0; i < reqCtrlData.size(); i++) {
                    if (reqCtrlData.get(i).getRequisitionNo().equals(reqNmbr)) {
//                    reqCtrlData.get(i).setCommonReceivingShippingDetailDataLists(commonReceivingShippingDetailDataLists);
                        isReqFound = true;
                        reqCtrlData.get(i).setStickersDialogDataList(stickersDialogDataList);
                        reqCtrlData.get(i).setSequenceQRNumber(sequenceQRNumber);
                        reqCtrlData.get(i).setResponses(responses);
                        break;
                    }
                }
                if (!isReqFound) {
                    reqCtrlData.add(new RequisitionWiseQRDetail(reqNmbr, responses, sequenceQRNumber, stickersDialogDataList));
                }
            } else {
                reqCtrlData = new ArrayList<>();
                reqCtrlData.add(new RequisitionWiseQRDetail(reqNmbr, responses, sequenceQRNumber, stickersDialogDataList));
            }

            Toast.makeText(this, "Temporary data saved successfully", Toast.LENGTH_SHORT).show();
            Preference.getInstance(getApplicationContext()).saveReqCtrlData(reqCtrlData);
        } else {
            Toast.makeText(this, "Please pick some quantity first!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getTempData() {
        ArrayList<RequisitionWiseQRDetail> reqCtrlData = Preference.getInstance(getApplicationContext()).getReqCtrlData();
        if (reqCtrlData != null) {
            for (int i = 0; i < reqCtrlData.size(); i++) {
                if (reqCtrlData.get(i).getRequisitionNo().equals(reqNmbr)) {
                    responses = reqCtrlData.get(i).getResponses();
                    sequenceQRNumber = reqCtrlData.get(i).getSequenceQRNumber();
                    stickersDialogDataList = reqCtrlData.get(i).getStickersDialogDataList();
                    for (ResponseBodyQRDetails response : responses) {
                        setTempData(response);
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    void setTempData(ResponseBodyQRDetails response) {
        String responseItemID = response.getItemId();
        for (int position = 0; position < commonReceivingShippingDetailDataLists.size(); position++) {
            String listRCDItemid = commonReceivingShippingDetailDataLists.get(position).getItemId();

            if (responseItemID.equals(listRCDItemid)) {

//                stickersDialogDataList.add(new StickersDialogData(qrDetails, response.getBatchId(), commonReceivingShippingDetailDataLists.get(position).getItemId()));

                String qrDataStickerqty = response.getStickerQty();
                commonReceivingShippingDetailDataLists.get(position).setConfig(response.getConfig());
                String[] expdate = response.getExpdate().split("\\s+");
                commonReceivingShippingDetailDataLists.get(position).setExpdate(expdate[0]);
                commonReceivingShippingDetailDataLists.get(position).setBatchId(response.getBatchId());

                if (commonReceivingShippingDetailDataLists.get(position).getpickededQty() == null) {
                    if (commonReceivingShippingDetailDataLists.get(position).getApprovedQty().equals(qrDataStickerqty)) {
                        commonReceivingShippingDetailDataLists.get(position).setremainingQty("0");
                        commonReceivingShippingDetailDataLists.get(position).setpickededQty(qrDataStickerqty);
                        commonReceivingShippingDetailDataLists.get(position).setReason("");
                        commonReceivingShippingDetailDataLists.get(position).setUnitId(response.getUnitId());

                        List<BatchNoList> batchNoLists = new ArrayList<>();
                        String batchNo = commonReceivingShippingDetailDataLists.get(position).getBatchId();
                        String batchQty = commonReceivingShippingDetailDataLists.get(position).getpickededQty();
                        String config = commonReceivingShippingDetailDataLists.get(position).getConfig();
                        if (commonReceivingShippingDetailDataLists.get(position).getBatchNoList() == null) {
                            BatchNoList batchNoList = new BatchNoList();
                            batchNoList.setBatchNo(batchNo);
                            batchNoList.setBatchQty(batchQty);
                            batchNoList.setConfig(config);
                            batchNoList.setBatchAutoIncreeId(response.getBatchAutoIncreeId());
                            batchNoLists.add(batchNoList);
                            commonReceivingShippingDetailDataLists.get(position).setBatchNoList(batchNoLists);
                        }

                        resetList(position);

                    } else {
                        float appqty = Float.parseFloat(commonReceivingShippingDetailDataLists.get(position).getApprovedQty());
                        float qrDataQtytemp = Float.parseFloat(qrDataStickerqty);
                        float qrDataQty = roundToPlaces(qrDataQtytemp, 3);
                        float remQty = roundToPlaces(appqty - qrDataQty, 3);

                        String test = String.format("%.03f", remQty);
                        commonReceivingShippingDetailDataLists.get(position).setUnitId(response.getUnitId());
                        if (remQty >= 0) {
                            commonReceivingShippingDetailDataLists.get(position).setremainingQty(test);
                            commonReceivingShippingDetailDataLists.get(position).setpickededQty(qrDataStickerqty);
                            if (remQty == 0) {
                                commonReceivingShippingDetailDataLists.get(position).setReason("");
                            }

                            String batchNo = commonReceivingShippingDetailDataLists.get(position).getBatchId();
                            String batchQty = commonReceivingShippingDetailDataLists.get(position).getpickededQty();
                            String config = commonReceivingShippingDetailDataLists.get(position).getConfig();
                            float stickQty = Float.parseFloat(response.getStickerQty());
                            List<BatchNoList> batchNoLists = new ArrayList<>();
                            if (commonReceivingShippingDetailDataLists.get(position).getBatchNoList() == null) {
                                BatchNoList batchNoList = new BatchNoList();
                                batchNoList.setBatchNo(batchNo);
                                batchNoList.setBatchQty(batchQty);
                                batchNoList.setConfig(config);
                                batchNoList.setBatchAutoIncreeId(response.getBatchAutoIncreeId());
                                batchNoLists.add(batchNoList);
                                commonReceivingShippingDetailDataLists.get(position).setBatchNoList(batchNoLists);

                            } else if (commonReceivingShippingDetailDataLists.get(position).getBatchNoList().size() == 0) {
                                BatchNoList batchNoList = new BatchNoList();
                                batchNoList.setBatchNo(batchNo);
                                batchNoList.setBatchQty(batchQty);
                                batchNoList.setConfig(config);
                                batchNoList.setBatchAutoIncreeId(response.getBatchAutoIncreeId());
                                batchNoLists.add(batchNoList);
                                commonReceivingShippingDetailDataLists.get(position).setBatchNoList(batchNoLists);

                            } else {
                                boolean batchB = false;
                                boolean confB = false;
                                if (commonReceivingShippingDetailDataLists.get(position).getBatchNoList() != null) {


                                    for (int j = 0; j < commonReceivingShippingDetailDataLists.get(position).getBatchNoList().size(); j++) {
                                        String batch = commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(j).getBatchNo();
                                        if (batchNo.equals(batch)) {
                                            batchB = true;
                                            for (int k = 0; k < commonReceivingShippingDetailDataLists.get(position).getBatchNoList().size(); k++) {
                                                String configur = commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(k).getConfig();
                                                if (configur.equals(response.getConfig())) {
                                                    String confBatch = commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(k).getBatchNo();
                                                    if (batch.equals(confBatch)) {
                                                        float prevBatchQty = Float.parseFloat(commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(k).getBatchQty());
                                                        float total = prevBatchQty + stickQty;
                                                        commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(k).setBatchQty(String.valueOf(total));
                                                        confB = true;

                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if (confB == false) {
                                        BatchNoList batchNoList = new BatchNoList();
                                        batchNoList.setBatchNo(batchNo);
                                        batchNoList.setBatchQty(String.valueOf(stickQty));
                                        batchNoList.setConfig(config);
                                        batchNoList.setBatchAutoIncreeId(response.getBatchAutoIncreeId());
                                        batchNoLists.add(batchNoList);
                                        commonReceivingShippingDetailDataLists.get(position).getBatchNoList().add(batchNoList);
                                        resetList(position);
                                        break;
                                    }

                                    if (batchB == false) {
                                        BatchNoList batchNoList = new BatchNoList();
                                        batchNoList.setBatchNo(batchNo);
                                        batchNoList.setBatchQty(String.valueOf(stickQty));
                                        batchNoList.setConfig(config);
                                        batchNoList.setBatchAutoIncreeId(response.getBatchAutoIncreeId());
                                        batchNoLists.add(batchNoList);
                                        commonReceivingShippingDetailDataLists.get(position).getBatchNoList().add(batchNoList);
                                        resetList(position);
                                        break;
                                    }
                                }
                            }

                            resetList(position);
                        } else {
                            Toast.makeText(RequisitionControlDetails.this, "Picked Qty. is not more then Approved Qty.", Toast.LENGTH_SHORT).show();
                            evergreenLoop();
                            scanQR.setText("");
                        }

                    }
                } else {
                    float prePickedQty = Float.parseFloat(commonReceivingShippingDetailDataLists.get(position).getpickededQty());
                    float QRDataStickerqty = Float.parseFloat(qrDataStickerqty);
                    float totalpickedQty = roundToPlaces(prePickedQty + QRDataStickerqty, 3);
                    float approvedQty = Float.parseFloat(commonReceivingShippingDetailDataLists.get(position).getApprovedQty());

                    if (approvedQty == totalpickedQty) {
                        commonReceivingShippingDetailDataLists.get(position).setremainingQty("0");
                        String tempPickQty = String.format("%.03f", totalpickedQty);
                        commonReceivingShippingDetailDataLists.get(position).setpickededQty(tempPickQty);
                        commonReceivingShippingDetailDataLists.get(position).setReason("");
                        commonReceivingShippingDetailDataLists.get(position).setUnitId(response.getUnitId());
//                                            spinner_reason.setEnabled(false);

                        List<BatchNoList> batchNoLists = new ArrayList<>();
                        String batchNo = commonReceivingShippingDetailDataLists.get(position).getBatchId();
                        String batchQty = commonReceivingShippingDetailDataLists.get(position).getpickededQty();
                        float stickQty = Float.parseFloat(response.getStickerQty());
                        String config = commonReceivingShippingDetailDataLists.get(position).getConfig();
                        if (commonReceivingShippingDetailDataLists.get(position).getBatchNoList() == null) {
                            BatchNoList batchNoList = new BatchNoList();
                            batchNoList.setBatchNo(batchNo);
                            batchNoList.setBatchQty(batchQty);
                            batchNoList.setConfig(config);
                            batchNoList.setBatchAutoIncreeId(response.getBatchAutoIncreeId());
                            batchNoLists.add(batchNoList);
                            commonReceivingShippingDetailDataLists.get(position).setBatchNoList(batchNoLists);

                        } else if (commonReceivingShippingDetailDataLists.get(position).getBatchNoList().size() == 0) {
                            BatchNoList batchNoList = new BatchNoList();
                            batchNoList.setBatchNo(batchNo);
                            batchNoList.setBatchQty(batchQty);
                            batchNoList.setConfig(config);
                            batchNoList.setBatchAutoIncreeId(response.getBatchAutoIncreeId());
                            batchNoLists.add(batchNoList);
                            commonReceivingShippingDetailDataLists.get(position).setBatchNoList(batchNoLists);

                        } else {
                            boolean batchB = false;
                            boolean confB = false;
                            if (commonReceivingShippingDetailDataLists.get(position).getBatchNoList() != null) {

                                for (int j = 0; j < commonReceivingShippingDetailDataLists.get(position).getBatchNoList().size(); j++) {
                                    String batch = commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(j).getBatchNo();
                                    if (batchNo.equals(batch)) {
                                        batchB = true;
                                        for (int k = 0; k < commonReceivingShippingDetailDataLists.get(position).getBatchNoList().size(); k++) {
                                            String configur = commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(k).getConfig();
                                            if (configur.equals(response.getConfig())) {
                                                String confBatch = commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(k).getBatchNo();
                                                if (batch.equals(confBatch)) {
                                                    float prevBatchQty = Float.parseFloat(commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(k).getBatchQty());
                                                    float total = prevBatchQty + stickQty;
                                                    commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(k).setBatchQty(String.valueOf(total));
                                                    confB = true;
                                                }
                                            }
                                        }
                                    }
                                }

                                if (confB == false) {
                                    BatchNoList batchNoList = new BatchNoList();
                                    batchNoList.setBatchNo(batchNo);
                                    batchNoList.setBatchQty(String.valueOf(stickQty));
                                    batchNoList.setConfig(config);
                                    batchNoList.setBatchAutoIncreeId(response.getBatchAutoIncreeId());
                                    batchNoLists.add(batchNoList);
                                    commonReceivingShippingDetailDataLists.get(position).getBatchNoList().add(batchNoList);
                                    resetList(position);
                                    break;
                                }

                                if (batchB == false) {
                                    BatchNoList batchNoList = new BatchNoList();
                                    batchNoList.setBatchNo(batchNo);
                                    batchNoList.setBatchQty(String.valueOf(stickQty));
                                    batchNoList.setConfig(config);
                                    batchNoList.setBatchAutoIncreeId(response.getBatchAutoIncreeId());
                                    batchNoLists.add(batchNoList);
                                    commonReceivingShippingDetailDataLists.get(position).getBatchNoList().add(batchNoList);
                                    resetList(position);
                                    break;
                                }
                            }
                        }
                        resetList(position);
                    } else {
                        float remqty = Float.parseFloat(commonReceivingShippingDetailDataLists.get(position).getApprovedQty()) - totalpickedQty;

                        String test = String.format("%.03f", remqty);

                        float stickQty = Float.parseFloat(response.getStickerQty());
                        commonReceivingShippingDetailDataLists.get(position).setUnitId(response.getUnitId());
                        if (remqty >= 0) {
                            commonReceivingShippingDetailDataLists.get(position).setremainingQty(test);
                            String tempPickQty = String.format("%.03f", totalpickedQty);
                            commonReceivingShippingDetailDataLists.get(position).setpickededQty(tempPickQty);

                            if (remqty == 0.0) {
                                commonReceivingShippingDetailDataLists.get(position).setReason("");
                            }

                            List<BatchNoList> batchNoLists = new ArrayList<>();
                            String batchNo = commonReceivingShippingDetailDataLists.get(position).getBatchId();
                            String batchQty = commonReceivingShippingDetailDataLists.get(position).getpickededQty();
                            String config = commonReceivingShippingDetailDataLists.get(position).getConfig();
                            if (commonReceivingShippingDetailDataLists.get(position).getBatchNoList() == null) {
                                BatchNoList batchNoList = new BatchNoList();
                                batchNoList.setBatchNo(batchNo);
                                batchNoList.setBatchQty(batchQty);
                                batchNoList.setConfig(config);
                                batchNoList.setBatchAutoIncreeId(response.getBatchAutoIncreeId());
                                batchNoLists.add(batchNoList);
                                commonReceivingShippingDetailDataLists.get(position).setBatchNoList(batchNoLists);

                            } else if (commonReceivingShippingDetailDataLists.get(position).getBatchNoList().size() == 0) {
                                BatchNoList batchNoList = new BatchNoList();
                                batchNoList.setBatchNo(batchNo);
                                batchNoList.setBatchQty(batchQty);
                                batchNoList.setConfig(config);
                                batchNoList.setBatchAutoIncreeId(response.getBatchAutoIncreeId());
                                batchNoLists.add(batchNoList);
                                commonReceivingShippingDetailDataLists.get(position).setBatchNoList(batchNoLists);

                            } else {
//                                                    boolean confBatchB = false;
                                boolean batchB = false;
                                boolean confB = false;
                                if (commonReceivingShippingDetailDataLists.get(position).getBatchNoList() != null) {

                                    for (int j = 0; j < commonReceivingShippingDetailDataLists.get(position).getBatchNoList().size(); j++) {
                                        String batch = commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(j).getBatchNo();
                                        if (batchNo.equals(batch)) {
                                            batchB = true;
                                            for (int k = 0; k < commonReceivingShippingDetailDataLists.get(position).getBatchNoList().size(); k++) {
                                                String configur = commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(k).getConfig();
                                                if (configur.equals(response.getConfig())) {
                                                    String confBatch = commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(k).getBatchNo();
                                                    if (batch.equals(confBatch)) {
                                                        float prevBatchQty = Float.parseFloat(commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(k).getBatchQty());
                                                        float total = prevBatchQty + stickQty;
                                                        commonReceivingShippingDetailDataLists.get(position).getBatchNoList().get(k).setBatchQty(String.valueOf(total));
                                                        confB = true;
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if (confB == false) {
                                        BatchNoList batchNoList = new BatchNoList();
                                        batchNoList.setBatchNo(batchNo);
                                        batchNoList.setBatchQty(String.valueOf(stickQty));
                                        batchNoList.setConfig(config);
                                        batchNoList.setBatchAutoIncreeId(response.getBatchAutoIncreeId());
                                        batchNoLists.add(batchNoList);
                                        commonReceivingShippingDetailDataLists.get(position).getBatchNoList().add(batchNoList);
                                        resetList(position);
                                        break;
                                    }

                                    if (batchB == false) {
                                        BatchNoList batchNoList = new BatchNoList();
                                        batchNoList.setBatchNo(batchNo);
                                        batchNoList.setBatchQty(String.valueOf(stickQty));
                                        batchNoList.setConfig(config);
                                        batchNoList.setBatchAutoIncreeId(response.getBatchAutoIncreeId());
                                        batchNoLists.add(batchNoList);
                                        commonReceivingShippingDetailDataLists.get(position).getBatchNoList().add(batchNoList);
                                        resetList(position);
                                        break;
                                    }
                                }
                            }
                            resetList(position);
                        } else {
                            Toast.makeText(RequisitionControlDetails.this, "Picked Qty. is not more then Approved Qty.", Toast.LENGTH_SHORT).show();
                            evergreenLoop();
                            scanQR.setText("");
                        }
                    }
                }

            }
        }
    }

    void resetList(int position) {
        commonReceivingShippingDetailDataList tempRcdDetail = new commonReceivingShippingDetailDataList();
        tempRcdDetail = commonReceivingShippingDetailDataLists.get(position);
        commonReceivingShippingDetailDataLists.remove(position);
        commonReceivingShippingDetailDataLists.add(0, tempRcdDetail);
        scanQR.setText("");
    }

    private void deleteTempData() {
        ArrayList<RequisitionWiseQRDetail> reqCtrlData = Preference.getInstance(getApplicationContext()).getReqCtrlData();

        if (reqCtrlData != null) {
            for (int i = 0; i < reqCtrlData.size(); i++) {
                if (reqCtrlData.get(i).getRequisitionNo().equals(reqNmbr)) {
                    reqCtrlData.remove(i);
                    break;
                }
            }
        }
        Preference.getInstance(getApplicationContext()).saveReqCtrlData(reqCtrlData);
    }

}