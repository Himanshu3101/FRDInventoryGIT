package com.yoeki.kalpnay.frdinventry.Dashboard.InventoryCounting;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.yoeki.kalpnay.frdinventry.Login.LoginActivity;
import com.yoeki.kalpnay.frdinventry.R;

public class dashboardInventoryCount extends AppCompatActivity implements View.OnClickListener{
//    Spinner spinnerBook;
    ImageView img_logoutCount;
    AppCompatEditText describeEdit,spinnerBook;
    com.yoeki.kalpnay.frdinventry.custom_control.Custom_Button submitBook;
//    String []booksList={"ABC","DEF","GHI","JKL","MNO","PQR"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dashboard_inventory_count);

        initialize();
        submitBook.setOnClickListener(this);
        img_logoutCount.setOnClickListener(this);
//        SpinnerAdapter adapter=new SpinnerAdapter(getApplicationContext(),R.layout.adapter_spinner, booksList);
//        spinnerBook.setAdapter(adapter);

    }

    public void initialize(){
        spinnerBook=findViewById(R.id.spinnerBook);
        describeEdit=findViewById(R.id.describeEdit);
        submitBook=findViewById(R.id.submitBook);
        img_logoutCount=findViewById(R.id.img_logoutCount);
    }

    @Override
    public void onBackPressed() {
        openLoginDialog(R.style.DialogAnimation);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submitBook:
                String book = spinnerBook.getText().toString();
                if(describeEdit.getText().toString().equals("")){
                    describeEdit.setText(book);
                }
                Intent formIntent=new Intent(dashboardInventoryCount.this, dataScanOnGrid.class);
                startActivity(formIntent);
                finish();
                break;
            case R.id.img_logoutCount:
                openLoginDialog(R.style.DialogAnimation);
                break;
        }
    }

    private void openLoginDialog(int dialogAnimation) {
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
                Intent openRef=new Intent(dashboardInventoryCount.this, LoginActivity.class);
                startActivity(openRef);
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }
}
