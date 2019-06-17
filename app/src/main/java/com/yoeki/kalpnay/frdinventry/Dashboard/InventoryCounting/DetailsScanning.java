package com.yoeki.kalpnay.frdinventry.Dashboard.InventoryCounting;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.yoeki.kalpnay.frdinventry.R;

import java.util.ArrayList;
import java.util.List;

public class DetailsScanning extends RecyclerView.Adapter<DetailsScanning.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private static Context context;

    public DetailsScanning(Context context, ArrayList<String> statementPopupList) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = statementPopupList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.data_scanning/*details_scanning_recycler*/, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String[] Dataa = mData.get(position).split(",");
        holder.number.setText(Dataa[0]);
        holder.name.setText(Dataa[1]);
        holder.nameArabic.setText(Dataa[2]);

        holder.detailing.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.detailing_dialog);
                TextView numberD = dialog.findViewById(R.id.numberD);
//                TextView nameD = dialog.findViewById(R.id.nameD);
//                TextView nameArabicD = dialog.findViewById(R.id.nameArabicD);
                TextView onHandD = dialog.findViewById(R.id.onHandD);
                TextView wareHouseD = dialog.findViewById(R.id.wareHouseD);
                TextView countingD = dialog.findViewById(R.id.countingD);
                TextView differenceD = dialog.findViewById(R.id.differenceD);
                numberD.setText(Dataa[0]);
//                nameD.setText(Dataa[1]);
//                nameArabicD.setText(Dataa[2]);
                onHandD.setText(Dataa[3]);
                wareHouseD.setText(Dataa[4]);
                countingD.setText(Dataa[5]);
                differenceD.setText(Dataa[6]);
                dialog.show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView number,name,nameArabic/*,onHand,wareHouse,counting,difference*/;
        AppCompatButton detailing;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            name = itemView.findViewById(R.id.name);
            nameArabic = itemView.findViewById(R.id.nameArabic);
            detailing =  itemView.findViewById(R.id.detailing);
//            onHand = itemView.findViewById(R.id.onHand);
//            wareHouse = itemView.findViewById(R.id.wareHouse);
//            counting = itemView.findViewById(R.id.counting);
//            difference = itemView.findViewById(R.id.difference);
        }


    }
}
