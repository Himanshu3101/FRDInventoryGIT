package com.yoeki.kalpnay.frdinventry.InventoryCountingNew;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.yoeki.kalpnay.frdinventry.Api.Api;
import com.yoeki.kalpnay.frdinventry.Api.ApiInterface;
import com.yoeki.kalpnay.frdinventry.Api.Preference;
import com.yoeki.kalpnay.frdinventry.Dashboard.dashboardNew;
import com.yoeki.kalpnay.frdinventry.InventoryCountingNew.Adapter.InventoryCountingAdapter;
import com.yoeki.kalpnay.frdinventry.InventoryCountingNew.model.InventoryCountingData;
import com.yoeki.kalpnay.frdinventry.InventoryCountingNew.model.ScheduleDetail;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.UserIDModel;
import com.yoeki.kalpnay.frdinventry.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventoryCountingNew extends AppCompatActivity {

    AppCompatButton backInventoryCounting;
    RecyclerView recyclerViewInventoryCounting;
    List<ScheduleDetail> scheduleDetailList;
    InventoryCountingAdapter inventoryCountingAdapter;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_counting_new);
        userId = Preference.getInstance(getApplicationContext()).getUserId();

        initViews();
        getCountingList();
    }

    void initViews() {
        backInventoryCounting = findViewById(R.id.backInventoryCounting);
        backInventoryCounting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), dashboardNew.class);
                startActivity(intent);
                finish();
            }
        });

        recyclerViewInventoryCounting = findViewById(R.id.recyclerViewInventoryCounting);
    }

    void getCountingList() {
        final ProgressDialog progressDialog = new ProgressDialog(InventoryCountingNew.this);
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        progressDialog.setCancelable(false); // set cancelable to false
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        UserIDModel userIDModel = new UserIDModel(userId);

        Call<InventoryCountingData> call = apiInterface.getCountingSchedules(userIDModel);
        call.enqueue(new Callback<InventoryCountingData>() {
            @Override
            public void onResponse(Call<InventoryCountingData> call, Response<InventoryCountingData> response) {
                progressDialog.dismiss();
                InventoryCountingData body = response.body();
                if (body.getMessage() != null || body.getStatus() != null) {
                    if (body.getStatus().equals("Success")) {
                        scheduleDetailList = response.body().getScheduleList();
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        inventoryCountingAdapter = new InventoryCountingAdapter(InventoryCountingNew.this, scheduleDetailList);
                        recyclerViewInventoryCounting.setLayoutManager(layoutManager);
                        recyclerViewInventoryCounting.setAdapter(inventoryCountingAdapter);
                    } else {
                        Toast.makeText(InventoryCountingNew.this, body.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(InventoryCountingNew.this, "Invalid data found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InventoryCountingData> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(InventoryCountingNew.this, "Some problem occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), dashboardNew.class);
        startActivity(intent);
        finish();
    }
}
