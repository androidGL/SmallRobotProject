package com.pcare.bloodpressure.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pcare.bloodpressure.R;
import com.pcare.common.base.BaseActivity;
import com.pcare.common.base.IPresenter;
import com.pcare.common.entity.BPMEntity;
import com.pcare.common.table.BPMTableController;
import com.pcare.common.table.UserDao;
import com.pcare.common.util.CommonUtil;
import com.pcare.common.util.LogUtil;

import java.util.Date;
import java.util.List;

/**
 * @Author: gl
 * @CreateDate: 2019/11/26
 * @Description:
 */
public class BPMHistoryActivity extends BaseActivity {

    private RecyclerView bpmListView;
    private List<BPMEntity> bpmList;
    private TextView startView, endView;
    private Date startDate, endDate;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bpm_history;
    }

    @Override
    protected IPresenter bindPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        super.initView();
        bpmListView = findViewById(R.id.view_list_bpm);
        startView = findViewById(R.id.date_start);
        endView = findViewById(R.id.date_end);
    }

    @Override
    protected void initData() {
        super.initData();
        //获取当前用户的血压记录
        bpmList = BPMTableController.getInstance(getApplicationContext()).searchByUserId(UserDao.getCurrentUserId());
        if (bpmList.size() <= 0) {
            findViewById(R.id.null_view).setVisibility(View.VISIBLE);
            bpmListView.setVisibility(View.GONE);
            return;
        }

        bpmListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        bpmListView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_bpm, parent, false);
                return new ItemHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                if (holder instanceof ItemHolder) {
                    BPMEntity entity = bpmList.get(position);
                    ((ItemHolder) holder).timeView.setText(CommonUtil.getDateStr(entity.getTimeData()));
                    ((ItemHolder) holder).systolicView.setText(entity.getSystolicData() + entity.getUnit());
                    ((ItemHolder) holder).diastolicView.setText(entity.getDiastolicData() + entity.getUnit());
                    ((ItemHolder) holder).meanAPView.setText(entity.getMeanAPData() + entity.getUnit());
                    ((ItemHolder) holder).pulseView.setText(entity.getPulseData() + "bpm");
                    if (Double.parseDouble(entity.getSystolicData()) > Double.parseDouble(getResources().getString(R.string.systolic_top))
                            || Double.parseDouble(entity.getDiastolicData()) > Double.parseDouble(getResources().getString(R.string.diastolic_top))) {
                        ((ItemHolder) holder).typeView.setText("偏高");
                        ((ItemHolder) holder).typeView.setBackgroundResource(R.mipmap.circle_yellow);
                    } else if (Double.parseDouble(entity.getSystolicData()) < Double.parseDouble(getResources().getString(R.string.systolic_bottom))
                            || Double.parseDouble(entity.getDiastolicData()) < Double.parseDouble(getResources().getString(R.string.diastolic_bottom))) {
                        ((ItemHolder) holder).typeView.setText("偏低");
                        ((ItemHolder) holder).typeView.setBackgroundResource(R.mipmap.circle_yellow);
                    } else {
                        ((ItemHolder) holder).typeView.setText("正常");
                        ((ItemHolder) holder).typeView.setBackgroundResource(R.mipmap.circle_green);
                    }
                    if (Double.parseDouble(entity.getSystolicData()) > 150
                            || Double.parseDouble(entity.getDiastolicData()) > 100) {
                        ((ItemHolder) holder).typeView.setText("过高");
                        ((ItemHolder) holder).typeView.setBackgroundResource(R.mipmap.circle_red);
                    } else if (Double.parseDouble(entity.getSystolicData()) < 110
                            || Double.parseDouble(entity.getDiastolicData()) < 70) {
                        ((ItemHolder) holder).typeView.setText("过低");
                        ((ItemHolder) holder).typeView.setBackgroundResource(R.mipmap.circle_red);
                    }
                }
            }

            @Override
            public int getItemCount() {
                return bpmList.size();
            }
        });
    }

    public void filterList(View v) {
        if (null == startDate) {
            startDate = new Date();
            if (null == endDate) {
                Toast.makeText(this, "请选择起始日期或结束日期", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (null == endDate)
            endDate = new Date(System.currentTimeMillis());
        bpmList = BPMTableController.getInstance(getApplicationContext())
                .searchByUserId(UserDao.getCurrentUserId(), startDate, endDate);
        bpmListView.getAdapter().notifyDataSetChanged();
    }

    public void clearDate(View v) {
        startView.setText("");
        endView.setText("");
        startDate = null;
        endDate = null;
        bpmList = BPMTableController.getInstance(getApplicationContext())
                .searchByUserId(UserDao.getCurrentUserId());
        bpmListView.getAdapter().notifyDataSetChanged();

    }

    public void selectDate(View v) {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            switch (v.getTag().toString()) {
                case "start":
                    startView.setText(year + "年" + (month + 1) + "月" + dayOfMonth + "日");
                    startDate = CommonUtil.getDate(year + "-" + (month + 1) + "-" + dayOfMonth);
                    break;
                case "end":
                    endView.setText(year + "年" + (month + 1) + "月" + dayOfMonth + "日");
                    endDate = CommonUtil.getDate(year + "-" + (month + 1) + "-" + dayOfMonth);
                    break;
            }

        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();
    }

    public void toChartActivity(View view) {
        startActivity(new Intent(this, BPMTrendChartActivity.class));
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private TextView timeView, systolicView, diastolicView, meanAPView, pulseView, typeView;

        public ItemHolder(View itemView) {
            super(itemView);
            timeView = itemView.findViewById(R.id.bpm_time);
            systolicView = itemView.findViewById(R.id.bpm_systolic);
            diastolicView = itemView.findViewById(R.id.bpm_diastolic);
            meanAPView = itemView.findViewById(R.id.bpm_mean_ap);
            pulseView = itemView.findViewById(R.id.bpm_pulse);
            typeView = itemView.findViewById(R.id.bpm_type);

        }
    }
}
