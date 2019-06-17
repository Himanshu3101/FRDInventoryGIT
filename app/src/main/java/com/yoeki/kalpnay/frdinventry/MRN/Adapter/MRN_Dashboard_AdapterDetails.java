package com.yoeki.kalpnay.frdinventry.MRN.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.util.AdapterListUpdateCallback;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yoeki.kalpnay.frdinventry.MRN.Model.MRNDetailsList;
import com.yoeki.kalpnay.frdinventry.R;

import java.util.List;

public class MRN_Dashboard_AdapterDetails extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 2;
    private List<MRNDetailsList> stringMRNArrayListDetails;
    private Activity activity;
    int languageChangeVisible=0;

    public MRN_Dashboard_AdapterDetails(Activity activity, List<MRNDetailsList> MRN_dashboardAdapterDetails) {
        this.activity = activity;
        this.stringMRNArrayListDetails = MRN_dashboardAdapterDetails;
    }

    public MRN_Dashboard_AdapterDetails(Activity activity, List<MRNDetailsList> listMRNDetailsList, int languageChangeVisible) {
        this.activity = activity;
        this.stringMRNArrayListDetails = listMRNDetailsList;
        this.languageChangeVisible = languageChangeVisible;
    }

//    public MRN_Dashboard_AdapterDetails(MaterialReceivingDetails materialReceivingDetails) {
//
//    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_mrn_details, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
        if(languageChangeVisible==0){
            itemViewHolder.tv_nameMRN.setVisibility(View.VISIBLE);
            itemViewHolder.tv_nameMRNArabic.setVisibility(View.INVISIBLE);
        }else{
            itemViewHolder.tv_nameMRN.setVisibility(View.INVISIBLE);
            itemViewHolder.tv_nameMRNArabic.setVisibility(View.VISIBLE);
        }
        itemViewHolder.tv_itemnoMRN.setText(stringMRNArrayListDetails.get(position).getItemId());
        itemViewHolder.tv_nameMRN.setText(stringMRNArrayListDetails.get(position).getItemname());
        itemViewHolder.tv_nameMRNArabic.setText(stringMRNArrayListDetails.get(position).getItemArabicName());
        itemViewHolder.tv_batchNoMRN.setText(stringMRNArrayListDetails.get(position).getBatchNo());
        itemViewHolder.tv_receivedqty.setText(stringMRNArrayListDetails.get(position).getReceivedQuantity());
        itemViewHolder.tv_configurationMRN.setText(stringMRNArrayListDetails.get(position).getConfig());

        itemViewHolder.tv_scanqty.setText(stringMRNArrayListDetails.get(position).getScanQty());

        String[] split = stringMRNArrayListDetails.get(position).getExpiryDate().split("\\s+");
        itemViewHolder.tv_expiryDateMRN.setText(split[0]);
//        itemViewHolder.tv_scanqty.setText(stringMRNArrayListDetails.get(position).getReceivedQuantity());
    }

    @Override
    public int getItemCount() {
        return stringMRNArrayListDetails.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tv_itemnoMRN, tv_nameMRN, tv_nameMRNArabic,tv_batchNoMRN, tv_receivedqty, tv_expiryDateMRN, tv_scanqty, tv_configurationMRN;
        LinearLayout linearLayoutMRNdetails;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_itemnoMRN = itemView.findViewById(R.id.tv_itemnoMRN);
            tv_nameMRN = itemView.findViewById(R.id.tv_nameMRN);
            tv_nameMRNArabic = itemView.findViewById(R.id.tv_nameMRNArabic);
            tv_batchNoMRN = itemView.findViewById(R.id.tv_batchNoMRN);
            tv_receivedqty = itemView.findViewById(R.id.tv_receivedqty);
            tv_expiryDateMRN = itemView.findViewById(R.id.tv_expiryDateMRN);
            tv_scanqty = itemView.findViewById(R.id.tv_scanqty);
            tv_configurationMRN = itemView.findViewById(R.id.tv_configurationMRN);
            linearLayoutMRNdetails = itemView.findViewById(R.id.linearLayoutMRNdetails);
        }
    }
}






















