package com.yoeki.kalpnay.frdinventry.GenerateListModels;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.yoeki.kalpnay.frdinventry.R;

import java.util.ArrayList;

public class GenerateList extends AppCompatActivity implements View.OnClickListener {
    ImageView img_backgenrate;
    RecyclerView rv_generate;
    ArrayList<GenrateModel> arraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_generate_list);
        initialize();
        arraylist = new ArrayList<>();
        adddata();
        img_backgenrate.setOnClickListener(this);
    }
    public void initialize() {
        img_backgenrate = findViewById(R.id.img_backgenrate);
        rv_generate = findViewById(R.id.rv_generate);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_backgenrate:
                finish();
                break;
        }
    }
    public void adddata(){
        GenrateModel data = new GenrateModel();
        data.setItemname("Rice");
        data.setVarientname("Basmati");
        data.setBatchno("2554");
        data.setExpiredate("15-05-2019");
        data.setAvalqty("Available Qty. 35");
        data.setReqqty("Req Qty. 40");
        arraylist.add(data);

        GenrateModel data2 = new GenrateModel();
        data2.setItemname("Rice");
        data2.setVarientname("Basmati");
        data2.setBatchno("2554");
        data2.setExpiredate("15-05-2019");
        data2.setAvalqty("Available Qty. 35");
        data2.setReqqty("Req Qty. 40");
        arraylist.add(data2);

        GenrateModel data3 = new GenrateModel();
        data3.setItemname("Rice");
        data3.setVarientname("Basmati");
        data3.setBatchno("2554");
        data3.setExpiredate("15-05-2019");
        data3.setAvalqty("Available Qty. 35");
        data3.setReqqty("Req Qty. 40");
        arraylist.add(data3);

        rv_generate.setLayoutManager(new LinearLayoutManager(GenerateList.this, LinearLayoutManager.VERTICAL, false));
        rv_generate.setItemAnimator(new DefaultItemAnimator());

        GenratelistAdapter adapter=new GenratelistAdapter(GenerateList.this,arraylist);
        rv_generate.setAdapter(adapter);
    }
}