package com.yoeki.kalpnay.frdinventry.banchInventoryReceiving.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yoeki.kalpnay.frdinventry.R;
import com.yoeki.kalpnay.frdinventry.banchInventoryReceiving.InventoryControlDetail;
import com.yoeki.kalpnay.frdinventry.banchInventoryReceiving.model.CompletedReceivingModel;

import java.util.List;

public class CompletedReceivingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 2;

    private List<CompletedReceivingModel> completedReceivingModelList;
    private Activity activity;

    public CompletedReceivingAdapter(Activity activity, List<CompletedReceivingModel> list) {
        this.activity = activity;
        this.completedReceivingModelList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_completed_receiving_list, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.requisition.setText(completedReceivingModelList.get(position).getRequisitionNo());
        itemViewHolder.location.setText(completedReceivingModelList.get(position).getLocation());


        itemViewHolder.linearLayoutRequisition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, InventoryControlDetail.class));
                activity.finish();
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return completedReceivingModelList.size();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView requisition, location;
        LinearLayout linearLayoutRequisition;

        public ItemViewHolder(View itemView) {
            super(itemView);
            requisition = itemView.findViewById(R.id.requisition);
            location = itemView.findViewById(R.id.location);
            linearLayoutRequisition = itemView.findViewById(R.id.linearLayoutRequisition);
        }
    }

}
