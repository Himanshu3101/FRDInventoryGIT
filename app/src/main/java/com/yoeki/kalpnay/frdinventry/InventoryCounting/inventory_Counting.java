package com.yoeki.kalpnay.frdinventry.InventoryCounting;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.yoeki.kalpnay.frdinventry.Api.Api;
import com.yoeki.kalpnay.frdinventry.Api.ApiInterface;
import com.yoeki.kalpnay.frdinventry.Api.Preference;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.UserIDModel;
import com.yoeki.kalpnay.frdinventry.Login.loginModel.wareHouseResponse;
import com.yoeki.kalpnay.frdinventry.QRDetails.ResponseBodyQRDetails;
import com.yoeki.kalpnay.frdinventry.R;
import com.yoeki.kalpnay.frdinventry.Dashboard.dashboardNew;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class inventory_Counting extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton selectionWarehouse;
    List<String> listWareHouse;
    List<String> journalAndWarehouse;
    List<ResponseBodyQRDetails> saveInventoryCountingList;
    String selectedWarehouse = "Select Reason", journal, warehouse ;
    List<String> journalList = new ArrayList<>();
    AppCompatButton backInventoryCounting;
    RecyclerView recyclerViewInventoryReceivingTemp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_counting);
        init();
        saveInventoryCountingList =new ArrayList<>();
        journalAndWarehouse=new ArrayList<>();
        saveInventoryCountingList = Preference.getInstance(getApplicationContext()).getInvenrtoryCounting();

        try{
            if (saveInventoryCountingList == null) {
                // Do Nothing
            }else{
                for(int i =0;i<=saveInventoryCountingList.size()-1;i++){
                    journal = saveInventoryCountingList.get(i).getJournal();
                    warehouse = saveInventoryCountingList.get(i).getWareHouse();
                    if(journalList.size()==0){
                        journalAndWarehouse.add(journal + "&" + warehouse);
                        journalList.add(journal);
                    }else{
                        if(journalList.contains(journal)){
                        // Do Nothing
                        }else{
                            journalAndWarehouse.add(journal + "&" + warehouse);
                            journalList.add(journal);
                        }
                    }

                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }


        if(journalAndWarehouse.size() != 0) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            InventoryCountTemp inventoryCountTemp = new InventoryCountTemp(inventory_Counting.this, journalAndWarehouse);
            recyclerViewInventoryReceivingTemp.setLayoutManager(layoutManager);
            recyclerViewInventoryReceivingTemp.setAdapter(inventoryCountTemp);
        }

        backInventoryCounting.setOnClickListener(this);
    }

    public void init(){
        selectionWarehouse = (FloatingActionButton)findViewById(R.id.selectionWarehouse);
        recyclerViewInventoryReceivingTemp  = findViewById(R.id.recyclerViewInventoryReceivingTemp);
        backInventoryCounting = (AppCompatButton)findViewById(R.id.backInventoryCounting);
        selectionWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wareHouseSelection();
            }
        });
    }

    public void wareHouseSelection() {
        final Dialog dialog = new Dialog(inventory_Counting.this);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dashboard_inventory_count);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);




        List<wareHouseResponse> wareHouseList =new ArrayList<>();
        wareHouseList = Preference.getInstance(getApplicationContext()).getwareHouseResponse();

        AppCompatButton submitWarehouse = (AppCompatButton)dialog.findViewById(R.id.submitWarehouse);
        Spinner spin = (Spinner)dialog.findViewById(R.id.spinner_warehouse);
         listWareHouse = new ArrayList<>();
        try {
            for (int i = 0; i <= wareHouseList.size() - 1; ) {
                String wareHouseName = wareHouseList.get(i).getWareHouse().toString();
                listWareHouse.add(wareHouseName);
                i++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }




        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(inventory_Counting.this,  android.R.layout.simple_spinner_item, listWareHouse);
        adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter_state);

            spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int spinnerPosition, long id) {
                    selectedWarehouse = parent.getItemAtPosition(spinnerPosition).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            submitWarehouse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!selectedWarehouse.equals("Select Reason")){
                        getJournal(selectedWarehouse);
                    }else{
                        Toast.makeText(inventory_Counting.this, "Please, Select Warehouse.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        dialog.show();
    }

    public void getJournal(final String selectedWarehouse) {
        final ProgressDialog progressDialog = new ProgressDialog(inventory_Counting.this);
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        progressDialog.setCancelable(false); // set cancelable to false
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        String userId = Preference.getInstance(getApplicationContext()).getUserId();

        UserIDModel detailsRequest = new UserIDModel(userId);

        Call<JiournalResponse> call = apiInterface.getJournalNum(detailsRequest);
        call.enqueue(new Callback<JiournalResponse>() {
            @Override
            public void onResponse(Call<JiournalResponse> call, Response<JiournalResponse> response) {
                try {
                    progressDialog.dismiss();
                    if (response.body().getStatus().equals("Success")) {
                        String journal = response.body().getInventJounralId();
                        Toast.makeText(inventory_Counting.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),InventoryCountingScan.class);
                        intent.putExtra("wareHouse",selectedWarehouse);
                        intent.putExtra("journal",journal);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(inventory_Counting.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(inventory_Counting.this, "Service Unavailable.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JiournalResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(inventory_Counting.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backInventoryCounting:
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
}
