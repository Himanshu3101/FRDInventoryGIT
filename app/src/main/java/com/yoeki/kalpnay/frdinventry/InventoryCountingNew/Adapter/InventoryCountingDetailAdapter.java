package com.yoeki.kalpnay.frdinventry.InventoryCountingNew.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yoeki.kalpnay.frdinventry.InventoryCountingNew.InventoryCountingDetail;
import com.yoeki.kalpnay.frdinventry.InventoryCountingNew.model.QrDetail;
import com.yoeki.kalpnay.frdinventry.R;

import java.util.List;

public class InventoryCountingDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private List<QrDetail> listMatched;
    private String warehouseSelect;
    int languageChangeVisible = 0;

    public InventoryCountingDetailAdapter(InventoryCountingDetail invCountingTempDetail, List<QrDetail> listMatched, String warehouseSelect, int languageChangeVisible) {
        this.activity = invCountingTempDetail;
        this.listMatched = listMatched;
        this.warehouseSelect = warehouseSelect;
        this.languageChangeVisible = languageChangeVisible;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_inventory_counting, viewGroup, false);
        return new ItemViewHolder(itemView);
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

        itemViewHolder.itemnoIVC.setText(listMatched.get(position).getItemId());
        itemViewHolder.nameIVC.setText(listMatched.get(position).getItemName());
        itemViewHolder.nameIVCArabic.setText(listMatched.get(position).getItemnameArabic());
        itemViewHolder.batchNoIVC.setText(listMatched.get(position).getBatchId());
        itemViewHolder.scanqtyIVC.setText(listMatched.get(position).getStickerQty());
        if(activity instanceof InventoryCountingDetail){
            itemViewHolder.scanqtyIVCHead.setText("Quantity :");
        }
        itemViewHolder.wareHouseIVC.setText(warehouseSelect);
        itemViewHolder.configurationIVC.setText(listMatched.get(position).getConfig());
    }

    @Override
    public int getItemCount() {
        return listMatched.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView itemnoIVC, nameIVC, nameIVCArabic, batchNoIVC, scanqtyIVC, wareHouseIVC, configurationIVC;
        TextView scanqtyIVCHead;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemnoIVC = itemView.findViewById(R.id.itemnoIVC);
            nameIVC = itemView.findViewById(R.id.nameIVC);
            nameIVCArabic = itemView.findViewById(R.id.nameIVCArabic);
            batchNoIVC = itemView.findViewById(R.id.batchNoIVC);
            scanqtyIVC = itemView.findViewById(R.id.scanqtyIVC);
            scanqtyIVCHead = itemView.findViewById(R.id.scanqtyIVCHead);
            wareHouseIVC = itemView.findViewById(R.id.wareHouseIVC);
            configurationIVC = itemView.findViewById(R.id.configurationIVC);
        }
    }
}