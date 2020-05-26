package com.yoeki.kalpnay.frdinventry.QRDetailsQuantityUpdate;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yoeki.kalpnay.frdinventry.Api.Api;
import com.yoeki.kalpnay.frdinventry.Api.ApiInterface;
import com.yoeki.kalpnay.frdinventry.Api.Preference;
import com.yoeki.kalpnay.frdinventry.Dashboard.dashboardNew;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.InventoryPendingModel;
import com.yoeki.kalpnay.frdinventry.MRN.Adapter.MRN_Dashboard_AdapterDetails;
import com.yoeki.kalpnay.frdinventry.MRN.MaterialReceivingDetails;
import com.yoeki.kalpnay.frdinventry.MRN.Model.PostingJsonResponse;
import com.yoeki.kalpnay.frdinventry.QRDetails.QrDetailActivity;
import com.yoeki.kalpnay.frdinventry.QRDetails.RequestBodyQRDetails;
import com.yoeki.kalpnay.frdinventry.QRDetails.ResponseBodyQRDetails;
import com.yoeki.kalpnay.frdinventry.R;
import com.yoeki.kalpnay.frdinventry.banchInventoryReceiving.InventoryControlDetail;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrDetailQuantityUpdate extends AppCompatActivity implements View.OnClickListener {
    AppCompatButton img_backQrQuantityUPdateDetails,update_QrQuantityUPdateDetails,saveTempBtn_qrQuantityUpdate;
    RecyclerView rcy_QrQuantityUPdateDetails;
    AppCompatAutoCompleteTextView edt_scanQR_QuantityUPdateDetails;
    ArrayList<String> sequenceQRNumber= new ArrayList<>();
    List<ResponseBodyQRDetails> responseList;
    List<String> sequenceList = new ArrayList<>();
    List<ResponseBodyQRDetails> saveInventoryCountingList;
    LinearLayoutManager layoutManager;
    String qrDetails, roleID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrquantity_updatedetails);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initUi();
        responseList = new ArrayList<>();
        img_backQrQuantityUPdateDetails.setOnClickListener(this);
        update_QrQuantityUPdateDetails.setOnClickListener(this);
        saveTempBtn_qrQuantityUpdate.setOnClickListener(this);
        layoutManager = new LinearLayoutManager(getApplicationContext());

        saveInventoryCountingList = new ArrayList<>();;
        saveInventoryCountingList = Preference.getInstance(getApplicationContext()).getTempQuantityUpdate();

        if(saveInventoryCountingList!=null && saveInventoryCountingList.size()!=0) {
            AdapterQrDetailQuantityUpdate adapterQrDetailQuantityUpdate = new AdapterQrDetailQuantityUpdate(QrDetailQuantityUpdate.this, saveInventoryCountingList);
            rcy_QrQuantityUPdateDetails.setLayoutManager(layoutManager);
            rcy_QrQuantityUPdateDetails.setAdapter(adapterQrDetailQuantityUpdate);
        }

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
                if (qrDetails.length() >= 12) {



                    if(saveInventoryCountingList==null) {
                        if (sequenceQRNumber.contains(qrDetails)) {
                            Toast.makeText(QrDetailQuantityUpdate.this, "QR is already Scanned.", Toast.LENGTH_SHORT).show();
                            edt_scanQR_QuantityUPdateDetails.setText("");
                        } else {
                            sequenceQRNumber.add(qrDetails);
                            qrDetailsMethod();
                        }
                    }else{
                        for(int i = 0;i<=saveInventoryCountingList.size()-1;i++){
                            sequenceList.add(i,saveInventoryCountingList.get(i).getSequenceNo());
                        }
                        if (sequenceList.contains(qrDetails)) {
                            Toast.makeText(QrDetailQuantityUpdate.this, "QR is already Scanned.", Toast.LENGTH_SHORT).show();
                            edt_scanQR_QuantityUPdateDetails.setText("");
                        } else {
                            if(sequenceQRNumber.contains(qrDetails)){
                                Toast.makeText(QrDetailQuantityUpdate.this, "QR is already Scanned.", Toast.LENGTH_SHORT).show();
                                edt_scanQR_QuantityUPdateDetails.setText("");
                            }else {
                                sequenceQRNumber.add(qrDetails);
                                qrDetailsMethod();
                            }
                        }
                    }
                }
            }
        };
        edt_scanQR_QuantityUPdateDetails.addTextChangedListener(textWatcher);

        roleID = Preference.getInstance(getApplicationContext()).getRole();
        if(roleID.equals("1")||roleID.equals("2")){
            saveTempBtn_qrQuantityUpdate.setVisibility(View.GONE);
            update_QrQuantityUPdateDetails.setVisibility(View.GONE);
        }
    }

    public void initUi() {
        img_backQrQuantityUPdateDetails = findViewById(R.id.img_backQrQuantityUPdateDetails);
        saveTempBtn_qrQuantityUpdate = findViewById(R.id.saveTempBtn_qrQuantityUpdate);
        edt_scanQR_QuantityUPdateDetails = findViewById(R.id.edt_scanQR_QuantityUPdateDetails);
        update_QrQuantityUPdateDetails = findViewById(R.id.update_QrQuantityUPdateDetails);
        rcy_QrQuantityUPdateDetails = findViewById(R.id.rcy_QrQuantityUPdateDetails);
    }

    public void qrDetailsMethod() {
        final ProgressDialog progressDialog = new ProgressDialog(QrDetailQuantityUpdate.this);
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        progressDialog.setCancelable(false); // set cancelable to false
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        String qr = edt_scanQR_QuantityUPdateDetails.getText().toString();
        RequestBodyQRDetails requestBodyQRDetails = new RequestBodyQRDetails(qr);
        Call<ResponseBodyQRDetails> call = apiInterface.QRWiseData(requestBodyQRDetails);
        call.enqueue(new Callback<ResponseBodyQRDetails>() {
            @Override
            public void onResponse(Call<ResponseBodyQRDetails> call, Response<ResponseBodyQRDetails> response) {
                try{
                    progressDialog.dismiss();
                    if (response.body().getStatus().equals("Success")) {
                        response.body().setSequenceNo(qrDetails);
                        updateQtyDialog(response.body());
                    } else {
                        invalidItemDialog();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(QrDetailQuantityUpdate.this, "Service Unavailable.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBodyQRDetails> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(QrDetailQuantityUpdate.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateQtyDialog(final ResponseBodyQRDetails response) {
        final Dialog dialog = new Dialog(QrDetailQuantityUpdate.this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_qrdetail_quantity_update);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;


//lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        AppCompatTextView detail_ExpiryDateDialogQRUpdate = dialog.findViewById(R.id.detail_ExpiryDateDialogQRUpdate);
        AppCompatTextView detail_ReceivedqtyDialogQRUpdate = dialog.findViewById(R.id.detail_ReceivedqtyDialogQRUpdate);
        AppCompatTextView detail_BatchNoDialogQRUpdate = dialog.findViewById(R.id.detail_BatchNoDialogQRUpdate);
        AppCompatTextView detail_ConfigurationDialogQRUpdate = dialog.findViewById(R.id.detail_ConfigurationDialogQRUpdate);
        AppCompatTextView detail_NameArabicDialogQRUpdate = dialog.findViewById(R.id.detail_NameArabicDialogQRUpdate);
        AppCompatTextView detail_NameDialogQRUpdate = dialog.findViewById(R.id.detail_NameDialogQRUpdate);
        AppCompatTextView detail_ItemnoDialogQRUpdate = dialog.findViewById(R.id.detail_ItemnoDialogQRUpdate);
        AppCompatTextView unitQuantityUpdate = dialog.findViewById(R.id.unitQuantityUpdate);
        final EditText manuallyUpdateQuantity = dialog.findViewById(R.id.manuallyUpdateQuantity);
        AppCompatButton updateQty = dialog.findViewById(R.id.updateQty_QRDetailsQuantity);
        AppCompatButton cancel_QRDetailsQuantity = dialog.findViewById(R.id.cancel_QRDetailsQuantity);

        if(roleID.equals("1")||roleID.equals("2")){
            manuallyUpdateQuantity.setVisibility(View.GONE);
            updateQty.setVisibility(View.GONE);
        }

        unitQuantityUpdate.setText(response.getUnitId());
        detail_ExpiryDateDialogQRUpdate.setText(response.getExpdate());
        detail_ReceivedqtyDialogQRUpdate.setText(response.getStickerQty());
        detail_BatchNoDialogQRUpdate.setText(response.getBatchId());
        detail_ConfigurationDialogQRUpdate.setText(response.getConfig());
        detail_NameArabicDialogQRUpdate.setText(response.getItemnameArabic());
        detail_NameDialogQRUpdate.setText(response.getItemName());
        detail_ItemnoDialogQRUpdate.setText(response.getItemId());
        manuallyUpdateQuantity.setText(response.getStickerQty());
        final float stickerQty = Float.parseFloat(response.getStickerQty());
        if(stickerQty==0.0){
            manuallyUpdateQuantity.setVisibility(View.GONE);
            updateQty.setVisibility(View.GONE);
        }


        cancel_QRDetailsQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sequenceQRNumber.remove(qrDetails);
                dialog.dismiss();
                edt_scanQR_QuantityUPdateDetails.setText("");
            }
        });


        updateQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updateNumber = manuallyUpdateQuantity.getText().toString();

                if(updateNumber.equals("")){
                    Toast.makeText(QrDetailQuantityUpdate.this, "Update Quantity not be empty.", Toast.LENGTH_SHORT).show();
                }else{
                    float update = Float.parseFloat(updateNumber);
                    if (update==0) {
                        Toast.makeText(QrDetailQuantityUpdate.this, "Please Input Update Quantity.", Toast.LENGTH_SHORT).show();
                    }else{
                        float remainingQty = stickerQty-update;

                        if(remainingQty < 0){
                            sequenceQRNumber.remove(qrDetails);
                            dialog.dismiss();
                            Toast.makeText(QrDetailQuantityUpdate.this, "Remaining Quantity not more then Sticker Quantity.", Toast.LENGTH_SHORT).show();
                            edt_scanQR_QuantityUPdateDetails.setText("");
                        }else{
                           /* if(remainingQty==0.0){
                                response.setConsumeQty(String.valueOf(stickerQty));
                                responseList.add(response);
                                AdapterQrDetailQuantityUpdate adapterQrDetailQuantityUpdate = new AdapterQrDetailQuantityUpdate(QrDetailQuantityUpdate.this, responseList*//*,qrDetails*//*);
                                rcy_QrQuantityUPdateDetails.setLayoutManager(layoutManager);
                                rcy_QrQuantityUPdateDetails.setAdapter(adapterQrDetailQuantityUpdate);
                                dialog.dismiss();
                            }else*/ if(remainingQty<=stickerQty){
                                response.setConsumeQty(String.valueOf(remainingQty));
                                responseList.add(response);
                                if(saveInventoryCountingList!=null && saveInventoryCountingList.size()!=0){

                                    List<ResponseBodyQRDetails> tempList = new ArrayList<>();
                                    tempList = Preference.getInstance(getApplicationContext()).getTempQuantityUpdate();
                                    int size = tempList.size();
                                    tempList.add(size,response);
                                    Preference.getInstance(getApplicationContext()).saveTempQuantityUpdate(tempList);

                                    AdapterQrDetailQuantityUpdate adapterQrDetailQuantityUpdate = new AdapterQrDetailQuantityUpdate(QrDetailQuantityUpdate.this, tempList/*,qrDetails*/);
                                    rcy_QrQuantityUPdateDetails.setLayoutManager(layoutManager);
                                    rcy_QrQuantityUPdateDetails.setAdapter(adapterQrDetailQuantityUpdate);
                                    saveInventoryCountingList = Preference.getInstance(getApplicationContext()).getTempQuantityUpdate();
                                    dialog.dismiss();

                                }else{
                                    AdapterQrDetailQuantityUpdate adapterQrDetailQuantityUpdate = new AdapterQrDetailQuantityUpdate(QrDetailQuantityUpdate.this, responseList/*,qrDetails*/);
                                    rcy_QrQuantityUPdateDetails.setLayoutManager(layoutManager);
                                    rcy_QrQuantityUPdateDetails.setAdapter(adapterQrDetailQuantityUpdate);
                                    dialog.dismiss();
                                }
                                edt_scanQR_QuantityUPdateDetails.setText("");
                            }else{
                                dialog.dismiss();
                                edt_scanQR_QuantityUPdateDetails.setText("");
                                Toast.makeText(QrDetailQuantityUpdate.this, "Remaining Quantity not more then Sticker Quantity.", Toast.LENGTH_SHORT).show();
                            }







                        }
                    }


                }
            }
        });
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_backQrQuantityUPdateDetails:
                Intent intent = new Intent(getApplicationContext(), dashboardNew.class);
                startActivity(intent);
                finish();
                break;
            case R.id.update_QrQuantityUPdateDetails:
               updateDetails();
                break;
            case R.id.saveTempBtn_qrQuantityUpdate:
                saveTemporary();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), dashboardNew.class);
        startActivity(intent);
        finish();
    }

    public void invalidItemDialog() {
        final Dialog dialog = new Dialog(QrDetailQuantityUpdate.this);
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
                edt_scanQR_QuantityUPdateDetails.setText("");
                dialog.dismiss();
            }
        });
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    public void updateDetails(){
        final ProgressDialog progressDialog = new ProgressDialog(QrDetailQuantityUpdate.this);
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        progressDialog.setCancelable(false); // set cancelable to false
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        UpdateRequestDetails updateRequestDetails = null;
        String userId = Preference.getInstance(getApplicationContext()).getUserId();

        if(saveInventoryCountingList==null){
            updateRequestDetails = new UpdateRequestDetails(userId, responseList);
        }else if(saveInventoryCountingList.size()!=0){
            updateRequestDetails = new UpdateRequestDetails(userId, saveInventoryCountingList);
        }else if(saveInventoryCountingList.size()==0){
            updateRequestDetails = new UpdateRequestDetails(userId, responseList);
        }



        Call<PostingJsonResponse> call = apiInterface.updateStickerQty(updateRequestDetails);
        call.enqueue(new Callback<PostingJsonResponse>() {
            @Override
            public void onResponse(Call<PostingJsonResponse> call, Response<PostingJsonResponse> response) {
                try{
                    progressDialog.dismiss();
                    if (response.body().getStatus().equals("success")) {
                        if(saveInventoryCountingList!=null){
                            saveInventoryCountingList.clear();
                        }
                        if(responseList!=null){
                            responseList.clear();
                        }

                        Preference.getInstance(getApplicationContext()).saveTempQuantityUpdate(saveInventoryCountingList);
                        Toast.makeText(QrDetailQuantityUpdate.this, "Successfully Updated.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),dashboardNew.class);
                        startActivity(intent);
                        finish();

                    } else {
                        alertDialog(response.body().getMessage());
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(QrDetailQuantityUpdate.this, "Service Unavailable.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<PostingJsonResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(QrDetailQuantityUpdate.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
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

    public void saveTemporary(){
        if(saveInventoryCountingList==null){
            Preference.getInstance(getApplicationContext()).saveTempQuantityUpdate(responseList);
            Intent intent = new Intent(getApplicationContext(), dashboardNew.class);
            startActivity(intent);
            finish();
        }else if(saveInventoryCountingList.size()==0){
            Preference.getInstance(getApplicationContext()).saveTempQuantityUpdate(responseList);
            Intent intent = new Intent(getApplicationContext(), dashboardNew.class);
            startActivity(intent);
            finish();
        } else{
//            List<ResponseBodyQRDetails> tempList = new ArrayList<>();
//            tempList = Preference.getInstance(getApplicationContext()).getTempQuantityUpdate();
//            int size = tempList.size();
//            for(int i=0;i<=responseList.size();i++){
//                tempList.add(size,responseList.get(i));
//                size++;
//            }
            Preference.getInstance(getApplicationContext()).saveTempQuantityUpdate(saveInventoryCountingList);
            Intent intent = new Intent(getApplicationContext(), dashboardNew.class);
            startActivity(intent);
            finish();
        }
    }
}
