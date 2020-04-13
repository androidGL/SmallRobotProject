/*
 * Copyright (c) 2015, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.pcare.bloodglu;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pcare.common.base.IPresenter;
import com.pcare.common.entity.GlucoseEntity;
import com.pcare.common.entity.GlucoseEntityDao;
import com.pcare.common.entity.NetResponse;
import com.pcare.common.net.Api;
import com.pcare.common.net.RetrofitHelper;
import com.pcare.common.oem.battery.BatteryManagerCallbacks;
import com.pcare.common.oem.battery.LoggableBleManager;
import com.pcare.common.table.BPMTableController;
import com.pcare.common.table.GluTableController;
import com.pcare.common.table.UserDao;
import com.pcare.common.util.CommonUtil;
import com.pcare.common.util.LogUtil;
import com.pcare.common.view.CommonAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

@Route(path = "/glu/main")
public class GlucoseActivity extends BleProfileExpandableListActivity implements BatteryManagerCallbacks {

    private GlucoseManager mGlucoseManager;
    private TextView mBatteryLevelView;
    private RelativeLayout gluBatteryView;

    private TextView gluNum;
    private TextView timeView;
    private TextView locationView;
    private TextView gluType;
    private TextView normalGlu;

    @Override
    protected void initView() {
        super.initView();
        if (!ensureBLEExists())
            finish();
        mBatteryLevelView = findViewById(R.id.battery);
        gluBatteryView = findViewById(R.id.glu_battery);
        gluNum = findViewById(R.id.glu_num);
        timeView = findViewById(R.id.timestamp);
        locationView = findViewById(R.id.location);
        gluType = findViewById(R.id.glu_type);
        normalGlu = findViewById(R.id.normal_glu);
    }

    @Override
    public void start() {
        super.start();
//        testHistory();
    }

    @Override
    protected LoggableBleManager<BatteryManagerCallbacks> initializeManager() {
        GlucoseManager manager = mGlucoseManager = GlucoseManager.getGlucoseManager(getApplicationContext()).setCallBack(entity -> {
            insertToNet(entity,true);
        });
        manager.setGattCallbacks(this);
        return manager;
    }
    private void insertToNet(GlucoseEntity entity,boolean isSave){
        RetrofitHelper.getInstance().getRetrofit()
                .create(Api.class)
                .insertGLU("insert",entity)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribeWith(new DisposableSingleObserver<NetResponse>() {

                    @Override
                    public void onSuccess(NetResponse value) {
                        LogUtil.i(value.toString());
                        if(value.getStatus() == 0 && isSave) {
                            try {
                                JSONObject object = new JSONObject(value.getData().toString());
                                gluNum.setText(getString(R.string.gls_value,
                                        entity.getGlucose()));
                                timeView.setText(entity.getCheck_time());
                                locationView.setText(getResources().getStringArray(R.array.gls_location)[entity.getSample_location()]);
                                entity.setResult(Integer.parseInt(object.optString("result")));
                                normalGlu.setText("血糖正常范围为"+object.optString("normal"));
                                switch (entity.getResult()){
                                    case -2:
                                        gluType.setText("过低");
                                        break;
                                    case -1:
                                        gluType.setText("较低");
                                        break;
                                    case 0:
                                        gluType.setText("正常");
                                        break;
                                    case 1:
                                        gluType.setText("较高");
                                        break;
                                    case 2:
                                        gluType.setText("过高");
                                        break;

                                }
                                GluTableController.getInstance(getApplicationContext()).insertOrReplace(entity);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    protected int getAboutTextId() {
        return R.string.gls_about_text;
    }

    @Override
    protected int getDefaultDeviceName() {
        return R.string.gls_default_name;
    }

    @Override
    protected UUID getFilterUUID() {
        return GlucoseManager.GLS_SERVICE_UUID;
    }

    @Override
    public void onBatteryLevelChanged(@NonNull final BluetoothDevice device, final int batteryLevel) {
        runOnUiThread(() -> {
            mBatteryLevelView.setText(GlucoseActivity.this.getString(R.string.battery, batteryLevel));
            gluBatteryView.setVisibility(View.VISIBLE);
        });
    }

    private boolean ensureBLEExists() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.no_ble, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_feature_gls;
    }

    @Override
    protected IPresenter bindPresenter() {
        return null;
    }

    public void refreshData(View view) {
        mGlucoseManager.refreshRecords();
    }

    public void testHistory() {
        List<GlucoseEntity> list = GluTableController.getInstance(getSelfActivity()).searchAll();
        for (GlucoseEntity entity : list) {
            Log.i("Table-----", entity.getId() + entity.toString());
        }
    }

    public void testNet(View v){
        EditText month,day,hour,min,value;
        month = findViewById(R.id.t_month);
        day = findViewById(R.id.t_date);
        hour = findViewById(R.id.t_hour);
        min = findViewById(R.id.t_min);
        value = findViewById(R.id.t_value);
        String monthS,dayS,hourS,minS,valueS;
        monthS = month.getText().toString();
        dayS = day.getText().toString();
        hourS = hour.getText().toString();
        minS = min.getText().toString();
        valueS = value.getText().toString();
        if(TextUtils.isEmpty(monthS) || TextUtils.isEmpty(dayS) || TextUtils.isEmpty(hourS) || TextUtils.isEmpty(minS) || TextUtils.isEmpty(valueS)){
            Toast.makeText(getApplicationContext(),"得输入完整信息测试呀~",Toast.LENGTH_SHORT).show();
        }
        GlucoseEntity entity = new GlucoseEntity();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, Integer.valueOf(monthS), Integer.valueOf(dayS),Integer.valueOf(hourS),Integer.valueOf(minS));
        entity.setCheck_time(CommonUtil.getDateStr(calendar.getTime()));
        entity.setSequence_num("0");
        entity.setGlucose(Double.parseDouble(valueS));
        entity.setSample_type(0);
        entity.setSample_location(0);
        entity.setStatus( 1);
        entity.setUnit("mmol/l");
        entity.setRobot_id(CommonUtil.getUUID(getApplicationContext()));
        entity.setId(CommonUtil.getRandomId());
        entity.setUser_id(UserDao.getCurrentUserId());
        insertToNet(entity,true);
    }

    public void toTrendChartActivity(View view) {
        startActivity(new Intent(GlucoseActivity.this,GlucoseHistoryActivity.class));
    }
}
