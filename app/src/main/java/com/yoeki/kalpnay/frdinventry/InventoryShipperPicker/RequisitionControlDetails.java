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

public class RequisitionControlDetails extends AppCompatActivity implements View.OnClickListener{

    RecyclerView rcy_items;
    ImageView img_back;
    Button btn_accept,btn_reject;
    LinearLayout linearLayoutView;
    private Activity activity;
    CardView cardView;
    LinearLayout frameLayout;
    CheckBox checkBox;
    ImageView img_cross,img_generate,img_ungenerate;
    TabLayout tabs_requesttab;
    ViewPager viewpager_requestepager;
    ArrayList<ItemsModel>arraylist;
    AppCompatButton shipBtn;
    AppCompatAutoCompleteTextView scanQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.request_control_detail);

        setupWindowAnimations();

        arraylist=new ArrayList<>();

        Initialize();

        addlistdata();
        //setupViewPager(viewpager_requestepager);
        //tabs_requesttab.setupWithViewPager(viewpager_requestepager);
        img_back.setOnClickListener(this);
        shipBtn.setOnClickListener(this);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String qr = scanQR.getText().toString();

                if(qr.equals("123456789")) {
                    invalidItemDialog();
                }
            }
        };
        scanQR.addTextChangedListener(textWatcher);

        /*img_cross.setOnClickListener(this);
        img_generate.setOnClickListener(this);
        img_ungenerate.setOnClickListener(this);*/
        //Tabcolorchange();
         }
    public void Initialize(){
//        img_cross=findViewById(R.id.img_cross);
        scanQR = findViewById(R.id.scanQR);
        shipBtn = findViewById(R.id.shipBtn);
        img_back = findViewById(R.id.img_back);
        rcy_items=findViewById(R.id.rcy_items);
//        img_generate = findViewById(R.id.img_generate);
//        img_ungenerate = findViewById(R.id.img_ungenerate);
        // viewpager_requestepager=findViewById(R.id.viewpager_requestepager);
    }

    public void Tabcolorchange(){
        tabs_requesttab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //tabs_requesttab.setBackgroundColor(getResources().getColor(R.color.red));
               // tabs_requesttab.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                tabs_requesttab.setSelectedTabIndicatorColor(Color.parseColor("#289086"));
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //tabs_requesttab.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
               // tabs_requesttab.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                tabs_requesttab.setSelectedTabIndicatorColor(Color.parseColor("#289086"));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
      /* viewpager_requestepager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
           @Override
           public void onPageScrolled(int i, float v, int i1) {

           }

           @Override
           public void onPageSelected(int position) {
                for(int i= 0; i<tabs_requesttab.getTabCount();i++){
                    if(i==position){
                        tabs_requesttab.getTabAt(i).getCustomView().setBackgroundColor(R.color.red);
                    }else{
                        tabs_requesttab.getTabAt(i).getCustomView().setBackgroundColor(R.color.colorPrimary);
                    }
                }
           }

           @Override
           public void onPageScrollStateChanged(int i) {

           }
       });*/
    }

     private void setupWindowAnimations(){
        Fade fade = new Fade();
        fade.setDuration(2000);
        getWindow().setEnterTransition(fade);
         }

    @Override
    public void onClick(View v) {
           switch (v.getId()){
               case R.id.img_back:
                   Intent intent = new Intent(getApplicationContext(), InventoryPending.class);
                   startActivity(intent);
                   finish();
                   break;

               case R.id.shipBtn:
                   transferOrderDialog();
                   break;
//
//               case R.id.img_generate:
//                   Intent intent = new Intent(RequisitionControlDetails.this, GenerateList.class);
//                   startActivity(intent);
//                   img_ungenerate.setVisibility(View.GONE);
//                   break;
           }
    }

    private void rejectDialog(){
        final Dialog dialog=new Dialog(this);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reject_dialog_layout);
       // dialog.getWindow().getAttributes().windowAnimations=dialogAnimation;
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
        Button no=dialog.findViewById(R.id.button_no);
        Button yes = dialog.findViewById(R.id.button_yes);

         no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                dialog.dismiss();
            }
          });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequisitionControlDetails.this, dashboardInventoryRequisition.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();
      }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new RequestedItemsFragment(), "Requested Items");
        adapter.addFragment(new SelectedItemsFragment(), "Selected Items");
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public  void addlistdata(){
        final ProgressDialog progressDialog = new ProgressDialog(RequisitionControlDetails.this);
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        progressDialog.setCancelable(false); // set cancelable to false
        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);

        String userId = Preference.getInstance(getApplicationContext()).getUserId();
        String requisition = getIntent().getStringExtra("RequisitionNo");

        ParticularRequisitionDetails userIDModeParticularRequisitionDetailsl = new ParticularRequisitionDetails(userId,requisition);
        Call<InventoryPendingModel> call = apiInterface.inventoryPicker(userIDModeParticularRequisitionDetailsl);
        call.enqueue(new Callback<InventoryPendingModel>() {
            @Override
            public void onResponse(Call<InventoryPendingModel> call, Response<InventoryPendingModel> response) {
                progressDialog.dismiss();
                if (response.body().getStatus().equals("Success")) {
                    rcy_items.setLayoutManager(new LinearLayoutManager(RequisitionControlDetails.this, LinearLayoutManager.VERTICAL, false));
                    rcy_items.setItemAnimator(new DefaultItemAnimator());
                    requestControlDetailList adapter=new requestControlDetailList(RequisitionControlDetails.this,response.body().getDataList());
                    rcy_items.setAdapter(adapter);
                } else {
                    Toast.makeText(RequisitionControlDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InventoryPendingModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RequisitionControlDetails.this, "Something problem occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void transferOrderDialog(){
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

    public void invalidItemDialog(){
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