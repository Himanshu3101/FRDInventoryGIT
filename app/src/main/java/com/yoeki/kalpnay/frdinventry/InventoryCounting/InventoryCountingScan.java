package com.yoeki.kalpnay.frdinventry.InventoryCounting;

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
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Toast;

import com.yoeki.kalpnay.frdinventry.Api.Api;
import com.yoeki.kalpnay.frdinventry.Api.ApiInterface;
import com.yoeki.kalpnay.frdinventry.Api.Preference;
import com.yoeki.kalpnay.frdinventry.MRN.Model.PostingJsonResponse;
import com.yoeki.kalpnay.frdinventry.QRDetails.RequestBodyQRDetails;
import com.yoeki.kalpnay.frdinventry.QRDetails.ResponseBodyQRDetails;
import com.yoeki.kalpnay.frdinventry.R;
import com.yoeki.kalpnay.frdinventry.Dashboard.dashboardNew;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventoryCountingScan extends AppCompatActivity implements View.OnClickListener {
//    CheckBox checkUpdateQtyInventoryCountngScan;
    AppCompatAutoCompleteTextView scanQRInventoryCountngScan;
    AppCompatButton img_backInventoryCountngScan, languageChange_IVC, submitBtnIVC;
    RecyclerView rcy_itemsInventoryCountngScan;
    List<String> listSequenceQr;
    List<ResponseBodyQRDetails> listIVC, saveInventoryCountingList;
    List<String> batchListeds;
    String journal, wareHouse, qrScanningDetails;
    int languageChangeVisible = 1;
    AdapterDetailsIVC adapterIVC;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_counting_scan);
        initialize();
        listIVC = new ArrayList<>();
        saveInventoryCountingList =new ArrayList<>();
        try{
            saveInventoryCountingList = Preference.getInstance(getApplicationContext()).getInvenrtoryCounting();
        }catch(Exception e){
            e.printStackTrace();
        }
        journal = getIntent().getStringExtra("journal");
        wareHouse = getIntent().getStringExtra("wareHouse");
        batchListeds = new ArrayList<>();
        listSequenceQr = new ArrayList<>();
