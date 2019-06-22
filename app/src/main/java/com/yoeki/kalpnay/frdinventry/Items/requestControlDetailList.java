package com.yoeki.kalpnay.frdinventry.Items;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.detailDataList;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.RequisitionControlDetails;
import com.yoeki.kalpnay.frdinventry.R;

import java.util.ArrayList;
import java.util.List;

public class requestControlDetailList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 2;
    private List<detailDataList> stringArrayList;
    private Activity activity;
    String strtemp, finalUpdate = "0";
    RequisitionControlDetails requisitionControlDetails = new RequisitionControlDetails();
    RequestedItemsFragment requestedItemsFragment = new RequestedItemsFragment();
    Button button_accept, btn_reject;
    LinearLayout linearLayout;
    String clickable = "1";
    ImageView tick, cross, uncross, untick;
    LinearLayout ly_hovers;
    int languageChangeVisible = 0;
    String[] arrayreason = {"Select Reason", "Item expired", "Item not found", "Bad Condition"};

    public requestControlDetailList(Activity activity, List<detailDataList> strings) {
        this.activity = activity;
        this.stringArrayList = strings;
    }

    public requestControlDetailList(RequisitionControlDetails activity, List<detailDataList> listRCDDetailsList, int languageChangeVisible) {
        this.activity = activity;
        this.stringArrayList = listRCDDetailsList;
        this.languageChangeVisible = languageChangeVisible;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_control_details_listdesigen, parent, false);

        return new ItemViewHolderRCD(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolderRCD itemViewHolder = (ItemViewHolderRCD) holder;


        if (languageChangeVisible == 0) {
            itemViewHolder.tv_itemnameinEnglish.setVisibility(View.VISIBLE);
            itemViewHolder.tv_nameinarabic.setVisibility(View.INVISIBLE);
        } else {
            itemViewHolder.tv_itemnameinEnglish.setVisibility(View.INVISIBLE);
            itemViewHolder.tv_nameinarabic.setVisibility(View.VISIBLE);
        }


        itemViewHolder.tv_itemno.setText(stringArrayList.get(position).getItemId());
        itemViewHolder.tv_itemnameinEnglish.setText(stringArrayList.get(position).getItemName());
        itemViewHolder.tv_approved.setText(stringArrayList.get(position).getApprovedQty());
        itemViewHolder.tv_nameinarabic.setText(stringArrayList.get(position).getItemNameArabic());

        itemViewHolder.tv_remainingqty.setText(stringArrayList.get(position).getremainingQty());
        itemViewHolder.tv_pickedqty.setText(stringArrayList.get(position).getpickededQty());
//        itemViewHolder.tv_nameinarabic.setText(stringArrayList.get(position).getReason());


        itemViewHolder.tv_config.setText(stringArrayList.get(position).getConfig());
        itemViewHolder.tv_batchid.setText(stringArrayList.get(position).getBatchId());
        itemViewHolder.tv_expires.setText(stringArrayList.get(position).getExpdate());

        SpinnerAdapter adapter = new SpinnerAdapter(activity, R.layout.adapter_spinner, arrayreason);
        itemViewHolder.spinner_reason.setAdapter(adapter);

        itemViewHolder.spinner_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int spinnerPosition, long id) {
                String selectedItem = parent.getItemAtPosition(spinnerPosition).toString();
                stringArrayList.get(position).setReason(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        itemViewHolder.cardview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                recyclerViewDetailsDialog();
                return false;
            }
        });

      /*  itemViewHolder.updateQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = itemViewHolder.getAdapterPosition();
                update_Qty(position, itemViewHolder.tv_pickedqty);

            }
        });*/
    }

    @Override

    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }
    //kjhgfdssss
    private class ItemViewHolderRCD extends RecyclerView.ViewHolder {
        TextView tv_itemno, tv_approved, tv_pickedqty, tv_remainingqty, tv_nameinarabic, tv_itemnameinEnglish, tv_config, tv_batchid, tv_expires;
        LinearLayout linearLayoutTotal;
        CardView cardview;
        CheckBox checkBox;
        Spinner spinner_reason;

        public ItemViewHolderRCD(View itemView) {
            super(itemView);

            tv_itemno = itemView.findViewById(R.id.tv_itemno);
            tv_approved = itemView.findViewById(R.id.tv_approvedqty);
            tv_nameinarabic = itemView.findViewById(R.id.tv_nameinarabic);
            tv_itemnameinEnglish= itemView.findViewById(R.id.tv_itemnameinEnglish);
            tv_pickedqty = itemView.findViewById(R.id.tv_pickedqty);
            tv_remainingqty = itemView.findViewById(R.id.tv_remainingqty);
//            updateQty = itemView.findViewById(R.id.updateQty);
            cardview = itemView.findViewById(R.id.cardview);
            spinner_reason = itemView.findViewById(R.id.spinner_reason);

            tv_config = itemView.findViewById(R.id.tv_config);
            tv_batchid = itemView.findViewById(R.id.tv_batchid);
            tv_expires = itemView.findViewById(R.id.tv_expires);
        }
    }

    private void recyclerViewDetailsDialog() {
        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.recyclerview_detailsdialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        AppCompatTextView configDialog = dialog.findViewById(R.id.configDialog);
        AppCompatTextView batchNoDialog = dialog.findViewById(R.id.batchNoDialog);
        AppCompatTextView expiry = dialog.findViewById(R.id.expiry);
        AppCompatTextView pckdQty = dialog.findViewById(R.id.pckdQty);

        dialog.show();
    }

    public void update_Qty(final int position, final View tv_pickedqty) {
        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.update_qty);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        final AppCompatAutoCompleteTextView pckd_qty = dialog.findViewById(R.id.pckd_qty);
        final AppCompatAutoCompleteTextView updte_qty = dialog.findViewById(R.id.updte_qty);
        AppCompatButton submitBtn = dialog.findViewById(R.id.submitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String packed = pckd_qty.getText().toString();
                String update = updte_qty.getText().toString();
                if (packed.equals("")) {
                    Toast.makeText(activity, "Please Input Packed Quantity.", Toast.LENGTH_SHORT).show();
                } else if (update.equals("")) {
                    Toast.makeText(activity, "Please Input Update Quantity.", Toast.LENGTH_SHORT).show();
                } else {
                    String finalUpdate = String.valueOf(Integer.parseInt(packed) * Integer.parseInt(update));
                    Toast.makeText(activity, "Updated Complete. " + finalUpdate, Toast.LENGTH_SHORT).show();
                    TextView textView = (TextView) tv_pickedqty;
                    textView.setText(finalUpdate);
                    dialog.dismiss();
                }
            }
        });
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }
}