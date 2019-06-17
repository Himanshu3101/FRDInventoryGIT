package com.yoeki.kalpnay.frdinventry.Dashboard;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yoeki.kalpnay.frdinventry.Dashboard.InventoryCounting.dashboardInventoryCount;
import com.yoeki.kalpnay.frdinventry.dashboardInventoryRequisition;
import com.yoeki.kalpnay.frdinventry.R;

import java.util.List;

public class selectModuleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_ITEM = 2;

    private List<selectModuleDataModule> stringArrayList;
    private Activity activity;
    String strtemp,layoutCmng;


    public selectModuleAdapter(Activity activity, List<selectModuleDataModule> strings) {
        this.activity = activity;
        this.stringArrayList = strings;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dashboard_grid, parent, false);
        return new selectModuleAdapter.ItemViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final selectModuleAdapter.ItemViewHolder itemViewHolder = (selectModuleAdapter.ItemViewHolder) holder;

        itemViewHolder.selectModuleTxt.setText(stringArrayList.get(position).getModuleName());
//        itemViewHolder.location.setText(stringArrayList.get(position).getLocation());


        itemViewHolder.cardviewSelectModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String moduleSelected = itemViewHolder.selectModuleTxt.getText().toString();
                if(moduleSelected.equals("Inventory Counting")){
                    Intent intent=new Intent(activity, dashboardInventoryCount.class);
                    activity.startActivity(intent);
                }else if(moduleSelected.equals("Item Requisition")){
                    Intent intent=new Intent(activity, dashboardInventoryRequisition.class);
                    activity.startActivity(intent);
                }else{
//                    Intent intent=new Intent(activity, RequisitionDetails.class);
//                    activity.startActivity(intent);
                }
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

        TextView selectModuleTxt;
        CardView cardviewSelectModule;

        public ItemViewHolder(View itemView) {
            super(itemView);
            cardviewSelectModule = itemView.findViewById(R.id.cardviewSelectModule);
            selectModuleTxt = itemView.findViewById(R.id.selectModuleTxt);
        }
    }
}
