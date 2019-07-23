package com.yoeki.kalpnay.frdinventry.InventoryShipperPicker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yoeki.kalpnay.frdinventry.Api.Api;
import com.yoeki.kalpnay.frdinventry.Api.ApiInterface;
import com.yoeki.kalpnay.frdinventry.Api.Preference;
import com.yoeki.kalpnay.frdinventry.QRDetails.ResponseBodyQRDetails;
import com.yoeki.kalpnay.frdinventry.R;
import com.yoeki.kalpnay.frdinventry.Dashboard.dashboardNew;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class dashboardInventoryShipperPicker extends AppCompatActivity implements View.OnClickListener {
    AppCompatButton bck_orderProcessing;
    AppCompatTextView pendingCompleted,completeTotal;
    LinearLayout linearLayoutOrderCompleted,linearLayoutOrderPending;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_inventory_shipper_picker);
        initialize();
        linearLayoutOrderCompleted.setOnClickListener(this);
        linearLayoutOrderPending.setOnClickListener(this);
        bck_orderProcessing.setOnClickListener(this);
        countingSet();
    }

    public void initialize(){
        linearLayoutOrderCompleted=findViewById(R.id.linearLayoutOrderCompleted);
        linearLayoutOrderPending=findViewById(R.id.linearLayoutOrderPending);
        completeTotal = findViewById(R.id.completeTotal);
        pendingCompleted = findViewById(R.id.pendingCompleted);
        bck_orderProcessing = findViewById(R.id.bck_orderProcessing);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linearLayoutOrderCompleted:
                Intent intent = new Intent(getApplicationContext(), InventoryCompleted.class);
                startActivity(intent);
                finish();
                break;
            case R.id.linearLayoutOrderPending:
                Intent intent1 = new Intent(getApplicationContext(), InventoryPending.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.bck_orderProcessing:
                Intent intent2 = new Intent(getApplicationContext(), dashboardNew.class);
                startActivity(intent2);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent2 = new Intent(getApplicationContext(), dashboardNew.class);
        startActivity(intent2);
        finish();
    }

    public void countingSet(){
        final ProgressDialog progressDialog = new ProgressDialog(dashboardInventoryShipperPicker.this);
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        progressDialog.setCancelable(false); // set cancelable to false
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        String userId = Preference.getInstance(getApplicationContext()).getUserId();

        UserIDModel detailsRequest = new UserIDModel(userId);

        Call<ResponseBodyQRDetails> call = apiInterface.inventoryCounting(detailsRequest);
        call.enqueue(new Callback<ResponseBodyQRDetails>() {
            @Override
            public void onResponse(Call<ResponseBodyQRDetails> call, Response<ResponseBodyQRDetails> response) {
                progressDialog.dismiss();
                if(response.body().getStatus().equals("Success")){

                    completeTotal.setText(response.body().getTotalcomplete());
                    pendingCompleted.setText(response.body().getTotalPending());
                }else{
                    Toast.makeText(dashboardInventoryShipperPicker.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBodyQRDetails> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(dashboardInventoryShipperPicker.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