//        checkUpdateQtyInventoryCountngScan.setOnClickListener(this);
        img_backInventoryCountngScan.setOnClickListener(this);
        languageChange_IVC.setOnClickListener(this);
        submitBtnIVC.setOnClickListener(this);
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
                qrScanningDetails = String.valueOf(editable);
                if (qrScanningDetails.length() >= 12) {
                    qrDetails();
                }
            }
        };
        scanQRInventoryCountngScan.addTextChangedListener(textWatcher);
    }

    public void qrDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(InventoryCountingScan.this);
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        progressDialog.setCancelable(false); // set cancelable to false
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        String qr = scanQRInventoryCountngScan.getText().toString();
        RequestBodyQRDetails requestBodyQRDetails = new RequestBodyQRDetails(qr);
        Call<ResponseBodyQRDetails> call = apiInterface.QRWiseData(requestBodyQRDetails);
        call.enqueue(new Callback<ResponseBodyQRDetails>() {
            public void onResponse(Call<ResponseBodyQRDetails> call, Response<ResponseBodyQRDetails> response) {
                try {
                    progressDialog.dismiss();
                    if (response.body().getStatus().equals("Success")) {
                        if(listSequenceQr.size()==0){
                            listSequenceQr.add(qrScanningDetails);
                            scanQRAndSetLayout(response);
                        }else if(listSequenceQr.contains(qrScanningDetails)){
                            Toast.makeText(InventoryCountingScan.this, "QR is already Scanned. ", Toast.LENGTH_SHORT).show();
                            scanQRInventoryCountngScan.setText("");
                        }else {
                            listSequenceQr.add(qrScanningDetails);
                            scanQRAndSetLayout(response);
                        }
                    } else {
                        invalidItemDialog();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(InventoryCountingScan.this, "Service Unavailable.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBodyQRDetails> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(InventoryCountingScan.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initialize() {
        scanQRInventoryCountngScan = findViewById(R.id.scanQRInventoryCountngScan);
        img_backInventoryCountngScan = findViewById(R.id.img_backInventoryCountngScan);
//        checkUpdateQtyInventoryCountngScan = findViewById(R.id.checkUpdateQtyInventoryCountngScan);
        submitBtnIVC = findViewById(R.id.submitBtnIVC);
        rcy_itemsInventoryCountngScan = findViewById(R.id.rcy_itemsInventoryCountngScan);
        languageChange_IVC = findViewById(R.id.languageChange_IVC);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_backQrDetails:
                Intent intent = new Intent(getApplicationContext(), inventory_Counting.class);
                startActivity(intent);
                finish();
                break;
            case R.id.languageChange_IVC:
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                adapterIVC = new AdapterDetailsIVC(InventoryCountingScan.this, listIVC, wareHouse, languageChangeVisible);
                rcy_itemsInventoryCountngScan.setLayoutManager(layoutManager);
                rcy_itemsInventoryCountngScan.setAdapter(adapterIVC);
                if (languageChangeVisible == 0) {
                    languageChangeVisible = 1;
                } else {
                    languageChangeVisible = 0;
                }
                break;
            case R.id.submitBtnIVC:
                if(listIVC.size()==0){
                    Toast.makeText(this, "Firstlt, Scan the items.", Toast.LENGTH_SHORT).show();
                }else {
                    submitIVCData();
                }
                break;
            case R.id.img_backInventoryCountngScan:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), inventory_Counting.class);
        startActivity(intent);
        finish();
    }

    public void scanQRAndSetLayout(Response<ResponseBodyQRDetails> response){
            String configuration = response.body().getConfig();
            String batchID = response.body().getBatchId();
            String pckQty= response.body().getStickerQty();
          /*  if(checkUpdateQtyInventoryCountngScan.isChecked()){
                update_Qty(configuration,batchID,pckQty,response);
            }else{*/
                if (batchListeds.size() == 0) {
                    batchListeds.add(0,batchID);
                    listIVC.add(response.body());
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    adapterIVC = new AdapterDetailsIVC(InventoryCountingScan.this, listIVC, wareHouse);
                    rcy_itemsInventoryCountngScan.setLayoutManager(layoutManager);
                    rcy_itemsInventoryCountngScan.setAdapter(adapterIVC);
                    adapterIVC.notifyDataSetChanged();
                    scanQRInventoryCountngScan.setText("");
                } else {
                    if (!batchListeds.contains(response.body().getBatchId())) {
                        listIVC.add(0,response.body());
                        batchListeds.add(0,batchID);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        adapterIVC = new AdapterDetailsIVC(InventoryCountingScan.this, listIVC, wareHouse);
                        rcy_itemsInventoryCountngScan.setLayoutManager(layoutManager);
                        rcy_itemsInventoryCountngScan.setAdapter(adapterIVC);
                        adapterIVC.notifyDataSetChanged();
                        scanQRInventoryCountngScan.setText("");
                    }else if(batchListeds.contains(response.body().getBatchId())){
                        int i = batchListeds.indexOf(response.body().getBatchId());
                        String config = listIVC.get(i).getConfig();
                        if(config.equals(configuration)){
                            float previousQty = Float.parseFloat(listIVC.get(i).getStickerQty());
                            float cmngQty = Float.parseFloat(response.body().getStickerQty());
                            float finalQty = previousQty+cmngQty;
                            listIVC.get(i).setStickerQty(String.valueOf(finalQty));
                            //for batch list
                            String tempbatchListeds = batchListeds.get(i);
                            batchListeds.remove(i);
                            batchListeds.add(0,tempbatchListeds);
                            //for item list
                            ResponseBodyQRDetails tempIvcDetail = new ResponseBodyQRDetails();
                            tempIvcDetail = listIVC.get(i);
                            listIVC.remove(i);
                            adapterIVC.notifyItemRemoved(i);
                            listIVC.add(0, tempIvcDetail);
                            adapterIVC.notifyItemInserted(0);
                            adapterIVC.notifyDataSetChanged();
                            rcy_itemsInventoryCountngScan.smoothScrollToPosition(0);
                            scanQRInventoryCountngScan.setText("");
                        }
                    }
                }
//            }
        }

    public void update_Qty(final String configuration, final String batchID, String pckQty, final Response<ResponseBodyQRDetails> response) {
        final Dialog dialog = new Dialog(InventoryCountingScan.this);
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
                for(int i = 0; i<=listSequenceQr.size() - 1;i++){
                    String qrNUmber = listSequenceQr.get(i);
                    if(qrNUmber.equals(qrScanningDetails)){
                        listSequenceQr.remove(i);
                    }
                }
                dialog.dismiss();
                scanQRInventoryCountngScan.setText("");
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float packed = Float.parseFloat(pckd_qty.getText().toString());
                float update = Float.parseFloat(updte_qty.getText().toString());
                if (update==0) {
                    Toast.makeText(InventoryCountingScan.this, "Please Input Update Quantity.", Toast.LENGTH_SHORT).show();
                } else {
                    float UpdateQty = packed * update;
                    if (batchListeds.size() == 0) {
                        batchListeds.add(0,batchID);
                        listIVC.add(response.body());
                        listIVC.get(0).setStickerQty(String.valueOf(UpdateQty));
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        adapterIVC = new AdapterDetailsIVC(InventoryCountingScan.this, listIVC, wareHouse);
                        rcy_itemsInventoryCountngScan.setLayoutManager(layoutManager);
                        rcy_itemsInventoryCountngScan.setAdapter(adapterIVC);
                        adapterIVC.notifyDataSetChanged();
                        scanQRInventoryCountngScan.setText("");
                    } else {
                        if (!batchListeds.contains(response.body().getBatchId())) {
                            listIVC.add(0,response.body());
                            batchListeds.add(0,batchID);
                            listIVC.get(0).setStickerQty(String.valueOf(UpdateQty));
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            adapterIVC = new AdapterDetailsIVC(InventoryCountingScan.this, listIVC, wareHouse);
                            rcy_itemsInventoryCountngScan.setLayoutManager(layoutManager);
                            rcy_itemsInventoryCountngScan.setAdapter(adapterIVC);
                            adapterIVC.notifyDataSetChanged();
                            scanQRInventoryCountngScan.setText("");
                        }else if(batchListeds.contains(response.body().getBatchId())){
                            int i = batchListeds.indexOf(response.body().getBatchId());
                            String config = listIVC.get(i).getConfig();
                            if(config.equals(configuration)){
                                float previousQty = Float.parseFloat(listIVC.get(i).getStickerQty());
                                float finalQty = previousQty+UpdateQty;
                                listIVC.get(i).setStickerQty(String.valueOf(finalQty));
                                //for batch list
                                String tempbatchListeds = batchListeds.get(i);
                                batchListeds.remove(i);
                                batchListeds.add(0,tempbatchListeds);
                                //for item list
                                ResponseBodyQRDetails tempIvcDetail = new ResponseBodyQRDetails();
                                tempIvcDetail = listIVC.get(i);
                                listIVC.remove(i);
                                adapterIVC.notifyItemRemoved(i);
                                listIVC.add(0, tempIvcDetail);
                                adapterIVC.notifyItemInserted(0);
                                adapterIVC.notifyDataSetChanged();
                                rcy_itemsInventoryCountngScan.smoothScrollToPosition(0);
                                scanQRInventoryCountngScan.setText("");
                            }
                        }
                    }
                    dialog.dismiss();
                }
            }
        });
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    public void invalidItemDialog() {
        final Dialog dialog = new Dialog(InventoryCountingScan.this);
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
                scanQRInventoryCountngScan.setText("");
                dialog.dismiss();
            }
        });
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    public void submitIVCData() {
        final ProgressDialog progressDialog = new ProgressDialog(InventoryCountingScan.this);
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        progressDialog.setCancelable(false); // set cancelable to false
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        String userId = Preference.getInstance(getApplicationContext()).getUserId();
        RequestBodyIVCDetails requestBodyIVCDetails = new RequestBodyIVCDetails(journal,userId,wareHouse,listIVC);
        Call<PostingJsonResponse> call = apiInterface.inventoryCountingLine(requestBodyIVCDetails);
        call.enqueue(new Callback<PostingJsonResponse>() {
            @Override
            public void onResponse(Call<PostingJsonResponse> call, Response<PostingJsonResponse> response) {
                progressDialog.dismiss();
                if (response.body().getStatus().equals("Success")) {

                    for(int i=0;i<=listIVC.size()-1;i++){
                        listIVC.get(i).setJournal(journal);
                        listIVC.get(i).setWareHouse(wareHouse);
                    }

                    try{
                        if(saveInventoryCountingList==null){
                            List<ResponseBodyQRDetails> saveInventoryCountingList =new ArrayList<>();
                            for(int i = 0;i<=listIVC.size()-1;i++){
                                saveInventoryCountingList.add(listIVC.get(i));
                            }
                            Preference.getInstance(getApplicationContext()).saveInvenrtoryCounting(saveInventoryCountingList);
                        }else{
                            int size = saveInventoryCountingList.size();
                            for(int i = 0;i<=listIVC.size()-1;i++){
                                saveInventoryCountingList.add(size,listIVC.get(i));
                                size++;
                            }
                            Preference.getInstance(getApplicationContext()).saveInvenrtoryCounting(saveInventoryCountingList);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Toast.makeText(InventoryCountingScan.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), dashboardNew.class);
                    startActivity(intent);
                    finish();
                }else{
                    alertDialog(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<PostingJsonResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(InventoryCountingScan.this, "Time Out. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
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
}