package com.yoeki.kalpnay.frdinventry.banchInventoryReceiving;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.ItemRequisition.inventoryPendingAdapter;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.GetRequisitionPending;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.UserIDModel;
import com.yoeki.kalpnay.frdinventry.R;
import com.yoeki.kalpnay.frdinventry.banchInventoryReceiving.model.CompletedReceivingModel;
import com.yoeki.kalpnay.frdinventry.Dashboard.dashboardNew;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedReceiving extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerViewCompletedReceiving;
    AppCompatButton backCompletedReceiving;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<CompletedReceivingModel> listReceiving;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_receiving);
        listReceiving = new ArrayList<>();
        initUi();
        backCompletedReceiving.setOnClickListener(this);
        dashboardData();

    }

    public void initUi() {
        recyclerViewCompletedReceiving = findViewById(R.id.recyclerViewCompletedReceiving);
        backCompletedReceiving = findViewById(R.id.backCompletedReceiving);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefreshPickerDashboard);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dashboardData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    void dashboardData(){
        final ProgressDialog progressDialog = new ProgressDialog(CompletedReceiving.this);
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        progressDialog.setCancelable(false); // set cancelable to false
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);

        String userId = Preference.getInstance(getApplicationContext()).getUserId();

        UserIDModel userIDModel = new UserIDModel(userId);

        Call<GetRequisitionPending> call = apiInterface.completeRequestNumber(userIDModel);
        call.enqueue(new Callback<GetRequisitionPending>() {


            @Override
            public void onResponse(Call<GetRequisitionPending> call, Response<GetRequisitionPending> response) {
                progressDialog.dismiss();
                if (response.body().getStatus().equals("Success")) {

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    inventoryPendingAdapter adapter=new inventoryPendingAdapter(CompletedReceiving.this,response.body().getDataList(),"Completed");
                    recyclerViewCompletedReceiving.setLayoutManager(layoutManager);
                    recyclerViewCompletedReceiving.setAdapter(adapter);
                    recyclerViewCompletedReceiving.scheduleLayoutAnimation();

                } else {
                    Toast.makeText(CompletedReceiving.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetRequisitionPending> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CompletedReceiving.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backCompletedReceiving:
                startActivity(new Intent(getApplicationContext(), dashboardNew.class));
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), dashboardNew.class));
        finish();
    }
}
