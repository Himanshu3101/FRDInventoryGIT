package com.yoeki.kalpnay.frdinventry.Items;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.RequisitionControlDetails;
import com.yoeki.kalpnay.frdinventry.R;
import java.util.List;
public class SeletedItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 2;
    private List<SelecteditemsModel> stringArrayList;
    private Activity activity;
    String strtemp;
    RequisitionControlDetails requisitionDetails = new RequisitionControlDetails();
    Button button_accept, btn_reject;
    LinearLayout linearLayout;
    String clickable = "1";

    public SeletedItemsAdapter(Activity activity, List<SelecteditemsModel> strings) {

        this.activity = activity;
        this.stringArrayList = strings;
      /*  button_accept=  activity.findViewById(R.id.button_accept);
        btn_reject=activity.findViewById(R.id.button_reject);
        linearLayout = activity.findViewById(R.id.layoutView);*/
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_selecteditems, parent, false);

        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
       // itemViewHolder.tv_items.setText(stringArrayList.get(position).getItems());
        itemViewHolder.tv_selectedqty.setText(stringArrayList.get(position).getItems());
        itemViewHolder.tv_varientname.setText(stringArrayList.get(position).getVarientname());
        itemViewHolder.tv_avalqty.setText(stringArrayList.get(position).getQty());
        itemViewHolder.tv_remainingqty.setText(stringArrayList.get(position).getRemainigqty());
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

        TextView tv_items, tv_selectedqty,tv_remainingqty,tv_avalqty,tv_varientname;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_items = itemView.findViewById(R.id.tv_items);
            tv_selectedqty = itemView.findViewById(R.id.tv_selectedqty);
            tv_remainingqty = itemView.findViewById(R.id.tv_remainingqty);
            tv_avalqty = itemView.findViewById(R.id.tv_avalqty);
            tv_varientname = itemView.findViewById(R.id.tv_varientname);
        }
    }
}
