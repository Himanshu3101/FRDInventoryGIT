package com.yoeki.kalpnay.frdinventry.MRN;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.yoeki.kalpnay.frdinventry.Api.Api;
import com.yoeki.kalpnay.frdinventry.Api.ApiInterface;
import com.yoeki.kalpnay.frdinventry.Api.Preference;
import com.yoeki.kalpnay.frdinventry.MRN.Adapter.MRN_Dashboard_AdapterDetails;
import com.yoeki.kalpnay.frdinventry.MRN.Model.MRNDetailsList;
import com.yoeki.kalpnay.frdinventry.MRN.Model.MRNDetailsModel;
import com.yoeki.kalpnay.frdinventry.MRN.Model.MrnNumberDetailResponse;
import com.yoeki.kalpnay.frdinventry.MRN.Model.PostingJsonRequest;
import com.yoeki.kalpnay.frdinventry.MRN.Model.PostingJsonResponse;
import com.yoeki.kalpnay.frdinventry.MRN.Model.StickerSeq;
import com.yoeki.kalpnay.frdinventry.MRN.Model.mrnNumberDetailsRequest;
import com.yoeki.kalpnay.frdinventry.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaterialReceivingDetails extends AppCompatActivity implements View.OnClickListener {

    ArrayList<MRNDetailsModel> modelMRNDetails;
    AppCompatAutoCompleteTextView scanQRMRD;
    AppCompatButton postingBtn, saveTempBtn, img_backMRNDetails, languageChange;
    RecyclerView rcy_itemsMRD;
    String mrnNumber, activityNo;
    int languageChangeVisible = 1;
    List<MRNDetailsList> listMRNDetailsList = new ArrayList<>();
    private IntentIntegrator qrScan;
    MRN_Dashboard_AdapterDetails adapter;
    List<StickerSeq> tempSeqList = new ArrayList<>();
    boolean requiredBackDialog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_receiving_details);
        initUi();

        modelMRNDetails = new ArrayList<>();
        img_backMRNDetails.setOnClickListener(this);
        postingBtn.setOnClickListener(this);
        saveTempBtn.setOnClickListener(this);
        languageChange.setOnClickListener(this);
        qrScan = new IntentIntegrator(this);
        //scanQR.setOnClickListener(this);
        mrnNumber = getIntent().getStringExtra("MRNNumber");
        activityNo = getIntent().getStringExtra("activityNo");
        mrnDetailsDashboardData();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String tempSeq;
                tempSeq = scanQRMRD.getText().toString();
                String qty, newQty;
                boolean isQrFound = false;
                if (tempSeq.length() < 10) {

                } else {
                    if (listMRNDetailsList.size() > 0) {
                        for (int i = 0; i < listMRNDetailsList.size(); i++) {
                            boolean breakFlag = false;
                            boolean isSeqExistsInTempList = false;

                            if (listMRNDetailsList.get(i).getStickerSeq() == null) {
                            } else {
                                for (int j = 0; j < listMRNDetailsList.get(i).getStickerSeq().size(); j++) {
                                    if (listMRNDetailsList.get(i).getStickerSeq().get(j).getStickerSequence().equals(tempSeq)) {
                                        breakFlag = true;
                                        for (int k = 0; k < tempSeqList.size(); k++) {
                                            if (tempSeqList.get(k).getStickerSequence().equals(tempSeq)) {
                                                isSeqExistsInTempList = true;
                                            }
                                        }
                                        if (!isSeqExistsInTempList) {
                                            listMRNDetailsList.get(i).getStickerSeq().get(j).setMrnNo(mrnNumber);
                                            tempSeqList.add(listMRNDetailsList.get(i).getStickerSeq().get(j));

                                            requiredBackDialog = true;

                                            newQty = listMRNDetailsList.get(i).getStickerSeq().get(j).getStickerQty();
                                            if (listMRNDetailsList.get(i).getScanQty() == null) {
                                                listMRNDetailsList.get(i).setScanQty("0");
                                            }
                                            qty = listMRNDetailsList.get(i).getScanQty();

                                            float numQty = Float.parseFloat(qty) + Float.parseFloat(newQty);

                                            float recQty = Float.parseFloat(listMRNDetailsList.get(i).getReceivedQuantity());

                                            if (numQty > recQty) {
                                                Toast.makeText(MaterialReceivingDetails.this, "Scan Qty not be Greater then Received Qty.", Toast.LENGTH_SHORT).show();
//                                                invalidItemDialog();
                                            } else {
                                                listMRNDetailsList.get(i).setScanQty(roundToPlaces(numQty, 3) + "");
                                                MRNDetailsList tempMrnDetail = new MRNDetailsList();
                                                tempMrnDetail = listMRNDetailsList.get(i);
                                                listMRNDetailsList.remove(i);
                                                adapter.notifyItemRemoved(i);
                                                listMRNDetailsList.add(0, tempMrnDetail);
                                                adapter.notifyItemInserted(0);
                                                rcy_itemsMRD.smoothScrollToPosition(0);
                                            }
                                            scanQRMRD.setText("");
                                        } else {
                                            Toast.makeText(MaterialReceivingDetails.this, "QR already scanned!", Toast.LENGTH_SHORT).show();
                                            scanQRMRD.setText("");
                                        }
                                        isQrFound = true;
                                        break;
                                    } else {
                                        isQrFound = false;
                                    }
                                }
                            }
                            if (breakFlag) {
                                break;
                            }
                        }
                    }
                    if (!isQrFound) {
                        if (scanQRMRD.getText().toString().equals("")) {
                            //nothing do it
                        } else {
                            invalidItemDialog();
                        }
                    }
                }
            }
        };
        scanQRMRD.addTextChangedListener(textWatcher);
    }

    public void initUi() {
        rcy_itemsMRD = findViewById(R.id.rcy_itemsMRD);
        scanQRMRD = findViewById(R.id.scanQRMRD);
        postingBtn = findViewById(R.id.postingBtn);
        saveTempBtn = findViewById(R.id.saveTempBtn);
        img_backMRNDetails = findViewById(R.id.img_backMRNDetails);
        languageChange = findViewById(R.id.languageChange);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_backMRNDetails:
                if (requiredBackDialog) {
                    onBackDialog();
                } else {
                    Intent intent = new Intent(getApplicationContext(), MRN_Dashboard.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.languageChange:
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                MRN_Dashboard_AdapterDetails adapter = new MRN_Dashboard_AdapterDetails(MaterialReceivingDetails.this, listMRNDetailsList, languageChangeVisible);
                rcy_itemsMRD.setLayoutManager(layoutManager);
                rcy_itemsMRD.setAdapter(adapter);
                if (languageChangeVisible == 0) {
                    languageChangeVisible = 1;
                } else {
                    languageChangeVisible = 0;
                }
                break;
            case R.id.scanQR:
                qrScan.initiateScan();
                break;
            case R.id.postingBtn:
                try {
                    postingBtnClick();
                } catch (Exception e) {
                    Toast.makeText(this, "Service Unavailable", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.saveTempBtn:
                saveTempSeqList();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (requiredBackDialog) {
            onBackDialog();
        } else {
            Intent intent = new Intent(getApplicationContext(), MRN_Dashboard.class);
            startActivity(intent);
            finish();
        }
    }

    public void mrnDetailsDashboardData() {
        final ProgressDialog progressDialog = new ProgressDialog(MaterialReceivingDetails.this);
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        progressDialog.setCancelable(false); // set cancelable to false
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        String userId = Preference.getInstance(getApplicationContext()).getUserId();
        mrnNumberDetailsRequest detailsRequest = new mrnNumberDetailsRequest(mrnNumber, userId);

        Call<MrnNumberDetailResponse> call = apiInterface.getMRNDetails(detailsRequest);
        call.enqueue(new Callback<MrnNumberDetailResponse>() {
            @Override
            public void onResponse(Call<MrnNumberDetailResponse> call, Response<MrnNumberDetailResponse> response) {
                try {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("Success")) {
                            listMRNDetailsList = response.body().getMRNDetailsList();
                            initData();
                        } else {
                            Toast.makeText(MaterialReceivingDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MaterialReceivingDetails.this, "An Error has occured.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MRN_Dashboard.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MaterialReceivingDetails.this, "Service Unavailable.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MrnNumberDetailResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MaterialReceivingDetails.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    boolean isRecQtyScanQtyMatched() {
        boolean flag = false;
        for (int i = 0; i < listMRNDetailsList.size(); i++) {
            float recQty, scanQty;
            if (listMRNDetailsList.get(i).getScanQty() == null) {
                listMRNDetailsList.get(i).setScanQty("0");
            }

            recQty = Float.parseFloat(listMRNDetailsList.get(i).getReceivedQuantity());
            scanQty = Float.parseFloat(listMRNDetailsList.get(i).getScanQty());

            if (recQty != scanQty) {
                flag = false;
                break;
            } else {
                flag = true;
            }
        }
        return flag;
    }

    void postingBtnClick() {
        if (isRecQtyScanQtyMatched()) {
            final ProgressDialog progressDialog = new ProgressDialog(MaterialReceivingDetails.this);
            progressDialog.setMessage("Please Wait"); // set message
            progressDialog.show(); // show progress dialog
            progressDialog.setCancelable(false); // set cancelable to false
            ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
            PostingJsonRequest detailsRequest = new PostingJsonRequest(mrnNumber, activityNo);

            Call<PostingJsonResponse> call = apiInterface.mrnPosting(detailsRequest);
            call.enqueue(new Callback<PostingJsonResponse>() {
                @Override
                public void onResponse(Call<PostingJsonResponse> call, Response<PostingJsonResponse> response) {
                    try {
                        progressDialog.dismiss();
                        if (response.body().getStatus().equals("Success")) {
                            Toast.makeText(MaterialReceivingDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            deleteTempSeqData();
                            requiredBackDialog = false;
                            Intent intent = new Intent(getApplicationContext(), MRN_Dashboard.class);
                            startActivity(intent);
                            finish();
                        } else {
                            alertDialog(response.body().getMessage());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<PostingJsonResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    alertDialog(t.getMessage());
                }
            });
        } else {
            Toast.makeText(this, "Scan all items first", Toast.LENGTH_SHORT).show();
        }
    }

    private void alertDialog(String message) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage(message);
        dialog.setTitle("Error");
        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    public void invalidItemDialog() {
        final Dialog dialog = new Dialog(MaterialReceivingDetails.this);
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
                scanQRMRD.setText("");
                dialog.dismiss();
            }
        });

        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    public void onBackDialog() {
        final Dialog dialog = new Dialog(MaterialReceivingDetails.this);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.onback_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;

        AppCompatButton submitBtn = dialog.findViewById(R.id.submitBtn);
        AppCompatButton cancelBtn = dialog.findViewById(R.id.cancelBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), MRN_Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    private static BigDecimal roundToPlaces(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd;
    }

    void saveTempSeqList() {
        boolean mrnAvailable = false;
        for (int k = 0; k < tempSeqList.size(); k++) {
            if (tempSeqList.get(k).getMrnNo().equals(mrnNumber)) ;
            {
                mrnAvailable = true;
            }
        }
        if (mrnAvailable) {
            Preference.getInstance(getApplicationContext()).saveTempSeqList(tempSeqList);
            requiredBackDialog = false;
            Toast.makeText(this, "Temporary data saved successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MRN_Dashboard.class);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(this, "No scanned item found!", Toast.LENGTH_SHORT).show();
        }
    }

    void initData() {
        tempSeqList = Preference.getInstance(getApplicationContext()).getTempSeqList();
        if (tempSeqList != null) {
            if (tempSeqList.size() > 0) {
                for (int k = 0; k < tempSeqList.size(); k++) {
                    String tempSeq;
                    float newQty;
                    tempSeq = tempSeqList.get(k).getStickerSequence();
                    newQty = Float.parseFloat(tempSeqList.get(k).getStickerQty());
                    boolean breakFlag = false;
                    if (listMRNDetailsList.size() > 0) {
                        for (int i = 0; i < listMRNDetailsList.size(); i++) {
                            if (listMRNDetailsList.get(i).getStickerSeq() != null) {
                                for (int j = 0; j < listMRNDetailsList.get(i).getStickerSeq().size(); j++) {
                                    if (listMRNDetailsList.get(i).getStickerSeq().get(j).getStickerSequence().equals(tempSeq)) {
                                        breakFlag = true;
                                        if (listMRNDetailsList.get(i).getScanQty() == null) {
                                            listMRNDetailsList.get(i).setScanQty("0");
                                        }
                                        String qty = listMRNDetailsList.get(i).getScanQty();
                                        float numQty = Float.parseFloat(qty) + newQty;
                                        listMRNDetailsList.get(i).setScanQty(roundToPlaces(numQty, 3) + "");
                                        MRNDetailsList tempMrnDetail = new MRNDetailsList();
                                        tempMrnDetail = listMRNDetailsList.get(i);
                                        listMRNDetailsList.remove(i);
                                        listMRNDetailsList.add(0, tempMrnDetail);
                                        break;
                                    }
                                }
                            }
                            if (breakFlag) {
                                break;
                            }
                        }
                    }
                }

            }
        } else {
            tempSeqList = new ArrayList<>();
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        adapter = new MRN_Dashboard_AdapterDetails(MaterialReceivingDetails.this, listMRNDetailsList);
        rcy_itemsMRD.setLayoutManager(layoutManager);
        rcy_itemsMRD.setAdapter(adapter);
    }

    void deleteTempSeqData() {
        for (int i = 0; i < tempSeqList.size(); i++) {
            if (tempSeqList.get(i).getMrnNo().equals(mrnNumber)) {
                tempSeqList.remove(i);
            }
        }
        Preference.getInstance(getApplicationContext()).saveTempSeqList(tempSeqList);
        adapter.notifyDataSetChanged();
    }
}
