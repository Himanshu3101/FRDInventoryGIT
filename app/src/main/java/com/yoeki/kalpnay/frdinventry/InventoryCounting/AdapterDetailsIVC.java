package com.yoeki.kalpnay.frdinventry.InventoryCounting;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yoeki.kalpnay.frdinventry.MRN.Adapter.MRN_Dashboard_AdapterDetails;
import com.yoeki.kalpnay.frdinventry.QRDetails.ResponseBodyQRDetails;
import com.yoeki.kalpnay.frdinventry.R;

import java.util.List;

public class AdapterDetailsIVC extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private List<ResponseBodyQRDetails> stringIVCArrayListDetails;
    String wareHouse;
    int languageChangeVisible = 0;

    public AdapterDetailsIVC(InventoryCountingScan activity, List<ResponseBodyQRDetails> listIVC, String wareHouse) {
        this.activity = activity;
        this.stringIVCArrayListDetails = listIVC;
        this.wareHouse = wareHouse;
    }

    public AdapterDetailsIVC(InventoryCountingScan inventoryCountingScan, List<ResponseBodyQRDetails> listIVC, String wareHouse, int languageChangeVisible) {
        this.activity = inventoryCountingScan;
        this.stringIVCArrayListDetails = listIVC;
        this.wareHouse = wareHouse;
        this.languageChangeVisible = languageChangeVisible;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_inventory_counting, viewGroup, false);
        return new AdapterDetailsIVC.ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;

        if (languageChangeVisible == 0) {
            itemViewHolder.nameIVC.setVisibility(View.VISIBLE);
            itemViewHolder.nameIVCArabic.setVisibility(View.INVISIBLE);
        } else {
            itemViewHolder.nameIVC.setVisibility(View.INVISIBLE);
            itemViewHolder.nameIVCArabic.setVisibility(View.VISIBLE);
        }


        itemViewHolder.itemnoIVC.setText(stringIVCArrayListDetails.get(position).getItemId());
        itemViewHolder.nameIVC.setText(stringIVCArrayListDetails.get(position).getItemName());
        itemViewHolder.nameIVCArabic.setText(stringIVCArrayListDetails.get(position).getItemnameArabic());
        itemViewHolder.batchNoIVC.setText(stringIVCArrayListDetails.get(position).getBatchId());
        itemViewHolder.scanqtyIVC.setText(stringIVCArrayListDetails.get(position).getStickerQty());
        itemViewHolder.wareHouseIVC.setText(wareHouse);
        itemViewHolder.configurationIVC.setText(stringIVCArrayListDetails.get(position).getConfig());
    }

    @Override
    public int getItemCount() {
        return stringIVCArrayListDetails.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView itemnoIVC, nameIVC, nameIVCArabic, batchNoIVC, scanqtyIVC, wareHouseIVC, configurationIVC;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemnoIVC = itemView.findViewById(R.id.itemnoIVC);
            nameIVC = itemView.findViewById(R.id.nameIVC);
            nameIVCArabic = itemView.findViewById(R.id.nameIVCArabic);
            batchNoIVC = itemView.findViewById(R.id.batchNoIVC);
            scanqtyIVC = itemView.findViewById(R.id.scanqtyIVC);
            wareHouseIVC = itemView.findViewById(R.id.wareHouseIVC);
            configurationIVC = itemView.findViewById(R.id.configurationIVC);
        }
    }
}
