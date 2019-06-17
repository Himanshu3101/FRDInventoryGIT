package com.yoeki.kalpnay.frdinventry.Dashboard.InventoryCounting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yoeki.kalpnay.frdinventry.R;

import java.util.ArrayList;

public class dataScanOnGrid extends AppCompatActivity {

    DetailsScanning sadapter;
    private ArrayList<String> detailsRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.data_scan_grid);

        detailsRecycler = new ArrayList<>();
        detailsRecycler.add("01,Rice,أرز,80,Center Store,120,40");
        detailsRecycler.add("02,Wheat,أرز,100,Center dfgd Store,120,40");
        detailsRecycler.add("03,Vegetables,أرز,90,Center frteertefc Store,120,40");
        detailsRecycler.add("04,Fried Rice,أرز,120,Center ertgfdf Store,120,40");
        detailsRecycler.add("05,Chicken Curry,أرز,110,Center adsfg Store,120,40");

        RecyclerView recyclerView =  findViewById(R.id.detailsScannigItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sadapter = new DetailsScanning( this ,detailsRecycler);
        recyclerView.setAdapter(sadapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),dashboardInventoryCount.class);
        startActivity(intent);
        finish();
    }
}
