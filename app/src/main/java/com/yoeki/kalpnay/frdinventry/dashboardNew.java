package com.yoeki.kalpnay.frdinventry;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yoeki.kalpnay.frdinventry.Api.Api;
import com.yoeki.kalpnay.frdinventry.Api.ApiInterface;
import com.yoeki.kalpnay.frdinventry.Api.Preference;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.UserIDModel;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.dashboardInventoryShipperPicker;
import com.yoeki.kalpnay.frdinventry.Login.LoginActivity;
import com.yoeki.kalpnay.frdinventry.MRN.Adapter.MRN_Dashboard_AdapterDetails;
import com.yoeki.kalpnay.frdinventry.MRN.MRN_Dashboard;
import com.yoeki.kalpnay.frdinventry.MRN.MaterialReceivingDetails;
import com.yoeki.kalpnay.frdinventry.MRN.Model.MrnNumberDetailResponse;
import com.yoeki.kalpnay.frdinventry.MRN.Model.mrnNumberDetailsRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class dashboardNew extends AppCompatActivity implements View.OnClickListener {
    LinearLayout linearLayoutMaterialReciving, linearLayoutInventoryShipperPicker, linearLayoutBranchInventoryReciving, linearLayoutInventoryCounting;
    AppCompatButton logOut;
    TextView MRNCount, shipperCount, birCount, inventoryCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_new);

        initialize();
        linearLayoutMaterialReciving.setOnClickListener(this);
        linearLayoutInventoryShipperPicker.setOnClickListener(this);
        linearLayoutBranchInventoryReciving.setOnClickListener(this);
        linearLayoutInventoryCounting.setOnClickListener(this);
        logOut.setOnClickListener(this);
        countungSet();
    }

    public  void initialize(){
        linearLayoutMaterialReciving = findViewById(R.id.linearLayoutMaterialReciving);
        linearLayoutInventoryShipperPicker = findViewById(R.id.linearLayoutInventoryShipperPicker);
        linearLayoutBranchInventoryReciving = findViewById(R.id.linearLayoutBranchInventoryReciving);
        linearLayoutInventoryCounting = findViewById(R.id.linearLayoutInventoryCounting);
        logOut = findViewById(R.id.logOut);
        MRNCount = findViewById(R.id.MRNCount);
        shipperCount = findViewById(R.id.shipperCount);
        birCount = findViewById(R.id.birCount);
        inventoryCount = findViewById(R.id.inventoryCount);
    }

    public void countungSet(){
        final ProgressDialog progressDialog = new ProgressDialog(dashboardNew.this);
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        progressDialog.setCancelable(false); // set cancelable to false
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        String userId = Preference.getInstance(getApplicationContext()).getUserId();

        UserIDModel detailsRequest = new UserIDModel(userId);

        Call<CountResposeBocy> call = apiInterface.inventoryCounting(detailsRequest);
        call.enqueue(new Callback<CountResposeBocy>() {
            @Override
            public void onResponse(Call<CountResposeBocy> call, Response<CountResposeBocy> response) {
                progressDialog.dismiss();
                if(response.body().getStatus().equals("Success")){

                    MRNCount.setText(response.body().getTotalMrn());
                    shipperCount.setText(response.body().getTotalPending());

                }else{
                    Toast.makeText(dashboardNew.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CountResposeBocy> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(dashboardNew.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearLayoutMaterialReciving:
                Intent intent1 = new Intent(getApplicationContext(), MRN_Dashboard.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.linearLayoutInventoryShipperPicker:
                Intent intent = new Intent(getApplicationContext(), dashboardInventoryShipperPicker.class);
                startActivity(intent);
                finish();
                break;
            case R.id.linearLayoutBranchInventoryReciving:

                break;
            case R.id.linearLayoutInventoryCounting:

                break;
            case R.id.logOut:
                openLogOutDialog(R.style.DialogAnimation,"");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        openLogOutDialog(R.style.DialogAnimation,"");
    }

    private void openLogOutDialog(int dialogAnimation, String s) {
        final Dialog dialog=new Dialog(this);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.getWindow().getAttributes().windowAnimations=dialogAnimation;

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);

        AppCompatButton button_yes=dialog.findViewById(R.id.button_yes);
        AppCompatButton button_no=dialog.findViewById(R.id.button_no);

        button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openRef=new Intent(dashboardNew.this, LoginActivity.class);
                startActivity(openRef);
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }
}
