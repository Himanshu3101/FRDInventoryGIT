package com.yoeki.kalpnay.frdinventry.InventoryShipperPicker;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yoeki.kalpnay.frdinventry.Api.Api;
import com.yoeki.kalpnay.frdinventry.Api.ApiInterface;
import com.yoeki.kalpnay.frdinventry.Api.Preference;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.InventoryPendingModel;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.ParticularRequisitionDetails;
import com.yoeki.kalpnay.frdinventry.Items.ItemsModel;
import com.yoeki.kalpnay.frdinventry.Items.RequestedItemsFragment;
import com.yoeki.kalpnay.frdinventry.Items.SelectedItemsFragment;
import com.yoeki.kalpnay.frdinventry.Items.requestControlDetailList;
import com.yoeki.kalpnay.frdinventry.R;
import com.yoeki.kalpnay.frdinventry.dashboardInventoryRequisition;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequisitionControlDetails extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rcy_items;
    AppCompatTextView reqNo, rcd_Date, branchName_RCD;
    ArrayList<ItemsModel> arraylist;
    AppCompatButton shipBtn, languageChangeRCD, img_back;
    String wareHouse;
    AppCompatAutoCompleteTextView scanQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_control_detail);




        setupWindowAnimations();

        arraylist = new ArrayList<>();

        Initialize();
        String reqNmbr = getIntent().getStringExtra("RequisitionNo");
        wareHouse = getIntent().getStringExtra("wareHouse");
        reqNo.setText(reqNmbr);
        addlistdata();

        img_back.setOnClickListener(this);
        shipBtn.setOnClickListener(this);
        languageChangeRCD.setOnClickListener(this);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String qr = scanQR.getText().toString();
                if (qr.equals("123456789")) {
                    invalidItemDialog();
                }
            }
        };
        scanQR.addTextChangedListener(textWatcher);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void Initialize() {
        reqNo = findViewById(R.id.reqNo);
        rcd_Date = findViewById(R.id.rcd_Date);
        branchName_RCD = findViewById(R.id.branchName_RCD);
        scanQR = findViewById(R.id.scanQR);
        shipBtn = findViewById(R.id.shipBtn);
        img_back = findViewById(R.id.img_back);
        languageChangeRCD = findViewById(R.id.languageChangeRCD);
        rcy_items = findViewById(R.id.rcy_items);
    }

    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(2000);
        getWindow().setEnterTransition(fade);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                Intent intent = new Intent(getApplicationContext(), InventoryPending.class);
                startActivity(intent);
                finish();
                break;

            case R.id.shipBtn:
                transferOrderDialog();
                break;
            case R.id.languageChangeRCD:


//                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//                MRN_Dashboard_AdapterDetails adapter = new MRN_Dashboard_AdapterDetails(MaterialReceivingDetails.this, listMRNDetailsList, languageChangeVisible);
//                rcy_itemsMRD.setLayoutManager(layoutManager);
//                rcy_itemsMRD.setAdapter(adapter);
//                if (languageChangeVisible == 0) {
//                    languageChangeVisible = 1;
//                } else {
//                    languageChangeVisible = 0;
//                }




                break;
        }
    }

    public void addlistdata() {
        final ProgressDialog progressDialog = new ProgressDialog(RequisitionControlDetails.this);
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        progressDialog.setCancelable(false); // set cancelable to false
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);

        String userId = Preference.getInstance(getApplicationContext()).getUserId();
        String requisition = getIntent().getStringExtra("RequisitionNo");

        ParticularRequisitionDetails userIDModeParticularRequisitionDetailsl = new ParticularRequisitionDetails(userId, requisition);
        Call<InventoryPendingModel> call = apiInterface.inventoryPicker(userIDModeParticularRequisitionDetailsl);
        call.enqueue(new Callback<InventoryPendingModel>() {
            @Override
            public void onResponse(Call<InventoryPendingModel> call, Response<InventoryPendingModel> response) {
                progressDialog.dismiss();
                try {
                    if (response.body().getStatus().equals("Success")) {
                        String[] Date = response.body().getDataList().get(0).getReqestDate().split("\\s+");
                        rcd_Date.setText(Date[0]);
                        branchName_RCD.setText(response.body().getDataList().get(0).getBranch());
                        rcy_items.setLayoutManager(new LinearLayoutManager(RequisitionControlDetails.this, LinearLayoutManager.VERTICAL, false));
                        rcy_items.setItemAnimator(new DefaultItemAnimator());
                        requestControlDetailList adapter = new requestControlDetailList(RequisitionControlDetails.this, response.body().getDataList());
                        rcy_items.setAdapter(adapter);
                    } else {
                        Toast.makeText(RequisitionControlDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(RequisitionControlDetails.this, "Service Unavailable.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InventoryPendingModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RequisitionControlDetails.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void transferOrderDialog() {
        Dialog dialog = new Dialog(RequisitionControlDetails.this);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.transfer_order_detail);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    public void invalidItemDialog() {
        final Dialog dialog = new Dialog(RequisitionControlDetails.this);
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
                dialog.dismiss();
            }
        });

        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), InventoryPending.class);
        startActivity(intent);
        finish();
    }


}