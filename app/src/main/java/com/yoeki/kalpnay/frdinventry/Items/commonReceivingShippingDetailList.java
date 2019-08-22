package com.yoeki.kalpnay.frdinventry.Items;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yoeki.kalpnay.frdinventry.Api.Preference;
import com.yoeki.kalpnay.frdinventry.Dashboard.ReasonModule;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.commonReceivingShippingDetailDataList;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.RequisitionControlDetails;
import com.yoeki.kalpnay.frdinventry.R;
import com.yoeki.kalpnay.frdinventry.banchInventoryReceiving.InventoryControlDetail;
import com.yoeki.kalpnay.frdinventry.banchInventoryReceiving.model.SequenceQuanitiy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class commonReceivingShippingDetailList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 2;
    private List<commonReceivingShippingDetailDataList> stringArrayList;
    private Activity activity;
    commonReceivingShippingDetailList adapter;
    int languageChangeVisible = 0;
    //    String[] arrayreason = {"Select Reason", "Item expired", "Item not found", "Bad Condition"};
    String cometoWhere = "";
    List<ReasonModule> reasonList = new ArrayList<>();
    List<String> arrayreason;
    int getdataBySize = 0;

    public commonReceivingShippingDetailList(RequisitionControlDetails activity, List<commonReceivingShippingDetailDataList> strings, String cometoWhere) {
        this.activity = activity;
        this.stringArrayList = strings;
        this.cometoWhere = cometoWhere;
    }

    public commonReceivingShippingDetailList(InventoryControlDetail inventoryControlDetail, List<commonReceivingShippingDetailDataList> listRCDDetailsList, String cometoWhere) {
        this.activity = inventoryControlDetail;
        this.stringArrayList = listRCDDetailsList;
        this.cometoWhere = cometoWhere;
    }

    public commonReceivingShippingDetailList(RequisitionControlDetails activity, List<commonReceivingShippingDetailDataList> listRCDDetailsList, int languageChangeVisible) {
        this.activity = activity;
        this.stringArrayList = listRCDDetailsList;
        this.languageChangeVisible = languageChangeVisible;
    }

    public commonReceivingShippingDetailList(InventoryControlDetail inventoryControlDetail, List<commonReceivingShippingDetailDataList> listRCDDetailsList, int languageChangeVisible) {
        this.activity = inventoryControlDetail;
        this.stringArrayList = listRCDDetailsList;
        this.languageChangeVisible = languageChangeVisible;
    }

    public commonReceivingShippingDetailList() {
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_control_details_listdesigen, parent, false);
        arrayreason = new ArrayList<>();
        reasonList = Preference.getInstance(activity).getReasonModule();
        arrayreason.add("Select Reason");
        try {
            for (int i = 0; i <= reasonList.size() - 1; ) {
                String reasonLists = reasonList.get(i).getReason().toString();
                arrayreason.add(reasonLists);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ItemViewHolderRCD(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
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

        itemViewHolder.tv_nameinarabic.setText(stringArrayList.get(position).getItemNameArabic());

        itemViewHolder.tv_remainingqty.setText(stringArrayList.get(position).getremainingQty());
        itemViewHolder.tv_pickedqty.setText(stringArrayList.get(position).getpickededQty());
        itemViewHolder.tv_TONumber.setText(stringArrayList.get(position).getTONum());


        itemViewHolder.tv_config.setText(stringArrayList.get(position).getConfig());
        itemViewHolder.tv_batchid.setText(stringArrayList.get(position).getBatchId());
        itemViewHolder.tv_expires.setText(stringArrayList.get(position).getExpdate());

        if (cometoWhere.equals("BIReceiving")) {

            //For Reason
            itemViewHolder.spinner_reason.setVisibility(View.GONE);
            itemViewHolder.reasons.setVisibility(View.GONE);
            itemViewHolder.tv_remainingqty.setVisibility(View.GONE);
            itemViewHolder.remaining.setVisibility(View.GONE);

            itemViewHolder.shipped.setVisibility(View.VISIBLE);
            itemViewHolder.approved.setVisibility(View.GONE);
            itemViewHolder.tv_approved.setText(stringArrayList.get(position).getShippedQty());
            try {
                if (stringArrayList.get(position).getpickededQty() != null) {
                    if (stringArrayList.get(position).getShippedQty() != null) {
                        float pickedQty = Float.parseFloat(stringArrayList.get(position).getpickededQty());
                        float ShippedQty = Float.parseFloat(stringArrayList.get(position).getShippedQty());
                        if (pickedQty == ShippedQty) {


                            //For Reason
                          /*  int forReason = 0;
                            if (stringArrayList.get(position).getBatchListReceiver() == null) {
                                itemViewHolder.spinner_reason.setEnabled(false);
                            } else if (stringArrayList.get(position).getBatchListReceiver().size() == 0) {
                                if (stringArrayList.get(position).getBatchListReceiver().get(position).getReturnQty() == null) {
                                    itemViewHolder.spinner_reason.setEnabled(false);
                                } else if (stringArrayList.get(position).getBatchListReceiver().get(position).getReturnQty() == "0") {
                                    itemViewHolder.spinner_reason.setEnabled(false);
                                } else {
                                    itemViewHolder.spinner_reason.setEnabled(true);
                                }
                            } else {
                                for (int i = 0; i <= stringArrayList.get(position).getBatchListReceiver().size() - 1; i++) {

                                    String returnQty = stringArrayList.get(position).getBatchListReceiver().get(i).getReturnQty();

                                    if (!returnQty.equals("0")) {
                                        itemViewHolder.spinner_reason.setEnabled(true);
                                        forReason = 0;
                                        break;
                                    } else {
                                        forReason = 1;
                                    }
                                }
                                if (forReason == 1) {
                                    itemViewHolder.spinner_reason.setEnabled(false);
                                }
                            }*/
                            //For Reason
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            try {
                itemViewHolder.approved.setVisibility(View.VISIBLE);
                itemViewHolder.shipped.setVisibility(View.GONE);
                itemViewHolder.tv_approved.setText(stringArrayList.get(position).getApprovedQty());
                try {
                    if (stringArrayList.get(position).getpickededQty() != null) {
                        if (stringArrayList.get(position).getApprovedQty() != null) {
                            float pickedQty = Float.parseFloat(stringArrayList.get(position).getpickededQty());
                            float ApprovedQty = Float.parseFloat(stringArrayList.get(position).getApprovedQty());
                            if (pickedQty == ApprovedQty) {
                                itemViewHolder.spinner_reason.setEnabled(false);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

            //For Reason
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(activity, R.layout.spinner_design, arrayreason);
        itemViewHolder.spinner_reason.setAdapter(adapterSpinner);

        itemViewHolder.spinner_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int spinnerPosition, long id) {
                adapter = new commonReceivingShippingDetailList();
                String selectedItem = parent.getItemAtPosition(spinnerPosition).toString();
                stringArrayList.get(holder.getAdapterPosition()).setReason(selectedItem);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
            //For Reason





        itemViewHolder.cardview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int positions = itemViewHolder.getAdapterPosition();
                if (stringArrayList.get(positions).getpickededQty() != null) {
                    String[] date = stringArrayList.get(positions).getExpdate().split("\\s+");
                    recyclerViewDetailsDialog(stringArrayList.get(positions), cometoWhere, itemViewHolder, positions);
//                    recyclerViewDetailsDialog(stringArrayList.get(positions).getConfig(), stringArrayList.get(positions).getBatchId(), date[0], stringArrayList.get(positions).getpickededQty(),cometoWhere,itemViewHolder,positions);
                } else {
                    Toast.makeText(activity, "Firstly scan the items.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    private class ItemViewHolderRCD extends RecyclerView.ViewHolder {
        TextView tv_itemno, tv_approved, tv_TONumber, tv_pickedqty, tv_remainingqty, tv_nameinarabic, tv_itemnameinEnglish, tv_config, tv_batchid, tv_expires, shipped,
                approved, tv_returnedqty, reasons, remaining;
        CardView cardview;

        Spinner spinner_reason;

        public ItemViewHolderRCD(View itemView) {
            super(itemView);
            tv_itemno = itemView.findViewById(R.id.tv_itemno);
            tv_approved = itemView.findViewById(R.id.tv_approvedqty);
            tv_nameinarabic = itemView.findViewById(R.id.tv_nameinarabic);
            tv_itemnameinEnglish = itemView.findViewById(R.id.tv_itemnameinEnglish);
            tv_pickedqty = itemView.findViewById(R.id.tv_pickedqty);
            tv_remainingqty = itemView.findViewById(R.id.tv_remainingqty);
            tv_TONumber = itemView.findViewById(R.id.tv_TONumber);
            cardview = itemView.findViewById(R.id.cardview);
            reasons = itemView.findViewById(R.id.reasons);
            remaining = itemView.findViewById(R.id.remaining);
            spinner_reason = itemView.findViewById(R.id.spinner_reasonsed);
            tv_config = itemView.findViewById(R.id.tv_config);
            tv_batchid = itemView.findViewById(R.id.tv_batchid);
            tv_expires = itemView.findViewById(R.id.tv_expires);
            approved = itemView.findViewById(R.id.approved);
            shipped = itemView.findViewById(R.id.shipped);
            tv_returnedqty = itemView.findViewById(R.id.tv_returnedqty);
        }
    }

    private void recyclerViewDetailsDialog(final commonReceivingShippingDetailDataList commonReceivingShippingDetailDataList, final String cometoWhere, final ItemViewHolderRCD itemViewHolder, final int position) {
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
        final AppCompatTextView configDialog = dialog.findViewById(R.id.configDialog);
        final AppCompatTextView batchNoDialog = dialog.findViewById(R.id.batchNoDialog);
        final AppCompatTextView expiry = dialog.findViewById(R.id.expiry);
        final AppCompatTextView pckdQty = dialog.findViewById(R.id.pckdQty);
        final AppCompatTextView shippedQtyDataText = dialog.findViewById(R.id.shippedQtyDataText);
        final AppCompatTextView unit = dialog.findViewById(R.id.unit);
        TextView textviewPickedReturn = dialog.findViewById(R.id.textviewPickedReturn);

        final AppCompatEditText manuallyReturnQuantity = dialog.findViewById(R.id.manuallyReturnQuantity);
        LinearLayoutCompat shippedLinearLayout = dialog.findViewById(R.id.shippedLinearLayout);
        LinearLayout footerBtn = dialog.findViewById(R.id.footerBtn);
        AppCompatButton returnQty_OK = dialog.findViewById(R.id.returnQty_OK);
        AppCompatButton returnQty_Cancel = dialog.findViewById(R.id.returnQty_Cancel);
        AppCompatButton previous = dialog.findViewById(R.id.previous);
        final AppCompatButton after = dialog.findViewById(R.id.after);

        getdataBySize = 0;

        manuallyReturnQuantity.setText(stringArrayList.get(position).getReturnQty());

        if (cometoWhere.equals("BIReceiving")) {
            //For Reason
            itemViewHolder.spinner_reason.setVisibility(View.GONE);
            itemViewHolder.reasons.setVisibility(View.GONE);
            itemViewHolder.tv_remainingqty.setVisibility(View.GONE);
            itemViewHolder.remaining.setVisibility(View.GONE);
            //For Reason
//            textviewPickedReturn.setText("Return qty        :");
//            manuallyReturnQuantity.setVisibility(View.VISIBLE);
//            footerBtn.setVisibility(View.VISIBLE);
//            shippedLinearLayout.setVisibility(View.VISIBLE);
//            dialog.setCancelable(false);
            unit.setText(commonReceivingShippingDetailDataList.getUnitId());
            pckdQty.setVisibility(View.GONE);
            manuallyReturnQuantity.setVisibility(View.GONE);
            footerBtn.setVisibility(View.GONE);
            shippedLinearLayout.setVisibility(View.VISIBLE);
            textviewPickedReturn.setVisibility(View.GONE);
            dialog.setCancelable(true);
        } else {
            textviewPickedReturn.setText("Picked Qty         :");
            unit.setText(commonReceivingShippingDetailDataList.getUnitId());
        }

        returnQty_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] returnQtySplit = manuallyReturnQuantity.getText().toString().split("\\.");
                String returnQbeforePoint = "", returnQ;

                for (int i = 0; i < returnQtySplit[0].length(); i++) {
                    char c = returnQtySplit[0].charAt(i);
                    returnQbeforePoint = String.valueOf(c);
                }

                if (returnQtySplit.length >= 1) {
                    returnQ = manuallyReturnQuantity.getText().toString();
                } else {
                    returnQ = returnQbeforePoint + "." + returnQtySplit[1];
                }

                if (!returnQ.equals("")) {
                    if (!returnQ.equals("0")) {

                        returnQ = roundToPlaces(Double.parseDouble(returnQ), 3) + "";


                        String batchNo = batchNoDialog.getText().toString();
                        float stickQty = Float.parseFloat(shippedQtyDataText.getText().toString());
                        float reutnQ = Float.parseFloat(returnQ);


                        for (int i = 0; i < commonReceivingShippingDetailDataList.getBatchListReceiver().size(); i++) {
                            String batchFromList = commonReceivingShippingDetailDataList.getBatchListReceiver().get(i).getBatchNo();
                            if (batchNo.equals(batchFromList)) {
                                float returnQfromList = Float.parseFloat(commonReceivingShippingDetailDataList.getBatchListReceiver().get(i).getReturnQty());
                                if (returnQfromList == 0.0) {
                                    Boolean batchBoolean = false, qtyBoolean = false;
                                    if (reutnQ <= stickQty) {

                                        List<SequenceQuanitiy> sequenceQuanitiys = Preference.getInstance(activity).getSequenceQuantity();
                                        for (int l = 0; l < sequenceQuanitiys.size(); l++) {
                                            String batch = sequenceQuanitiys.get(i).getBatchNumber();
                                            if (batch.equals(batchFromList)) {
                                                batchBoolean = true;
                                                String qty = sequenceQuanitiys.get(i).getSequenceQty();
                                                if (qty.equals(returnQ)) {
                                                    qtyBoolean = true;
                                                    commonReceivingShippingDetailDataList.getBatchListReceiver().get(i).setReturnQty(returnQ);
                                                }
                                            }
                                        }

                                        if(batchBoolean==false){
                                            Toast.makeText(activity, "Did not find any batch.", Toast.LENGTH_SHORT).show();
                                        }
                                        if(qtyBoolean==false){
                                            Toast.makeText(activity, "Did not find these type Quantity.", Toast.LENGTH_SHORT).show();
                                        }


                                    } else {
                                        Toast.makeText(activity, "Return Quantity not more then Shipped Quantity.", Toast.LENGTH_SHORT).show();
                                    }

                                    //For Reason
                                    int forReason = 0;
                                    if (stringArrayList.get(position).getBatchListReceiver() == null) {
                                        itemViewHolder.spinner_reason.setEnabled(false);
                                    } else if (stringArrayList.get(position).getBatchListReceiver().size() == 0) {
                                        if (stringArrayList.get(position).getBatchListReceiver().get(position).getReturnQty() == null) {
                                            itemViewHolder.spinner_reason.setEnabled(false);
                                        } else if (stringArrayList.get(position).getBatchListReceiver().get(position).getReturnQty() == "0") {
                                            itemViewHolder.spinner_reason.setEnabled(false);
                                        } else {
                                            itemViewHolder.spinner_reason.setEnabled(true);
                                        }
                                    } else {
                                        for (int l = 0; l <= stringArrayList.get(position).getBatchListReceiver().size() - 1; l++) {

                                            String returnQty = stringArrayList.get(position).getBatchListReceiver().get(l).getReturnQty();

                                            if (!returnQty.equals("0")) {
                                                itemViewHolder.spinner_reason.setEnabled(true);
                                                forReason = 0;
                                                break;
                                            } else {
                                                forReason = 1;
                                            }
                                        }
                                        if (forReason == 1) {
                                            itemViewHolder.spinner_reason.setEnabled(false);
                                        }
                                    }
                                    //For Reason


                                    //////////
                                } else {
                                    ////////////
                                    float totalret = returnQfromList + reutnQ;
                                    if (totalret <= stickQty) {

                                        Boolean batchBoolean = false, qtyBoolean = false;
                                        List<SequenceQuanitiy> sequenceQuanitiys = Preference.getInstance(activity).getSequenceQuantity();
                                        for (int l = 0; l < sequenceQuanitiys.size(); l++) {
                                            String batch = sequenceQuanitiys.get(i).getBatchNumber();
                                            if (batch.equals(batchFromList)) {
                                                batchBoolean = true;
                                                String qty = sequenceQuanitiys.get(i).getSequenceQty();
                                                if (qty.equals(returnQ)) {
                                                    qtyBoolean = true;
                                                    commonReceivingShippingDetailDataList.getBatchListReceiver().get(i).setReturnQty(String.valueOf(totalret));
                                                }
                                            }
                                        }

                                        if(batchBoolean==false){
                                            Toast.makeText(activity, "Did not find any batch.", Toast.LENGTH_SHORT).show();
                                        }
                                        if(qtyBoolean==false){
                                            Toast.makeText(activity, "Did not find these type Quantity.", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Toast.makeText(activity, "Return Quantity not more then Shipped Quantity.", Toast.LENGTH_SHORT).show();
                                    }



                                    //For Reason
                                    int forReason = 0;
                                    if (stringArrayList.get(position).getBatchListReceiver() == null) {
                                        itemViewHolder.spinner_reason.setEnabled(false);
                                    } else if (stringArrayList.get(position).getBatchListReceiver().size() == 0) {
                                        if (stringArrayList.get(position).getBatchListReceiver().get(position).getReturnQty() == null) {
                                            itemViewHolder.spinner_reason.setEnabled(false);
                                        } else if (stringArrayList.get(position).getBatchListReceiver().get(position).getReturnQty() == "0") {
                                            itemViewHolder.spinner_reason.setEnabled(false);
                                        } else {
                                            itemViewHolder.spinner_reason.setEnabled(true);
                                        }
                                    } else {
                                        for (int l = 0; l <= stringArrayList.get(position).getBatchListReceiver().size() - 1; l++) {

                                            String returnQty = stringArrayList.get(position).getBatchListReceiver().get(l).getReturnQty();

                                            if (!returnQty.equals("0")) {
                                                itemViewHolder.spinner_reason.setEnabled(true);
                                                forReason = 0;
                                                break;
                                            } else {
                                                forReason = 1;
                                            }
                                        }
                                        if (forReason == 1) {
                                            itemViewHolder.spinner_reason.setEnabled(false);
                                        }
                                    }
                                    //For Reason


                                }
                            }
                        }
                        dialog.dismiss();

                    } else {
                        Toast.makeText(activity, "Return Quantity not be 0.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "Return Quantity not be empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        returnQty_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (!cometoWhere.equals("BIReceiving")) {
            if (commonReceivingShippingDetailDataList.getBatchNoList() == null) {
                configDialog.setText(commonReceivingShippingDetailDataList.getBatchNoList().get(getdataBySize).getConfig());
                batchNoDialog.setText(commonReceivingShippingDetailDataList.getBatchNoList().get(getdataBySize).getBatchNo());
                expiry.setText(commonReceivingShippingDetailDataList.getExpdate());
                pckdQty.setText(commonReceivingShippingDetailDataList.getBatchNoList().get(getdataBySize).getBatchQty());
                dialog.show();
            }else if (commonReceivingShippingDetailDataList.getBatchNoList().size() == 1) {
                configDialog.setText(commonReceivingShippingDetailDataList.getBatchNoList().get(getdataBySize).getConfig());
                batchNoDialog.setText(commonReceivingShippingDetailDataList.getBatchNoList().get(getdataBySize).getBatchNo());
                expiry.setText(commonReceivingShippingDetailDataList.getExpdate());
                pckdQty.setText(commonReceivingShippingDetailDataList.getBatchNoList().get(getdataBySize).getBatchQty());
                dialog.show();
            } else {
                configDialog.setText(commonReceivingShippingDetailDataList.getBatchNoList().get(getdataBySize).getConfig());
                batchNoDialog.setText(commonReceivingShippingDetailDataList.getBatchNoList().get(getdataBySize).getBatchNo());
                expiry.setText(commonReceivingShippingDetailDataList.getExpdate());
                pckdQty.setText(commonReceivingShippingDetailDataList.getBatchNoList().get(getdataBySize).getBatchQty());
                getdataBySize++;
                dialog.show();
            }
        } else {
            if (commonReceivingShippingDetailDataList.getBatchListReceiver() == null) {
                configDialog.setText(commonReceivingShippingDetailDataList.getBatchListReceiver().get(getdataBySize).getConfig());
                batchNoDialog.setText(commonReceivingShippingDetailDataList.getBatchListReceiver().get(getdataBySize).getBatchNo());
                expiry.setText(commonReceivingShippingDetailDataList.getExpdate());
                pckdQty.setText(commonReceivingShippingDetailDataList.getBatchListReceiver().get(getdataBySize).getReturnQty());
                shippedQtyDataText.setText(commonReceivingShippingDetailDataList.getBatchListReceiver().get(getdataBySize).getStickerQty());
                dialog.show();
            } else if (commonReceivingShippingDetailDataList.getBatchListReceiver().size() == 1) {
                configDialog.setText(commonReceivingShippingDetailDataList.getBatchListReceiver().get(getdataBySize).getConfig());
                batchNoDialog.setText(commonReceivingShippingDetailDataList.getBatchListReceiver().get(getdataBySize).getBatchNo());
                expiry.setText(commonReceivingShippingDetailDataList.getExpdate());
                pckdQty.setText(commonReceivingShippingDetailDataList.getBatchListReceiver().get(getdataBySize).getReturnQty());
                shippedQtyDataText.setText(commonReceivingShippingDetailDataList.getBatchListReceiver().get(getdataBySize).getStickerQty());
                dialog.show();
            } else {
                configDialog.setText(commonReceivingShippingDetailDataList.getBatchListReceiver().get(getdataBySize).getConfig());
                batchNoDialog.setText(commonReceivingShippingDetailDataList.getBatchListReceiver().get(getdataBySize).getBatchNo());
                expiry.setText(commonReceivingShippingDetailDataList.getExpdate());
                pckdQty.setText(commonReceivingShippingDetailDataList.getBatchListReceiver().get(getdataBySize).getReturnQty());
                shippedQtyDataText.setText(commonReceivingShippingDetailDataList.getBatchListReceiver().get(getdataBySize).getStickerQty());
                getdataBySize++;
                dialog.show();
            }
        }


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cometoWhere.equals("BIReceiving")) {
                    int batchListSize = commonReceivingShippingDetailDataList.getBatchNoList().size();
                    if (getdataBySize == 0) {
                        Toast.makeText(activity, "Not Available.", Toast.LENGTH_SHORT).show();
                    } else if (getdataBySize == 1) {
                        Toast.makeText(activity, "Not Available.", Toast.LENGTH_SHORT).show();
                    } else {
                        getdataBySize--;
                        if (batchListSize <= getdataBySize) {
                            Toast.makeText(activity, "Not Available.", Toast.LENGTH_SHORT).show();
                        } else {
                            getdataBySize--;
                            configDialog.setText(commonReceivingShippingDetailDataList.getBatchNoList().get(getdataBySize).getConfig());
                            batchNoDialog.setText(commonReceivingShippingDetailDataList.getBatchNoList().get(getdataBySize).getBatchNo());
                            expiry.setText(commonReceivingShippingDetailDataList.getExpdate());
                            pckdQty.setText(commonReceivingShippingDetailDataList.getBatchNoList().get(getdataBySize).getBatchQty());
                            getdataBySize++;
                        }
                    }
                } else {
                    int batchListSize = commonReceivingShippingDetailDataList.getBatchListReceiver().size();
                    if (getdataBySize == 0) {
                        Toast.makeText(activity, "Not Available.", Toast.LENGTH_SHORT).show();
                    } else if (getdataBySize == 1) {
                        Toast.makeText(activity, "Not Available.", Toast.LENGTH_SHORT).show();
                    } else {
                        getdataBySize--;
                        if (batchListSize <= getdataBySize) {
                            Toast.makeText(activity, "Not Available.", Toast.LENGTH_SHORT).show();
                        } else {
                            getdataBySize--;
                            configDialog.setText(commonReceivingShippingDetailDataList.getBatchListReceiver().get(getdataBySize).getConfig());
                            batchNoDialog.setText(commonReceivingShippingDetailDataList.getBatchListReceiver().get(getdataBySize).getBatchNo());
                            expiry.setText(commonReceivingShippingDetailDataList.getExpdate());
                            pckdQty.setText(commonReceivingShippingDetailDataList.getBatchListReceiver().get(getdataBySize).getReturnQty());
                            shippedQtyDataText.setText(commonReceivingShippingDetailDataList.getBatchListReceiver().get(getdataBySize).getStickerQty());
                            getdataBySize++;
                        }
                    }
                }
            }
        });

        after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cometoWhere.equals("BIReceiving")) {
                    int batchListSize = commonReceivingShippingDetailDataList.getBatchNoList().size();
                    if (getdataBySize == 0) {
                        Toast.makeText(activity, "Not Available.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (batchListSize <= getdataBySize) {
                            Toast.makeText(activity, "Not Available.", Toast.LENGTH_SHORT).show();
                        } else {
                            configDialog.setText(commonReceivingShippingDetailDataList.getBatchNoList().get(getdataBySize).getConfig());
                            batchNoDialog.setText(commonReceivingShippingDetailDataList.getBatchNoList().get(getdataBySize).getBatchNo());
                            expiry.setText(commonReceivingShippingDetailDataList.getExpdate());
                            pckdQty.setText(commonReceivingShippingDetailDataList.getBatchNoList().get(getdataBySize).getBatchQty());
                            getdataBySize++;
                        }
                    }
                } else {
                    int batchListSize = commonReceivingShippingDetailDataList.getBatchListReceiver().size();
                    if (getdataBySize == 0) {
                        Toast.makeText(activity, "Not Available.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (batchListSize <= getdataBySize) {
                            Toast.makeText(activity, "Not Available.", Toast.LENGTH_SHORT).show();
                        } else {
                            configDialog.setText(commonReceivingShippingDetailDataList.getBatchListReceiver().get(getdataBySize).getConfig());
                            batchNoDialog.setText(commonReceivingShippingDetailDataList.getBatchListReceiver().get(getdataBySize).getBatchNo());
                            expiry.setText(commonReceivingShippingDetailDataList.getExpdate());
                            pckdQty.setText(commonReceivingShippingDetailDataList.getBatchListReceiver().get(getdataBySize).getReturnQty());
                            shippedQtyDataText.setText(commonReceivingShippingDetailDataList.getBatchListReceiver().get(getdataBySize).getStickerQty());
                            getdataBySize++;
                        }
                    }
                }
            }
        });

    }

    public interface ActivityAdapterInterface {
        public void prepareSelection(View v, int position);
    }

    private static BigDecimal roundToPlaces(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd;
    }
}