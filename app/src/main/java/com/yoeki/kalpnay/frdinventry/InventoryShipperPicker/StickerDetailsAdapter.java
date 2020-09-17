package com.yoeki.kalpnay.frdinventry.InventoryShipperPicker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.StickersDialogData;
import com.yoeki.kalpnay.frdinventry.R;

import java.util.ArrayList;
import java.util.List;

public class StickerDetailsAdapter extends RecyclerView.Adapter<StickerDetailsAdapter.ViewHolder> {

    private List<StickersDialogData> stickersDialogDataList = new ArrayList<>();
    private Context context;
    private String itemId;

    public StickerDetailsAdapter(String itemId, Context context, List<StickersDialogData> stickersDialogDataList) {
        this.itemId = itemId;
        this.context = context;
        this.stickersDialogDataList = stickersDialogDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater infl = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = infl.inflate(R.layout.sticker_layout, parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int pos) {
        holder.textViewBatch.setText(stickersDialogDataList.get(pos).getBatchNo());
        holder.textViewSticker.setText(stickersDialogDataList.get(pos).getStickerSeq());
    }

    @Override
    public int getItemCount() {
        try {
            return stickersDialogDataList.size();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewSticker;
        TextView textViewBatch;

        public ViewHolder(View view) {
            super(view);
            textViewSticker = view.findViewById(R.id.stickerNumber);
            textViewBatch = view.findViewById(R.id.batchNumber);
        }

    }

}
