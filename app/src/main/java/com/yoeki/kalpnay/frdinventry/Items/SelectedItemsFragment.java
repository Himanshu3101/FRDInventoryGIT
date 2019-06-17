package com.yoeki.kalpnay.frdinventry.Items;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.yoeki.kalpnay.frdinventry.R;
import java.util.ArrayList;

public class SelectedItemsFragment  extends Fragment {

    View view;

    RecyclerView rcy_selecteditems;
    ArrayList<SelecteditemsModel> listdashboard;

    public static SelectedItemsFragment newInstance(){

        SelectedItemsFragment fragment = new SelectedItemsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        view = inflater.inflate(R.layout.fragmnet_selecteditems, container, false);
        rcy_selecteditems = view.findViewById(R.id.rcy_selecteditems);

        dashboarddata();
        return view;
    }

    public void dashboarddata(){

        listdashboard=new ArrayList<>();

        SelecteditemsModel data=new SelecteditemsModel();
        data.setItems("Rice");
        data.setQty("Aval.qty: 35");
        data.setVarientname("Basmati Rice");
        data.setRemainigqty("Remaining qty. 10");

        listdashboard.add(data);

        SelecteditemsModel data1=new SelecteditemsModel();
        data1.setItems("Rice");
        data1.setQty("Aval.qty: 45");
        data1.setVarientname("Basmati Rice");
        data1.setRemainigqty("Remaining qty. 20");
        listdashboard.add(data1);

        SelecteditemsModel data2=new SelecteditemsModel();
        data2.setItems("Sugar");
        data2.setQty("Aval.qty: 55");
        data2.setVarientname("Sugar1");
        data2.setRemainigqty("Remaining qty. 20");
        listdashboard.add(data2);

        SelecteditemsModel data3=new SelecteditemsModel();
        data3.setItems("Rice");
        data3.setQty("Aval.qty: 55");
        data3.setVarientname("Basmati Rice");
        data3.setRemainigqty("Remaining qty. 20");
        listdashboard.add(data3);

        SelecteditemsModel data4=new SelecteditemsModel();
        data4.setItems("Sugar");
        data4.setQty("Aval.qty: 55");
        data4.setVarientname("Sugar1");
        data4.setRemainigqty("Remaining qty. 20");
        listdashboard.add(data4);

        rcy_selecteditems.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rcy_selecteditems.addItemDecoration(new DividerItemDecoration(rcy_selecteditems.getContext(), DividerItemDecoration.VERTICAL));
        rcy_selecteditems.setItemAnimator(new DefaultItemAnimator());

        SeletedItemsAdapter adapter=new SeletedItemsAdapter(getActivity(),listdashboard);
        rcy_selecteditems.setAdapter(adapter);
    }
}