package com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.ItemRequisition;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.DataList;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.RequisitionControlDetails;
import com.yoeki.kalpnay.frdinventry.R;

import java.util.List;

public class DAshboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_ITEM = 2;

    private List<DataList> stringArrayList;
    private Activity activity;
    String strtemp,layoutCmng;

    public DAshboardAdapter(Activity activity, List<DataList> strings/*, String layout*/) {
        this.activity = activity;
        this.stringArrayList = strings;
//        this.layoutCmng = layout;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflating recycle view item layout
        View itemView;
//        if(layoutCmng.equals("Grid")){
//            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dashboard_grid, parent, false);
//        }else{
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_inventory_shippick_dashboard_list, parent, false);
//        }
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.requisition.setText(stringArrayList.get(position).getRequisitionNo());
        itemViewHolder.location.setText(stringArrayList.get(position).getwareHouseName());


        itemViewHolder.linearLayoutRequisition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, RequisitionControlDetails.class);
                intent.putExtra("RequisitionNo",(stringArrayList.get(position).getRequisitionNo()));
                intent.putExtra("wareHouse",(stringArrayList.get(position).getwareHouseName()));
                intent.putExtra("locationCode",(stringArrayList.get(position).getLocation()));
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView requisition,location;
        LinearLayout linearLayoutRequisition;

        public ItemViewHolder(View itemView) {
            super(itemView);
//            tv_text1 = itemView.findViewById(R.id.tv_text1);
            requisition = itemView.findViewById(R.id.requisition);
            location = itemView.findViewById(R.id.location);
            linearLayoutRequisition = itemView.findViewById(R.id.linearLayoutRequisition);
        }
    }
}














