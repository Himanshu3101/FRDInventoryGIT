package com.yoeki.kalpnay.frdinventry;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.ItemRequisition.DAshboardAdapter;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.ItemRequisition.DashboardModel;
import com.yoeki.kalpnay.frdinventry.Login.LoginActivity;

import java.util.ArrayList;

public class dashboardInventoryRequisition extends AppCompatActivity implements View.OnClickListener{
         RecyclerView rycdashboard;
         ArrayList<DashboardModel>listdashboard;
         AnimatorSet slidedown;
         String layout = "Grid";
         ImageView img_logoutInvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
//              getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.inventory_ship_pick_dashboard);
        initialize();
//             img_logout.setOnClickListener(this);
//             swtch_View.setOnClickListener(this);
        listdashboard=new ArrayList<>();
        img_logoutInvent.setOnClickListener(this);
//        dashboarddata();
        }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(getApplicationContext(), selectModule.class);
//        startActivity(intent);
//        finish();
        openLogOutDialog(R.style.DialogAnimation,"");
//        new AlertDialog.Builder(dashboardInventoryRequisition.this)
//                .setIcon(R.mipmap.alertdialog)
//                .setTitle("Alert")
//                .setMessage("Are you sure you want to exit?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
//                    @Override
//                    public void onClick(DialogInterface dialog, int i){
//                        finish();
//                        }
//                    })
//                .setNegativeButton("No", null)
//                .show();
           }

    public void initialize(){
        rycdashboard=findViewById(R.id.rycdashboard);
//        img_logoutInvent=findViewById(R.id.img_logoutInvent);
        slidedown= (AnimatorSet) AnimatorInflater.loadAnimator(dashboardInventoryRequisition.this,R.animator.slide_down);
     }

   /* public void dashboarddata(){

        RecyclerView.LayoutManager layoutManager;

        DashboardModel data=new DashboardModel();
//        data.setText1("Item Requisition No.");
        data.setRequisition("12356478951125");
        data.setLocation("Noida");
        listdashboard.add(data);

        DashboardModel data1=new DashboardModel();
//        data1.setText1("Item Requisition No.");
        data1.setRequisition("12356478951125");
        data1.setLocation("Ghaziabad");
        listdashboard.add(data1);

        DashboardModel data2=new DashboardModel();
//        data2.setText1("Item Requisition No.");
        data2.setRequisition("12356478951125");
        data2.setLocation("Delhi");
        listdashboard.add(data2);

        DashboardModel data3=new DashboardModel();
//        data3.setText1("Item Requisition No.");
        data3.setRequisition("12356478951125");
        data3.setLocation("Gurugaon");
        listdashboard.add(data3);

        DashboardModel data4=new DashboardModel();
//        data4.setText1("Item Requisition No.");
        data4.setRequisition("12356478951125");
        data4.setLocation("Gurugaon");
        listdashboard.add(data4);

        DashboardModel data5=new DashboardModel();
//        data5.setText1("Req.No.");
        data5.setRequisition("12356478951125");
        data5.setLocation("Noida");
        listdashboard.add(data5);

        DashboardModel data6=new DashboardModel();
//        data6.setText1("Item Requisition No.");
        data6.setRequisition("12356478951125");
        data6.setLocation("Noida");
        listdashboard.add(data6);

        DashboardModel data7=new DashboardModel();
//        data7.setText1("Item Requisition No.");
        data7.setRequisition("12356478951125");
        data7.setLocation("Noida");
        listdashboard.add(data7);

        DashboardModel data8=new DashboardModel();
//        data8.setText1("Item Requisition No.");
        data8.setRequisition("12356478951125");
        data8.setLocation("Gurugaon");
        listdashboard.add(data8);

        DashboardModel data9=new DashboardModel();
//        data9.setText1("Item Requisition No.");
        data9.setRequisition("12356478951125");
        data9.setLocation("Gurugaon");
        listdashboard.add(data9);

        DashboardModel data10=new DashboardModel();
        data10.setRequisition("12356478951125");
        data10.setLocation("Gurugaon");
        listdashboard.add(data10);

        DashboardModel data11=new DashboardModel();
        data11.setRequisition("12356478951125");
        data11.setLocation("Gurugaon");
        listdashboard.add(data11);


        rycdashboard.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        DAshboardAdapter adapter=new DAshboardAdapter(dashboardInventoryRequisition.this,listdashboard*//*,layout*//*);
        rycdashboard.setLayoutManager(layoutManager);
        rycdashboard.setAdapter(adapter);
      }*/

    private void openLogOutDialog(int dialogAnimation, String s) {
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
                Intent openRef=new Intent(dashboardInventoryRequisition.this,LoginActivity.class);
                startActivity(openRef);
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }

    public void changepassworddialog(Animator slidedown) {
        final Dialog dialog = new Dialog(dashboardInventoryRequisition.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_changepassword);
        slidedown.setTarget(dialog);
        slidedown.start();
        dialog.show();
     }

    @Override
    public void onClick(View v) {
         switch (v.getId()){
//             case R.id.img_logoutInvent:
//                 openLogOutDialog(R.style.DialogAnimation,"");
//                 break;
         }
    }
}
