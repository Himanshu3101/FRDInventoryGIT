package com.yoeki.kalpnay.frdinventry.InventoryCounting;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yoeki.kalpnay.frdinventry.R;

import java.util.List;

public class InventoryCountTemp extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<String> journalAndWarehouse;
    Activity activity;

    public InventoryCountTemp(inventory_Counting activity, List<String> journalAndWarehouse) {
        this.activity = activity;
        this.journalAndWarehouse = journalAndWarehouse;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inventory_counting_temporary, viewGroup, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
        String[] dateTemp = journalAndWarehouse.get(i).split("&");
        itemViewHolder.journalTemp.setText(dateTemp[0]);
        itemViewHolder.wareHouseTemp.setText(dateTemp[1]);

        itemViewHolder.linearLayoutInventoryCountTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] journal = journalAndWarehouse.get(i).split("&");
                Intent intent = new Intent(activity,INVCountingTempDetail.class);
                intent.putExtra("JournalSelect", journal[0]);
                intent.putExtra("WareHouseSelect", journal[1]);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return journalAndWarehouse.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView journalTemp, wareHouseTemp;
        LinearLayout linearLayoutInventoryCountTemp;

        public ItemViewHolder(View itemView) {
            super(itemView);
            linearLayoutInventoryCountTemp = itemView.findViewById(R.id.linearLayoutInventoryCountTemp);
            journalTemp = itemView.findViewById(R.id.journalTemp);
            wareHouseTemp = itemView.findViewById(R.id.wareHouseTemp);
        }
    }
}
