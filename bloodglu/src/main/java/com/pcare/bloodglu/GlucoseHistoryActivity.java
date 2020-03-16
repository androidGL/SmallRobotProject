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
import com.pcare.common.table.GluTableController;
import com.pcare.common.table.UserDao;
import com.pcare.common.util.CommonUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
        glucoseEntityList = GluTableController.getInstance(getSelfActivity()).searchAll();
        recycleAdapter = new GlucoseRecycleAdapter(getSelfActivity(), glucoseEntityList);
        gluListView.setAdapter(recycleAdapter);
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
        glucoseEntityList = GluTableController.getInstance(getApplicationContext())
                .searchByUserId(UserDao.getCurrentUserId(), startDate, endDate);
        gluListView.getAdapter().notifyDataSetChanged();
    }

    public void clearDate(View v) {
        startView.setText("");
        endView.setText("");
        startDate = null;
        endDate = null;
        glucoseEntityList = GluTableController.getInstance(getApplicationContext())
                .searchByUserId(UserDao.getCurrentUserId());
        gluListView.getAdapter().notifyDataSetChanged();

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
