package com.yoeki.kalpnay.frdinventry.GenerateListModels;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.RequisitionControlDetails;
import com.yoeki.kalpnay.frdinventry.R;

import java.util.List;

public class GenratelistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 2;
    private List<GenrateModel> stringArrayList;
    private Activity activity;
    String strtemp;
    RequisitionControlDetails requisitionDetails = new RequisitionControlDetails();
    Button button_accept, btn_reject;
    LinearLayout linearLayout;
    String clickable = "1";

    public GenratelistAdapter(Activity activity, List<GenrateModel> strings) {

        this.activity = activity;
        this.stringArrayList = strings;
      /*  button_accept=  activity.findViewById(R.id.button_accept);
        btn_reject=activity.findViewById(R.id.button_reject);
        linearLayout = activity.findViewById(R.id.layoutView);*/
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_genratelist, parent, false);

        return new ItemViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

        itemViewHolder.tv_itemname.setText(stringArrayList.get(position).getItemname());
        itemViewHolder.tv_varientname.setText(stringArrayList.get(position).getVarientname());
        itemViewHolder.tv_batchno.setText(stringArrayList.get(position).getBatchno());
        itemViewHolder.tv_expiredate.setText(stringArrayList.get(position).getExpiredate());
        itemViewHolder.tv_reqqty.setText(stringArrayList.get(position).getReqqty());
        itemViewHolder.tv_genavalqty.setText(stringArrayList.get(position).getAvalqty());

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
        TextView tv_itemname, tv_varientname,tv_batchno,tv_avalqty,tv_expiredate,tv_reqqty,tv_genavalqty;
        EditText edt_avlqty;
        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_itemname = itemView.findViewById(R.id.tv_itemname);
            tv_varientname = itemView.findViewById(R.id.tv_varientname);
            tv_batchno = itemView.findViewById(R.id.tv_batchno);
            tv_expiredate = itemView.findViewById(R.id.tv_expiredate);
            tv_reqqty = itemView.findViewById(R.id.tv_reqqty);
            tv_genavalqty = itemView.findViewById(R.id.tv_genavalqty);
            edt_avlqty = itemView.findViewById(R.id.edt_avlqty);
        }
    }
}

