package com.yoeki.kalpnay.frdinventry.Dashboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.yoeki.kalpnay.frdinventry.Login.LoginActivity;
import com.yoeki.kalpnay.frdinventry.R;

import java.util.ArrayList;

public class selectModule extends AppCompatActivity implements View.OnClickListener{
    RecyclerView rycfrEntry;
    ImageView img_logout;
    ArrayList<selectModuleDataModule> listSelectModuleDataModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_module);
        initialize();

        listSelectModuleDataModule = new ArrayList<>();
        img_logout.setOnClickListener(this);
        dashboarddata();
    }

    public void initialize(){
        rycfrEntry=findViewById(R.id.rycfrEntry);
        img_logout=findViewById(R.id.img_logout);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(selectModule.this)
                .setIcon(R.mipmap.alertdialog)
                .setTitle("Alert")
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int i){
                        finish();
                        }
                    })
                .setNegativeButton("No", null)
                .show();
    }

    public void dashboarddata(){

        RecyclerView.LayoutManager layoutManager;

        selectModuleDataModule moduleDataModule =new selectModuleDataModule();
        moduleDataModule.setModuleName("Inventory Counting");
        listSelectModuleDataModule.add(moduleDataModule);

        selectModuleDataModule moduleDataModule1=new selectModuleDataModule();
        moduleDataModule1.setModuleName("Item Requisition");
        listSelectModuleDataModule.add(moduleDataModule1);

        selectModuleDataModule moduleDataModule2=new selectModuleDataModule();
        moduleDataModule2.setModuleName("Item Receiving");
        listSelectModuleDataModule.add(moduleDataModule2);

        rycfrEntry.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getApplicationContext(),2);
        rycfrEntry.setLayoutManager(layoutManager);

        selectModuleAdapter adapter=new selectModuleAdapter(selectModule.this,listSelectModuleDataModule);
        rycfrEntry.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_logout:
                openLoginDialog(R.style.DialogAnimation,"");
                break;
        }
    }

    private void openLoginDialog(int dialogAnimation, String s) {
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

        Button button_yes=dialog.findViewById(R.id.button_yes);
        Button button_no=dialog.findViewById(R.id.button_no);

        button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openRef=new Intent(selectModule.this, LoginActivity.class);
                startActivity(openRef);
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }
}
