package com.pcare.bloodpressure.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.pcare.common.table.UserDao;
import com.pcare.common.util.CommonToast;
import com.pcare.common.util.CommonUtil;
import com.pcare.common.view.CommonAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    private List<BPMEntity> bpmList = new ArrayList<>();
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
                    ((ItemHolder) holder).itemView.setOnLongClickListener(v -> {
                        CommonAlertDialog.Builder(BPMHistoryActivity.this)
                                .setCancelText("否")
                                .setConfirmText("删除")
                                .setMessage("是否删除这个结果")
                                .setOnConfirmClickListener(view -> {
                                    delete(bpmList.get(position).getId(),position);
                                }).build().shown();
                        return true;
                    });

                }
            }

            @Override
            public int getItemCount() {
                return bpmList.size();
            }
        });
        getNetValueList();
    }

    private void delete(String id,int position){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RetrofitHelper.getInstance().getRetrofit()
                .create(Api.class)
                .deleteBPM("delete", jsonObject)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribeWith(new DisposableSingleObserver<NetResponse>() {
                    @Override
                    public void onSuccess(NetResponse value) {
                        if(value.getStatus() == 0){
                            CommonToast.showShortToast(getApplicationContext(),"删除数据成功", CommonToast.SUCCESS);
                            bpmList.remove(position);
                            bpmListView.getAdapter().notifyDataSetChanged();
                            return;
                        }
                        CommonToast.showShortToast(getApplicationContext(),"删除数据失败", CommonToast.FAILED);
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonToast.showShortToast(getApplicationContext(),"删除数据失败", CommonToast.FAILED);
                    }
                });
    }

    private void getNetValueList() {
        JSONObject object = new JSONObject();
        try {
            object.putOpt("user_id", UserDao.get(getApplicationContext()).getCurrentUser().getUser_id());
            if (null != startDate)
                object.putOpt("query_date_begin", CommonUtil.getDateStr(startDate,"yyyy-MM-dd"));
            if (null != endDate)
                object.putOpt("query_date_end", CommonUtil.getDateStr(endDate,"yyyy-MM-dd"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RetrofitHelper.getInstance().getRetrofit()
                .create(Api.class)
                .getBPMList("bpress_query_user_id", object)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribeWith(new DisposableSingleObserver<NetResponse>() {
                    @Override
                    public void onSuccess(NetResponse value) {
                        if(value.getStatus() == 0){
                            bpmList.clear();
                            JSONObject object;
                            try {
                                object = new JSONObject(CommonUtil.entityToJson(value.getData()));
                                JSONArray info = object.optJSONArray("info");
                                for (int i = 0;i<info.length();i++){
                                    bpmList.add(CommonUtil.fromJson(info.optJSONObject(i).toString(),BPMEntity.class));
                                }
                                bpmListView.getAdapter().notifyDataSetChanged();
                                if (bpmList.size() <= 0) {
                                    findViewById(R.id.null_view).setVisibility(View.VISIBLE);
                                    bpmListView.setVisibility(View.GONE);
                                }else {
                                    findViewById(R.id.null_view).setVisibility(View.GONE);
                                    bpmListView.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void filterList(View v) {
        if (null != endDate && null == startDate) {
                Toast.makeText(this, "请选择起始日期", Toast.LENGTH_SHORT).show();
                return;
        }
       getNetValueList();
    }

    public void clearDate(View v) {
        startView.setText("");
        endView.setText("");
        if(startDate != null || endDate != null){
            startDate = null;
            endDate = null;
            getNetValueList();
        }
    }

    public void selectDate(View v) {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            switch (v.getTag().toString()) {
                case "start":
                    startView.setText(year + "年" + (month + 1) + "月" + dayOfMonth + "日");
                    startDate = CommonUtil.getDate(year + "-" + (month + 1) + "-" + dayOfMonth,"yyyy-MM-dd");
                    break;
                case "end":
                    endView.setText(year + "年" + (month + 1) + "月" + dayOfMonth + "日");
                    endDate = CommonUtil.getDate(year + "-" + (month + 1) + "-" + dayOfMonth,"yyyy-MM-dd");
                    break;
            }

        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();
    }

    public void toChartActivity(View view) {
        startActivity(new Intent(this, BPMTrendChartActivity.class));
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private TextView timeView, systolicView, diastolicView, meanAPView, pulseView, typeView;
        private View itemView;

        public ItemHolder(View itemView) {
            super(itemView);
            timeView = itemView.findViewById(R.id.bpm_time);
            systolicView = itemView.findViewById(R.id.bpm_systolic);
            diastolicView = itemView.findViewById(R.id.bpm_diastolic);
            meanAPView = itemView.findViewById(R.id.bpm_mean_ap);
            pulseView = itemView.findViewById(R.id.bpm_pulse);
            typeView = itemView.findViewById(R.id.bpm_type);
            this.itemView = itemView;

        }
    }
}
