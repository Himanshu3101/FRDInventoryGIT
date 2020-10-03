package com.yoeki.kalpnay.frdinventry.InventoryCountingNew;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yoeki.kalpnay.frdinventry.Api.Api;
import com.yoeki.kalpnay.frdinventry.Api.ApiInterface;
import com.yoeki.kalpnay.frdinventry.Api.Preference;
import com.yoeki.kalpnay.frdinventry.InventoryCounting.AdapterINVCountingTempDetail;
import com.yoeki.kalpnay.frdinventry.InventoryCountingNew.Adapter.InventoryCountingDetailAdapter;
import com.yoeki.kalpnay.frdinventry.InventoryCountingNew.model.QrDetail;
import com.yoeki.kalpnay.frdinventry.InventoryCountingNew.model.QrScanResponse;
import com.yoeki.kalpnay.frdinventry.InventoryCountingNew.model.ScanQrRequest;
import com.yoeki.kalpnay.frdinventry.InventoryCountingNew.model.StickerSeqList;
import com.yoeki.kalpnay.frdinventry.InventoryCountingNew.model.SubmitPauseRequest;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.StickerList;
import com.yoeki.kalpnay.frdinventry.MRN.Model.PostingJsonResponse;
import com.yoeki.kalpnay.frdinventry.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventoryCountingDetail extends AppCompatActivity implements View.OnClickListener {

    AppCompatButton submitButton, pauseButton, languageChangeButton, backButton;
    AppCompatEditText editTextScanQr;
    RecyclerView recyclerViewCountingDetail;
    String userId;
    private String qrCode = "";
    String[] qrArr;
    private String sid, scheduleNo, wareHouseNo;
    ArrayList<QrDetail> itemList = new ArrayList<>();
    private InventoryCountingDetailAdapter inventoryCountingDetailAdapter;
    private int languageChangeValue = 0;
    private ArrayList<StickerSeqList> stickerSequenceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_counting_detail);
        userId = Preference.getInstance(getApplicationContext()).getUserId();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initViewsAndData();
    }

    void initViewsAndData() {
        sid = getIntent().getStringExtra("SID");
        scheduleNo = getIntent().getStringExtra("SCHEDULE_NO");
        wareHouseNo = getIntent().getStringExtra("WAREHOUSE_NO");

        backButton = findViewById(R.id.backButton);
        languageChangeButton = findViewById(R.id.languageChangeButton);
        pauseButton = findViewById(R.id.pauseButton);
        submitButton = findViewById(R.id.submitButton);
        editTextScanQr = findViewById(R.id.editTextScanQr);

        backButton.setOnClickListener(this);
        languageChangeButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);

        editTextScanQr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                qrCode = String.valueOf(s);
                if (!qrCode.contains(",")) {
                    if (qrCode.length() >= 8 && qrCode.length() <= 18) {
                        if (!qrFound(qrCode)) {
                            scanQR(qrCode, false);
                        } else {
                            editTextScanQr.setText("");
                            Toast.makeText(InventoryCountingDetail.this, "Sticker already scanned!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    qrArr = qrCode.split(",");
                    if (qrArr.length != 0) {
                        if (qrArr[0].length() >= 8 && qrArr[0].length() <= 18) {
                            if (!qrFound(qrArr[0])) {
                                scanQR(qrArr[0], true);
                            } else {
                                editTextScanQr.setText("");
                                Toast.makeText(InventoryCountingDetail.this, "Sticker already scanned!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });

        recyclerViewCountingDetail = findViewById(R.id.recyclerViewCountingDetail);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        inventoryCountingDetailAdapter = new InventoryCountingDetailAdapter
                (InventoryCountingDetail.this, itemList, wareHouseNo, languageChangeValue);
        recyclerViewCountingDetail.setLayoutManager(layoutManager);
        recyclerViewCountingDetail.setAdapter(inventoryCountingDetailAdapter);
    }

    boolean qrFound(String qrCode) {
        for (StickerSeqList list : stickerSequenceList) {
            if (list.getStickerSeq().equals(qrCode)) {
                return true;
            }
            if (list.getStickerSeqId().equals(qrCode)) {
                return true;
            }
        }
        return false;
    }

    void scanQR(final String qr, final boolean commaFound) {
        final ProgressDialog progressDialog = new ProgressDialog(InventoryCountingDetail.this);
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        progressDialog.setCancelable(false); // set cancelable to false
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        ScanQrRequest scanQrRequest = new ScanQrRequest(sid, scheduleNo, wareHouseNo, userId, qr);
        String json = new Gson().toJson(scanQrRequest);

        Call<QrScanResponse> call = apiInterface.scheduleScanActivity(scanQrRequest);
        call.enqueue(new Callback<QrScanResponse>() {
            @Override
            public void onResponse(Call<QrScanResponse> call, Response<QrScanResponse> response) {
                progressDialog.dismiss();
                editTextScanQr.setText("");
                try {
                    QrScanResponse body = response.body();
                    if (body.getStatus() != null || body.getMessage() != null) {
                        if (body.getStatus().equals("Success")) {
                            itemList.add(body.getDataList().get(0));
                            stickerSequenceList.add(new StickerSeqList(body.getDataList().get(0).getStickerSeq(),
                                    body.getDataList().get(0).getStickerSeqId()));
                            inventoryCountingDetailAdapter.notifyDataSetChanged();
                            if (commaFound) {
                                StringBuilder stickers = new StringBuilder();
                                for (int i = 1; i < qrArr.length; i++) {
                                    if (i == 1) {
                                        stickers.append(qrArr[i]);
                                    } else {
                                        stickers.append(", ").append(qrArr[i]);
                                    }
                                }
                                showNotScannedQr(stickers.toString());
                            }
                        } else {
                            Toast.makeText(InventoryCountingDetail.this, body.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(InventoryCountingDetail.this, "Invalid data found!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(InventoryCountingDetail.this, "Some problem occurred!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QrScanResponse> call, Throwable t) {
                progressDialog.dismiss();
                editTextScanQr.setText("");
                Toast.makeText(InventoryCountingDetail.this, "Some problem occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void showNotScannedQr(String stickers) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("These stickers are not scanned:");
        dialog.setMessage(stickers);
        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                Intent intent = new Intent(getApplicationContext(), InventoryCountingNew.class);
                startActivity(intent);
                finish();
                break;
            case R.id.languageChangeButton:
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                if (languageChangeValue == 0)
                    languageChangeValue = 1;
                else
                    languageChangeValue = 0;
                inventoryCountingDetailAdapter = new InventoryCountingDetailAdapter
                        (InventoryCountingDetail.this, itemList, wareHouseNo, languageChangeValue);
                recyclerViewCountingDetail.setLayoutManager(layoutManager);
                recyclerViewCountingDetail.setAdapter(inventoryCountingDetailAdapter);
                break;
            case R.id.pauseButton:
                onPauseButtonClick();
                break;
            case R.id.submitButton:
                onSubmitButtonClick();
                break;
        }
    }

    private void onSubmitButtonClick() {
        showSubmitAlert();
    }

    void showSubmitAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Do you want to submit this schedule?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                callSubmitPauseService("1");
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    private void onPauseButtonClick() {
        if (!itemList.isEmpty()) {
            showPauseAlert();
        } else {
            Toast.makeText(this, "Please scan at least one item!", Toast.LENGTH_SHORT).show();
        }
    }

    void showPauseAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Do you want to pause this schedule?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                callSubmitPauseService("0");
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), InventoryCountingNew.class);
        startActivity(intent);
        finish();
    }

    void callSubmitPauseService(String isSubmit) {
        final ProgressDialog progressDialog = new ProgressDialog(InventoryCountingDetail.this);
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        progressDialog.setCancelable(false); // set cancelable to false
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        SubmitPauseRequest submitPauseRequest = new SubmitPauseRequest(isSubmit, userId, scheduleNo, sid, wareHouseNo, stickerSequenceList);
        String json = new Gson().toJson(submitPauseRequest);

        Call<PostingJsonResponse> call = apiInterface.submitPauseSchedule(submitPauseRequest);
        call.enqueue(new Callback<PostingJsonResponse>() {
            @Override
            public void onResponse(Call<PostingJsonResponse> call, Response<PostingJsonResponse> response) {
                progressDialog.dismiss();
                PostingJsonResponse body = response.body();
                if (body.getStatus().equals("Success")) {
                    Toast.makeText(InventoryCountingDetail.this, body.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(InventoryCountingDetail.this, InventoryCountingNew.class));
                    finish();
                } else {
                    Toast.makeText(InventoryCountingDetail.this, body.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostingJsonResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(InventoryCountingDetail.this, "Some problem occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
