package com.yoeki.kalpnay.frdinventry.MRN;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.yoeki.kalpnay.frdinventry.Api.Api;
import com.yoeki.kalpnay.frdinventry.Api.ApiInterface;
import com.yoeki.kalpnay.frdinventry.MRN.Adapter.MRN_Dashboard_Adapter;
import com.yoeki.kalpnay.frdinventry.MRN.Model.MRNDetailsList;
import com.yoeki.kalpnay.frdinventry.MRN.Model.mrnDashboardModel;
import com.yoeki.kalpnay.frdinventry.Model.MRN.GetMRNModels;
import com.yoeki.kalpnay.frdinventry.Model.MRN.MRNList;
import com.yoeki.kalpnay.frdinventry.R;
import com.yoeki.kalpnay.frdinventry.Dashboard.dashboardNew;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MRN_Dashboard extends AppCompatActivity implements View.OnClickListener {

    ArrayList<mrnDashboardModel> modelMRN;
    RecyclerView mrndash_recycler;
    AppCompatButton bck_mrn;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mrn_dashboard);
        initialize();
        modelMRN = new ArrayList<>();
        bck_mrn.setOnClickListener(this);
        mrnDashboardData();
    }

    public void initialize() {
        mrndash_recycler = findViewById(R.id.mrndash_recycler);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefreshMrnDashboard);
        bck_mrn = findViewById(R.id.bck_mrn);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mrnDashboardData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bck_mrn:
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

    public void mrnDashboardData() {
        final ProgressDialog progressDialog = new ProgressDialog(MRN_Dashboard.this);
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        progressDialog.setCancelable(false); // set cancelable to false
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);

        Call<GetMRNModels> call = apiInterface.getMRNDataModels();
        call.enqueue(new Callback<GetMRNModels>() {
            @Override
            public void onResponse(Call<GetMRNModels> call, Response<GetMRNModels> response) {
                try{
                    progressDialog.dismiss();

                if (response.body().getStatus().equals("Success")) {

//
//
//                    for(int i=0;i<=mrnList.size()-1;i++){
//                        String[] date = mrnList.get(i).getMRNDate().split("\\s+");
//                        String sdf = date[0];
//                        mrnList.get(i).setMRNDate(sdf);
//                    }

//                    08-07-2019 00:00:00





                    List<MRNList> mrnList = response.body().getMRNList();


                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    MRN_Dashboard_Adapter adapter = new MRN_Dashboard_Adapter(MRN_Dashboard.this, mrnList);
                    mrndash_recycler.setLayoutManager(layoutManager);
                    mrndash_recycler.setAdapter(adapter);
                    mrndash_recycler.scheduleLayoutAnimation();
                } else {
                    Toast.makeText(MRN_Dashboard.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(MRN_Dashboard.this, "Service Unavailable.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetMRNModels> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MRN_Dashboard.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
