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
import com.pcare.common.entity.NetResponse;
import com.pcare.common.net.Api;
import com.pcare.common.net.RetrofitHelper;
import com.pcare.common.table.BPMTableController;
import com.pcare.common.table.UserDao;
import com.pcare.common.util.CommonUtil;
import com.pcare.common.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

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
                    ((ItemHolder) holder).timeView.setText(entity.getCheck_time());
                    ((ItemHolder) holder).systolicView.setText(entity.getSystolic() + entity.getUnit());
                    ((ItemHolder) holder).diastolicView.setText(entity.getDiastolic() + entity.getUnit());
                    ((ItemHolder) holder).meanAPView.setText(entity.getMean() + entity.getUnit());
                    ((ItemHolder) holder).pulseView.setText(entity.getPulse() + "bpm");
                    if (entity.getSystolic() > Double.parseDouble(getResources().getString(R.string.systolic_top))
                            || entity.getDiastolic() > Double.parseDouble(getResources().getString(R.string.diastolic_top))) {
                        ((ItemHolder) holder).typeView.setText("偏高");
                        ((ItemHolder) holder).typeView.setBackgroundResource(R.mipmap.circle_yellow);
                    } else if (entity.getSystolic() < Double.parseDouble(getResources().getString(R.string.systolic_bottom))
                            || entity.getDiastolic() < Double.parseDouble(getResources().getString(R.string.diastolic_bottom))) {
                        ((ItemHolder) holder).typeView.setText("偏低");
                        ((ItemHolder) holder).typeView.setBackgroundResource(R.mipmap.circle_yellow);
                    } else {
                        ((ItemHolder) holder).typeView.setText("正常");
                        ((ItemHolder) holder).typeView.setBackgroundResource(R.mipmap.circle_green);
                    }
                    if (entity.getSystolic() > 150
                            || entity.getDiastolic() > 100) {
                        ((ItemHolder) holder).typeView.setText("过高");
                        ((ItemHolder) holder).typeView.setBackgroundResource(R.mipmap.circle_red);
                    } else if (entity.getSystolic() < 110
                            || entity.getDiastolic() < 70) {
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
        getNetValueList();
    }
    private void getNetValueList(){
        JSONObject object = new JSONObject();
        try {
            object.putOpt("user_id",UserDao.get(getApplicationContext()).getCurrentUser().getUser_id());
            object.putOpt("query_date_begin",CommonUtil.getDateStr(startDate));
            object.putOpt("query_date_end",CommonUtil.getDateStr(endDate));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RetrofitHelper.getInstance().getRetrofit()
                .create(Api.class)
                .getBPMList("bpress_query_user_id",object)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribeWith(new DisposableSingleObserver<NetResponse>() {
                    @Override
                    public void onSuccess(NetResponse value) {
                        LogUtil.i("value: "+value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {

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
