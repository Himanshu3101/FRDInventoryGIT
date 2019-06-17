package com.yoeki.kalpnay.frdinventry.Items;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.yoeki.kalpnay.frdinventry.R;
import java.util.ArrayList;

public class RequestedItemsFragment extends Fragment {
    View view;
    RecyclerView  rcy_requesteditems;
    ArrayList<ItemsModel> listdashboard;
    LinearLayout ly_hover;
    String clicable="1";

    public static RequestedItemsFragment newInstance() {
        RequestedItemsFragment fragment = new RequestedItemsFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_requesteditems, container, false);
        rcy_requesteditems=view.findViewById(R.id.rcy_requesteditems);
        ly_hover = view.findViewById(R.id.ly_hover);
        return view;
    }

}