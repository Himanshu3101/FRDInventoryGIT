package com.yoeki.kalpnay.frdinventry.MRN.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yoeki.kalpnay.frdinventry.MRN.MaterialReceivingDetails;
import com.yoeki.kalpnay.frdinventry.Model.MRN.MRNList;
import com.yoeki.kalpnay.frdinventry.R;

import java.util.List;

public class MRN_Dashboard_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int TYPE_ITEM = 2;
        private List<MRNList> stringMRNArrayList;
        private Activity activity;

    public MRN_Dashboard_Adapter(Activity activity, List<MRNList> strings) {
        this.activity = activity;
        this.stringMRNArrayList = strings;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mrndashboard_list, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.mrnNo.setText(stringMRNArrayList.get(position).getMRNNumber());
        itemViewHolder.activityNo.setText(stringMRNArrayList.get(position).getActivityNumber());
        itemViewHolder.date.setText(stringMRNArrayList.get(position).getMRNDate());

        itemViewHolder.linearLayoutMRN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = itemViewHolder.getAdapterPosition();
                String MRNumber = stringMRNArrayList.get(position).getMRNNumber().toString();
                String activityNo = stringMRNArrayList.get(position).getActivityNumber().toString();
                Intent intent = new Intent(activity, MaterialReceivingDetails.class);
                intent.putExtra("MRNNumber",MRNumber);
                intent.putExtra("activityNo",activityNo);
                activity.startActivity(intent);
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
        return stringMRNArrayList.size();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView mrnNo, activityNo, date;
        LinearLayout linearLayoutMRN;

        public ItemViewHolder(View itemView) {
            super(itemView);

            mrnNo = itemView.findViewById(R.id.mrnNo);
            activityNo = itemView.findViewById(R.id.activityNo);
            date = itemView.findViewById(R.id.date);
            linearLayoutMRN = itemView.findViewById(R.id.linearLayoutMRN);
        }
    }
}
