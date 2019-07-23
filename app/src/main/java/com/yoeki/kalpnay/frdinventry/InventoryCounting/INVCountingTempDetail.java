package com.yoeki.kalpnay.frdinventry.InventoryCounting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yoeki.kalpnay.frdinventry.Api.Preference;
import com.yoeki.kalpnay.frdinventry.QRDetails.ResponseBodyQRDetails;
import com.yoeki.kalpnay.frdinventry.R;

import java.util.ArrayList;
import java.util.List;

public class INVCountingTempDetail extends AppCompatActivity implements View.OnClickListener {
    int languageChangeVisible = 1;
    String JournalSelect,WarehouseSelect;
    List<ResponseBodyQRDetails> saveInventoryCountingList,listMatched;
    RecyclerView rcy_inv_countingtemp_detail;
    AdapterINVCountingTempDetail adapterINVCountingTempDetail;
    AppCompatButton close,languageChange_inv_countingtemp_detail,img_backinv_countingtemp_detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inv_countingtemp_detail);
        init();
        close.setOnClickListener(this);
        img_backinv_countingtemp_detail.setOnClickListener(this);
        languageChange_inv_countingtemp_detail.setOnClickListener(this);
        saveInventoryCountingList =new ArrayList<>();
        listMatched =new ArrayList<>();
        JournalSelect = getIntent().getStringExtra("JournalSelect");
        WarehouseSelect = getIntent().getStringExtra("WareHouseSelect");
        saveInventoryCountingList = Preference.getInstance(getApplicationContext()).getInvenrtoryCounting();

        for(int i=0;i<=saveInventoryCountingList.size()-1;i++){
            if(JournalSelect.equals(saveInventoryCountingList.get(i).getJournal())){
                listMatched.add(saveInventoryCountingList.get(i));
            }
        }

        if(listMatched.size() != 0) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            adapterINVCountingTempDetail = new AdapterINVCountingTempDetail(INVCountingTempDetail.this, listMatched, WarehouseSelect);
            rcy_inv_countingtemp_detail.setLayoutManager(layoutManager);
            rcy_inv_countingtemp_detail.setAdapter(adapterINVCountingTempDetail);
        }

    }

    public void init(){
        close = findViewById(R.id.close);
        rcy_inv_countingtemp_detail  = findViewById(R.id.rcy_inv_countingtemp_detail);
        img_backinv_countingtemp_detail = findViewById(R.id.img_backinv_countingtemp_detail);
        languageChange_inv_countingtemp_detail = findViewById(R.id.languageChange_inv_countingtemp_detail);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                Intent intent = new Intent(getApplicationContext(), inventory_Counting.class);
                startActivity(intent);
                finish();
                break;
            case R.id.img_backinv_countingtemp_detail:
                Intent intent1 = new Intent(getApplicationContext(), inventory_Counting.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.languageChange_inv_countingtemp_detail:
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                adapterINVCountingTempDetail = new AdapterINVCountingTempDetail(INVCountingTempDetail.this, listMatched, WarehouseSelect, languageChangeVisible);
                rcy_inv_countingtemp_detail.setLayoutManager(layoutManager);
                rcy_inv_countingtemp_detail.setAdapter(adapterINVCountingTempDetail);
                if (languageChangeVisible == 0) {
                    languageChangeVisible = 1;
                } else {
                    languageChangeVisible = 0;
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1 = new Intent(getApplicationContext(), inventory_Counting.class);
        startActivity(intent1);
        finish();
    }
}
