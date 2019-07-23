package com.yoeki.kalpnay.frdinventry.InventoryShipperPicker;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yoeki.kalpnay.frdinventry.Api.Api;
import com.yoeki.kalpnay.frdinventry.Api.ApiInterface;
import com.yoeki.kalpnay.frdinventry.Api.Preference;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.ItemRequisition.DashboardModel;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.InventoryPendingModel;
import com.yoeki.kalpnay.frdinventry.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventoryCompleted extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rycdashboard;
    ArrayList<DashboardModel>listdashboard;
    AnimatorSet slidedown;
    Button bck_shipperPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_ship_pick_dashboard);
        initialize();
        listdashboard=new ArrayList<>();
        bck_shipperPicker.setOnClickListener(this);
//        dashboarddataComp();
    }

    public void initialize(){
        rycdashboard=findViewById(R.id.rycdashboard);
        bck_shipperPicker = findViewById(R.id.bck_shipperPicker);
        slidedown = (AnimatorSet) AnimatorInflater.loadAnimator(InventoryCompleted.this, R.animator.slide_down);
    }

   /* public void dashboarddataComp(){

        final ProgressDialog progressDialog = new ProgressDialog(InventoryCompleted.this);
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        progressDialog.setCancelable(false); // set cancelable to false
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);

        String userId = Preference.getInstance(getApplicationContext()).getUserId();

        UserIDModel userIDModel = new UserIDModel(userId);

        Call<InventoryPendingModel> call = apiInterface.inventoryComplete(userIDModel);
        call.enqueue(new Callback<InventoryPendingModel>() {
            @Override
            public void onResponse(Call<InventoryPendingModel> call, Response<InventoryPendingModel> response) {
                progressDialog.dismiss();
                if (response.body().getStatus().equals("Success")) {

                    *//*LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    inventoryPendingAdapter adapter=new inventoryPendingAdapter(InventoryCompleted.this,response.body().getDataList());
                    rycdashboard.setLayoutManager(layoutManager);
                    rycdashboard.setAdapter(adapter);*//*

                } else {
                    Toast.makeText(InventoryCompleted.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InventoryPendingModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(InventoryCompleted.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }*/


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bck_shipperPicker:
                Intent intent = new Intent(getApplicationContext(), dashboardInventoryShipperPicker.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), dashboardInventoryShipperPicker.class);
        startActivity(intent);
        finish();
    }
}
