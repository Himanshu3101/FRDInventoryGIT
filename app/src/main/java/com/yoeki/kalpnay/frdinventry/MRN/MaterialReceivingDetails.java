package com.yoeki.kalpnay.frdinventry.MRN;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.AdapterListUpdateCallback;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
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
import com.google.zxing.integration.android.IntentResult;
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
import com.yoeki.kalpnay.frdinventry.ScannigQR;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    List<MRNDetailsList> listMRNDetailsList;
    private IntentIntegrator qrScan;

    MRN_Dashboard_AdapterDetails adapter;
    List<StickerSeq> tempSeqList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_receiving_details);
        initialize();

//        mrn_dashboard_adapterDetails = new MRN_Dashboard_AdapterDetails(this);
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
//        scanQR();

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
//                if(tempSeq.equals("QR-000001171")){
////                    for (int i = 0; i < listMRNDetailsList.size(); i++) {
////
////                    }
//                    listMRNDetailsList.get(0).setScanQty("5.000");
//                    adapter.notifyDataSetChanged();
//                    scanQRMRD.setText("");
//
////
////
////
////                    listMRNDetailsList.get(i).setScanQty("");
//                }else{
//                    try {
                tempSeq = scanQRMRD.getText().toString();
                String qty, newQty;
                boolean isQrFound = false;

                if (listMRNDetailsList.size() > 0) {
                    for (int i = 0; i < listMRNDetailsList.size(); i++) {
                        boolean breakFlag = false;
                        boolean isSeqExistsInTempList = false;
//                        int size = listMRNDetailsList.size();
//                        Log.d("aaaa",size+"" );

                        if (listMRNDetailsList.get(i).getStickerSeq() == null) {
//                                    Toast.makeText(MaterialReceivingDetails.this, "Sticker Sequence is empty", Toast.LENGTH_SHORT).show();
                        } else {
                            for (int j = 0; j < listMRNDetailsList.get(i).getStickerSeq().size(); j++) {
//                            String a = listMRNDetailsList.get(i).getStickerSeq().get(j).getStickerSequence();
//                            Log.d("aaaa",a );
//                            int sizea = listMRNDetailsList.get(i).getStickerSeq().size();
//                            Log.d("aaaa",sizea+"" );
                                if (listMRNDetailsList.get(i).getStickerSeq().get(j).getStickerSequence().equals(tempSeq)) {
                                    breakFlag = true;
                                    for (int k = 0; k < tempSeqList.size(); k++) {
                                        if (tempSeqList.get(k).getStickerSequence().equals(tempSeq)) {
                                            isSeqExistsInTempList = true;
                                        }
                                    }
                                    if (!isSeqExistsInTempList) {
                                        tempSeqList.add(listMRNDetailsList.get(i).getStickerSeq().get(j));

                                        newQty = listMRNDetailsList.get(i).getStickerSeq().get(j).getStickerQty();
                                        if (listMRNDetailsList.get(i).getScanQty() == null) {
                                            listMRNDetailsList.get(i).setScanQty("0");
                                        }
                                        qty = listMRNDetailsList.get(i).getScanQty();

                                        float numQty = Float.parseFloat(qty) + Float.parseFloat(newQty);

                                        listMRNDetailsList.get(i).setScanQty(numQty + "");

                                        adapter.notifyDataSetChanged();
                                        scanQRMRD.setText("");
//                                    if (isRecQtyScanQtyMatched()) {
//                                        postingBtn.setEnabled(true);
//                                    } else {
//                                        postingBtn.setEnabled(false);
//                                    }

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
//                    }catch(Exception e){
//                        e.printStackTrace();
//                        scanQRMRD.setText("");
//                        invalidItemDialog();
//                    }
            }


//                }
        };
        scanQRMRD.addTextChangedListener(textWatcher);


    }

    public void initialize() {
        rcy_itemsMRD = findViewById(R.id.rcy_itemsMRD);
        scanQRMRD = findViewById(R.id.scanQRMRD);
        postingBtn = findViewById(R.id.postingBtn);
        saveTempBtn = findViewById(R.id.saveTempBtn);
        img_backMRNDetails = findViewById(R.id.img_backMRNDetails);
        languageChange = findViewById(R.id.languageChange);
//        tv_scanqty = findViewById(R.id.tv_scanqty);
        //scanQR = findViewById(R.id.scanQR);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_backMRNDetails:
                onBackDialog();
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
                postingBtnClick();
                break;
            case R.id.saveTempBtn:

                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        onBackDialog();
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
                progressDialog.dismiss();
                if (response.body() != null) {
                    if (response.body().getStatus().equals("Success")) {
                        listMRNDetailsList = response.body().getMRNDetailsList();
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        adapter = new MRN_Dashboard_AdapterDetails(MaterialReceivingDetails.this, listMRNDetailsList);
                        rcy_itemsMRD.setLayoutManager(layoutManager);
                        rcy_itemsMRD.setAdapter(adapter);
                    } else {
                        Toast.makeText(MaterialReceivingDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MaterialReceivingDetails.this, "An Error has occured.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MRN_Dashboard.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<MrnNumberDetailResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MaterialReceivingDetails.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (result != null) {
//            //if qrcode has nothing in it
//            if (result.getContents() == null) {
//                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
//            } else {
//                //if qr contains data
//                //converting the data to json
//                String tempSeq = result.getContents();
//                //String tempSeq = "QR-000000002";
//                //setting values to textviews
//
//                scanQRMRD.getText().clear();
//                scanQRMRD.setText(tempSeq);
//
//                String qty, newQty;
//
//                if (listMRNDetailsList.size() > 0) {
//                    for (int i = 0; i < listMRNDetailsList.size(); i++) {
//                        boolean breakFlag = false;
//                        boolean isSeqExistsInTempList = false;
////                        int size = listMRNDetailsList.size();
////                        Log.d("aaaa",size+"" );
//
//                        for (int j = 0; j < listMRNDetailsList.get(i).getStickerSeq().size(); j++) {
////                            String a = listMRNDetailsList.get(i).getStickerSeq().get(j).getStickerSequence();
////                            Log.d("aaaa",a );
////                            int sizea = listMRNDetailsList.get(i).getStickerSeq().size();
////                            Log.d("aaaa",sizea+"" );
//                            if (listMRNDetailsList.get(i).getStickerSeq().get(j).getStickerSequence().equals(tempSeq)) {
//                                breakFlag = true;
//                                for (int k = 0; k < tempSeqList.size(); k++) {
//                                    if (tempSeqList.get(k).getStickerSequence().equals(tempSeq)) {
//                                        isSeqExistsInTempList = true;
//                                    }
//                                }
//                                if (!isSeqExistsInTempList) {
//                                    tempSeqList.add(listMRNDetailsList.get(i).getStickerSeq().get(j));
//
//                                    newQty = listMRNDetailsList.get(i).getStickerSeq().get(j).getStickerQty();
//                                    if (listMRNDetailsList.get(i).getScanQty() == null) {
//                                        listMRNDetailsList.get(i).setScanQty("0");
//                                    }
//                                    qty = listMRNDetailsList.get(i).getScanQty();
//
//                                    float numQty = Float.parseFloat(qty) + Float.parseFloat(newQty);
//
//                                    listMRNDetailsList.get(i).setScanQty(numQty + "");
//
//                                    adapter.notifyDataSetChanged();
//
////                                    if (isRecQtyScanQtyMatched()) {
////                                        postingBtn.setEnabled(true);
////                                    } else {
////                                        postingBtn.setEnabled(false);
////                                    }
//
//                                } else {
//                                    Toast.makeText(this, "QR already scanned!", Toast.LENGTH_SHORT).show();
//                                }
//                                break;
//                            }
//                        }
//                        if (breakFlag) {
//                            break;
//                        }
//                    }
//                }
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

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
                    progressDialog.dismiss();
                    if (response.body().getStatus().equals("Success")) {
                        Toast.makeText(MaterialReceivingDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MRN_Dashboard.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MaterialReceivingDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PostingJsonResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(MaterialReceivingDetails.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Scan all items first", Toast.LENGTH_SHORT).show();
        }
    }

    public void invalidItemDialog() {
        final Dialog dialog = new Dialog(MaterialReceivingDetails.this);
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
//lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
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
}
