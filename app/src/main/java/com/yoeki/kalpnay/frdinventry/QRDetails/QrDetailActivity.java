package com.yoeki.kalpnay.frdinventry.QRDetails;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yoeki.kalpnay.frdinventry.Api.Api;
import com.yoeki.kalpnay.frdinventry.Api.ApiInterface;
import com.yoeki.kalpnay.frdinventry.R;
import com.yoeki.kalpnay.frdinventry.Dashboard.dashboardNew;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrDetailActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatButton img_backQrDetails;
    AppCompatAutoCompleteTextView edt_scanQR;
    AppCompatTextView detail_VendorNo, detail_VendorName, detail_po_no, detail_ItemnoMRN, detail_NameMRN, detail_NameMRNArabic,
            detail_ConfigurationMRN, detail_BatchNoMRN, detail_Receivedqty, detail_ExpiryDateMRN,unitQr;
    LinearLayout qrDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_detail);

        initialize();
        img_backQrDetails.setOnClickListener(this);


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                String qrDetails = String.valueOf(editable);
                if (qrDetails.length() >= 10) {
                    try {
                        qrDetails();
                    }catch (Exception e){
                        Toast.makeText(QrDetailActivity.this, "Service Unavailable.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        edt_scanQR.addTextChangedListener(textWatcher);
    }

    public void initialize() {
        qrDetails = findViewById(R.id.qrDetails);
        img_backQrDetails = findViewById(R.id.img_backQrDetails);
        edt_scanQR = findViewById(R.id.edt_scanQR);
        detail_VendorNo = findViewById(R.id.detail_VendorNo);
        detail_VendorName = findViewById(R.id.detail_VendorName);
        detail_po_no = findViewById(R.id.detail_po_no);
        detail_ItemnoMRN = findViewById(R.id.detail_ItemnoMRN);
        detail_NameMRN = findViewById(R.id.detail_NameMRN);
        detail_NameMRNArabic = findViewById(R.id.detail_NameMRNArabic);
        detail_ConfigurationMRN = findViewById(R.id.detail_ConfigurationMRN);
        detail_BatchNoMRN = findViewById(R.id.detail_BatchNoMRN);
        detail_Receivedqty = findViewById(R.id.detail_Receivedqty);
        detail_ExpiryDateMRN = findViewById(R.id.detail_ExpiryDateMRN);
        unitQr = findViewById(R.id.unitQr);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_backQrDetails:
                Intent intent = new Intent(getApplicationContext(), dashboardNew.class);
                startActivity(intent);
                finish();
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

    public void qrDetails() {

        final ProgressDialog progressDialog = new ProgressDialog(QrDetailActivity.this);
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        progressDialog.setCancelable(false); // set cancelable to false
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);

        String qr = edt_scanQR.getText().toString();

        RequestBodyQRDetails requestBodyQRDetails = new RequestBodyQRDetails(qr);

        Call<ResponseBodyQRDetails> call = apiInterface.QRWiseData(requestBodyQRDetails);
        call.enqueue(new Callback<ResponseBodyQRDetails>() {
            @Override
            public void onResponse(Call<ResponseBodyQRDetails> call, Response<ResponseBodyQRDetails> response) {
                try{
                progressDialog.dismiss();
                if (response.body().getStatus().equals("Success")) {
                    qrDetails.setVisibility(View.VISIBLE);
                    unitQr.setText(response.body().getUnitId());
                    detail_VendorNo.setText(response.body().getVendorId());
                    detail_VendorName.setText(response.body().getVendorName());
                    detail_po_no.setText(response.body().getPONumber());
                    detail_ItemnoMRN.setText(response.body().getItemId());
                    detail_NameMRN.setText(response.body().getItemName());
                    detail_NameMRNArabic.setText(response.body().getItemnameArabic());
                    detail_ConfigurationMRN.setText(response.body().getConfig());
                    detail_BatchNoMRN.setText(response.body().getBatchId());
                    detail_Receivedqty.setText(response.body().getStickerQty());
                    String[] Date = response.body().getExpdate().split("\\s+");
                    detail_ExpiryDateMRN.setText(Date[0]);
                    edt_scanQR.setText("");
                } else {
                    invalidItemDialog();
                }
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(QrDetailActivity.this, "Service Unavailable.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBodyQRDetails> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(QrDetailActivity.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void invalidItemDialog() {
        final Dialog dialog = new Dialog(QrDetailActivity.this);
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
                edt_scanQR.setText("");
                dialog.dismiss();
            }
        });
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }
}
