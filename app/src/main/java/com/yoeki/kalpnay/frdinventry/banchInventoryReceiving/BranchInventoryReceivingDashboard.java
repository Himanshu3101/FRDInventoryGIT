package com.yoeki.kalpnay.frdinventry.banchInventoryReceiving;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.LinearLayout;

import com.yoeki.kalpnay.frdinventry.R;
import com.yoeki.kalpnay.frdinventry.Dashboard.dashboardNew;

public class BranchInventoryReceivingDashboard extends AppCompatActivity implements View.OnClickListener {

    AppCompatButton backInventoryReceiving;
    LinearLayout linearLayoutCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_inventory_receiving_dashboard);
        initUi();

        linearLayoutCompleted.setOnClickListener(this);
        backInventoryReceiving.setOnClickListener(this);
    }

    public void initUi() {
        linearLayoutCompleted = findViewById(R.id.linearLayoutCompleted);
        backInventoryReceiving = findViewById(R.id.backInventoryReceiving);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearLayoutCompleted:
                startActivity(new Intent(getApplicationContext(), CompletedReceiving.class));
                finish();
                break;
            case R.id.backInventoryReceiving:
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
