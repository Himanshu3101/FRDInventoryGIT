package com.yoeki.kalpnay.frdinventry.QRDetailsQuantityUpdate;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yoeki.kalpnay.frdinventry.MRN.Model.MRNDetailsList;
import com.yoeki.kalpnay.frdinventry.QRDetails.ResponseBodyQRDetails;
import com.yoeki.kalpnay.frdinventry.R;

import java.util.List;

import retrofit2.Response;

public class AdapterQrDetailQuantityUpdate extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private List<ResponseBodyQRDetails> responseDetails;
//    private String sequenceQR;

    public AdapterQrDetailQuantityUpdate(QrDetailQuantityUpdate activity, List<ResponseBodyQRDetails> response/*, String qrDetails*/) {
        this.activity = activity;
        this.responseDetails = response;
//        this.sequenceQR = qrDetails;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_adapter_qrdetails_quantityupdate, viewGroup, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
        
        /*if (languageChangeVisible == 0) {
            itemViewHolder.tv_nameMRN.setVisibility(View.VISIBLE);
            itemViewHolder.tv_nameMRNArabic.setVisibility(View.INVISIBLE);
        } else {
            itemViewHolder.tv_nameMRN.setVisibility(View.INVISIBLE);
            itemViewHolder.tv_nameMRNArabic.setVisibility(View.VISIBLE);
        }*/

        itemViewHolder.tv_batchNoQuantityUpdate.setText(responseDetails.get(position).getBatchId());
//        itemViewHolder.tv_expiryDateQuantityUpdate.setText(responseDetails.get(position).getItemname());
        itemViewHolder.tv_configurationQuantityUpdate.setText(responseDetails.get(position).getConfig());
        itemViewHolder.tv_scanqtyQuantityUpdate.setText(responseDetails.get(position).getConsumeQty());
        itemViewHolder.tv_itemnoQuantityUpdate.setText(responseDetails.get(position).getItemId());
//        itemViewHolder.sequenceNo_QuantityUpdate.setText(sequenceQR);
        itemViewHolder.tv_nameArabicQuantityUpdate.setText(responseDetails.get(position).getItemnameArabic());
        String[] split = responseDetails.get(position).getExpdate().split("\\s+");
        itemViewHolder.tv_expiryDateQuantityUpdate.setText(split[0]);
    }

    @Override
    public int getItemCount()  {
        return responseDetails.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tv_batchNoQuantityUpdate, tv_expiryDateQuantityUpdate, tv_configurationQuantityUpdate, tv_scanqtyQuantityUpdate, tv_itemnoQuantityUpdate,
                sequenceNo_QuantityUpdate,tv_nameArabicQuantityUpdate;
        

        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_batchNoQuantityUpdate = itemView.findViewById(R.id.tv_batchNoQuantityUpdate);
            tv_expiryDateQuantityUpdate = itemView.findViewById(R.id.tv_expiryDateQuantityUpdate);
            tv_configurationQuantityUpdate = itemView.findViewById(R.id.tv_configurationQuantityUpdate);
            tv_scanqtyQuantityUpdate = itemView.findViewById(R.id.tv_scanqtyQuantityUpdate);
            tv_itemnoQuantityUpdate = itemView.findViewById(R.id.tv_itemnoQuantityUpdate);
            sequenceNo_QuantityUpdate = itemView.findViewById(R.id.sequenceNo_QuantityUpdate);
            tv_nameArabicQuantityUpdate = itemView.findViewById(R.id.tv_nameArabicQuantityUpdate);
        }
    }
}
