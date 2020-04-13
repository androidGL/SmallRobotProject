package com.pcare.bloodglu;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pcare.common.base.BaseActivity;
import com.pcare.common.base.IPresenter;
import com.pcare.common.entity.BPMEntity;
import com.pcare.common.entity.GlucoseEntity;
import com.pcare.common.entity.NetResponse;
import com.pcare.common.net.Api;
import com.pcare.common.net.RetrofitHelper;
import com.pcare.common.table.GluTableController;
import com.pcare.common.table.UserDao;
import com.pcare.common.util.CommonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author: gl
 * @CreateDate: 2020/3/13
 * @Description:
 */
public class GlucoseHistoryActivity  extends BaseActivity {
    private RecyclerView gluListView;
    private TextView startView, endView;
    private Date startDate, endDate;
    private List<GlucoseEntity> glucoseEntityList = new ArrayList<>();
    private GlucoseRecycleAdapter recycleAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_glu_history;
    }

    @Override
    protected IPresenter bindPresenter() {
        return null;
    }
    @Override
    protected void initView() {
        super.initView();
        gluListView = findViewById(R.id.view_list_glu);
        startView = findViewById(R.id.date_start);
        endView = findViewById(R.id.date_end);
        gluListView.setLayoutManager(new LinearLayoutManager(getSelfActivity()));
    }

    @Override
    protected void initData() {
        super.initData();
//        glucoseEntityList = GluTableController.getInstance(getSelfActivity()).searchAll();
        recycleAdapter = new GlucoseRecycleAdapter(getSelfActivity(), glucoseEntityList);
        gluListView.setAdapter(recycleAdapter);
        getNetValueList();
    }

    public void filterList(View v) {
        if (null != endDate && null == startDate) {
            Toast.makeText(this, "请选择起始日期", Toast.LENGTH_SHORT).show();
            return;
        }
        if (null == endDate)
            endDate = new Date(System.currentTimeMillis());

        getNetValueList();
//        glucoseEntityList = GluTableController.getInstance(getApplicationContext())
//                .searchByUserId(UserDao.getCurrentUserId(), startDate, endDate);
//        gluListView.getAdapter().notifyDataSetChanged();
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
                .getGLUList("glu_query_user_id", object)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribeWith(new DisposableSingleObserver<NetResponse>() {
                    @Override
                    public void onSuccess(NetResponse value) {
                        if(value.getStatus() == 0){
                            glucoseEntityList.clear();
                            JSONObject object;
                            try {
                                object = new JSONObject(CommonUtil.entityToJson(value.getData()));
                                JSONArray info = object.optJSONArray("info");
                                for (int i = 0;i<info.length();i++){
                                    glucoseEntityList.add(CommonUtil.fromJson(info.optJSONObject(i).toString(),GlucoseEntity.class));
                                }
                                gluListView.getAdapter().notifyDataSetChanged();
                                if (glucoseEntityList.size() <= 0) {
                                    findViewById(R.id.null_view).setVisibility(View.VISIBLE);
                                    gluListView.setVisibility(View.GONE);
                                }else {
                                    findViewById(R.id.null_view).setVisibility(View.GONE);
                                    gluListView.setVisibility(View.VISIBLE);
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
        startActivity(new Intent(this, GluTrendChartActivity.class));
    }

}
