package com.yoeki.kalpnay.frdinventry.InventoryCountingNew.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yoeki.kalpnay.frdinventry.InventoryCountingNew.InventoryCountingDetail;
import com.yoeki.kalpnay.frdinventry.InventoryCountingNew.InventoryCountingNew;
import com.yoeki.kalpnay.frdinventry.InventoryCountingNew.model.ScheduleDetail;
import com.yoeki.kalpnay.frdinventry.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InventoryCountingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ScheduleDetail> scheduleDetailList;
    private Activity activity;
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ssa");//09/30/2020 12:58:55PM
    private SimpleDateFormat sdfToShow = new SimpleDateFormat("dd/MMM/yyyy hh:mm a");

    public InventoryCountingAdapter(InventoryCountingNew activity, List<ScheduleDetail> scheduleDetailList) {
        this.activity = activity;
        this.scheduleDetailList = scheduleDetailList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inventory_counting_temporary, viewGroup, false);
        return new InventoryCountingAdapter.ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        InventoryCountingAdapter.ItemViewHolder itemViewHolder = (InventoryCountingAdapter.ItemViewHolder) viewHolder;
        itemViewHolder.journalTempHead.setText(activity.getString(R.string.sch_activity));
        itemViewHolder.journalTemp.setText(scheduleDetailList.get(position).getScheduleActivityNo());
        itemViewHolder.wareHouseTemp.setText(scheduleDetailList.get(position).getWareHouseNo());


        itemViewHolder.linearLayoutInventoryCountTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                Date currentTime = cal.getTime();
                Date scheduleTime = null;

                try {
                    scheduleTime = sdf.parse(scheduleDetailList.get(position).getFromTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (scheduleTime != null) {
                    if (currentTime.before(scheduleTime)) {
                        String time = sdfToShow.format(scheduleTime);
                        Toast toast = Toast.makeText(activity, "You can open this schedule after \n" + time, Toast.LENGTH_LONG);
                        TextView tv = toast.getView().findViewById(android.R.id.message);
                        if (tv != null) tv.setGravity(Gravity.CENTER);
                        toast.show();
                    } else {
                        Intent intent = new Intent(activity, InventoryCountingDetail.class);
                        intent.putExtra("SID", scheduleDetailList.get(position).getSid());
                        intent.putExtra("SCHEDULE_NO", scheduleDetailList.get(position).getScheduleActivityNo());
                        intent.putExtra("WAREHOUSE_NO", scheduleDetailList.get(position).getWareHouseNo());
                        activity.startActivity(intent);
                        activity.finish();
                    }
                } else {
                    Toast.makeText(activity, "Invalid Time", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return scheduleDetailList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView journalTemp, journalTempHead, wareHouseTemp;
        LinearLayout linearLayoutInventoryCountTemp;

        public ItemViewHolder(View itemView) {
            super(itemView);
            linearLayoutInventoryCountTemp = itemView.findViewById(R.id.linearLayoutInventoryCountTemp);
            journalTemp = itemView.findViewById(R.id.journalTemp);
            journalTempHead = itemView.findViewById(R.id.journalTempHead);
            wareHouseTemp = itemView.findViewById(R.id.wareHouseTemp);
        }
    }
}
