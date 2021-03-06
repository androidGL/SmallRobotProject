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
package com.pcare.bloodpressure.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pcare.bloodpressure.R;
import com.pcare.bloodpressure.manager.BPMManagerCallbacks;
import com.pcare.common.base.IPresenter;
import com.pcare.common.entity.BPMEntity;
import com.pcare.common.entity.NetResponse;
import com.pcare.common.net.Api;
import com.pcare.common.net.RetrofitHelper;
import com.pcare.common.oem.battery.LoggableBleManager;
import com.pcare.common.table.BPMTableController;
import com.pcare.common.table.UserDao;
import com.pcare.common.util.CommonUtil;
import com.pcare.bloodpressure.manager.BPMManager;
import com.pcare.common.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import no.nordicsemi.android.ble.common.profile.bp.BloodPressureMeasurementCallback;
import no.nordicsemi.android.ble.common.profile.bp.IntermediateCuffPressureCallback;
import okhttp3.ResponseBody;

@Route(path = "/bp/main")
public class BPMActivity extends BleProfileActivity implements BPMManagerCallbacks {

    private final String TAG = "BPMActivity";
    private TextView mSystolicView; //高压
    private TextView mSystolicUnitView;//高压单位
    private TextView mDiastolicView;//低压
    private TextView mDiastolicUnitView;//低压单位
    private TextView mMeanAPView;//平均动脉压
    private TextView mMeanAPUnitView;//平均动脉压单位
    private TextView mPulseView;//脉搏
    private TextView mTimestampView;//时间
    private TextView mBatteryLevelView;//电池
    private BPMEntity mBPMEntity;//血压实体类
    private TextView userNameText; //用户姓名
    private TextView bPressType,normalDiastolic,normalSystolic;//类型，低压正常值，高压正常值

    private void setGUI() {
        mSystolicView = findViewById(R.id.systolic);
        mSystolicUnitView = findViewById(R.id.systolic_unit);
        mDiastolicView = findViewById(R.id.diastolic);
        mDiastolicUnitView = findViewById(R.id.diastolic_unit);
        mMeanAPView = findViewById(R.id.mean_ap);
        mMeanAPUnitView = findViewById(R.id.mean_ap_unit);
        mPulseView = findViewById(R.id.pulse);
        mTimestampView = findViewById(R.id.timestamp);
        mBatteryLevelView = findViewById(R.id.battery);
        userNameText = findViewById(R.id.user_name);
        userNameText.setText(UserDao.get(getApplicationContext()).getCurrentUser().getNickName());
        bPressType = findViewById(R.id.bpress_type);
        normalDiastolic = findViewById(R.id.normal_diastolic);
        normalSystolic = findViewById(R.id.normal_systolic);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_feature_bpm;
    }

    @Override
    protected IPresenter bindPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        setGUI();
    }

    @Override
    protected int getLoggerProfileTitle() {
        return R.string.bpm_feature_title;
    }

    @Override
    protected int getDefaultDeviceName() {
        return R.string.bpm_default_name;
    }

    @Override
    protected UUID getFilterUUID() {
        return BPMManager.BP_SERVICE_UUID;
    }

    @Override
    protected LoggableBleManager<BPMManagerCallbacks> initializeManager() {
        final BPMManager manager = BPMManager.getBPMManager(getApplicationContext());
        manager.setGattCallbacks(this);
        return manager;
    }

    @Override
    protected void setDefaultUI() {
        mSystolicView.setText(R.string.not_available_value);
        mSystolicUnitView.setText(null);
        mDiastolicView.setText(R.string.not_available_value);
        mDiastolicUnitView.setText(null);
        mMeanAPView.setText(R.string.not_available_value);
        mMeanAPUnitView.setText(null);
        mPulseView.setText(R.string.not_available_value);
        mTimestampView.setText(R.string.not_available);
        mBatteryLevelView.setText(R.string.not_available);
    }

    @Override
    public void onServicesDiscovered(@NonNull final BluetoothDevice device, final boolean optionalServicesFound) {
        // this may notify user or show some views
    }

    @Override
    public void onDeviceReady(@NonNull final BluetoothDevice device) {
        // this may notify user
    }

    @Override
    public void onDeviceDisconnected(@NonNull final BluetoothDevice device) {
        super.onDeviceDisconnected(device);
        mBatteryLevelView.setText(R.string.not_available);
    }

    @Override
    public void onBloodPressureMeasurementReceived(@NonNull final BluetoothDevice device,
                                                   final float systolic, final float diastolic, final float meanArterialPressure, final int unit,
                                                   @Nullable final Float pulseRate, @Nullable final Integer userID,
                                                   @Nullable final BPMStatus status, @Nullable final Calendar calendar) {
        runOnUiThread(() -> {
            if (null == mBPMEntity) {
                mBPMEntity = new BPMEntity();
            }
            mBPMEntity.setUser_id(UserDao.getCurrentUserId());
            mBPMEntity.setSystolic((int) systolic);
            mBPMEntity.setDiastolic((int)diastolic);
            mBPMEntity.setMean((int)meanArterialPressure);
            if (pulseRate != null)
                mBPMEntity.setPulse(pulseRate.intValue());
            else
                mBPMEntity.setPulse(Integer.parseInt(getString(R.string.not_available_value)));
            if (calendar != null) {
                mBPMEntity.setCheck_time(CommonUtil.getDateStr(calendar.getTime()));
            }
            mBPMEntity.setUnit(unit == BloodPressureMeasurementCallback.UNIT_mmHg ? getString(R.string.bpm_unit_mmhg) : getString(R.string.bpm_unit_kpa));
            updateViews();

            Log.i(TAG, mBPMEntity.toString());
            //插入数据
            insertToNet(true);
        });
    }
    private void updateViews(){
        mSystolicView.setText(String.valueOf(mBPMEntity.getSystolic()));
        mDiastolicView.setText(String.valueOf(mBPMEntity.getDiastolic()));
        mMeanAPView.setText(String.valueOf(mBPMEntity.getMean()));
        mPulseView.setText(String.valueOf(mBPMEntity.getPulse()));
        if (mBPMEntity.getCheck_time() != null)
            mTimestampView.setText(mBPMEntity.getCheck_time());
        else
            mTimestampView.setText(R.string.not_available);
        mSystolicUnitView.setText(mBPMEntity.getUnit());
        mDiastolicUnitView.setText(mBPMEntity.getUnit());
        mMeanAPUnitView.setText(mBPMEntity.getUnit());
    }

    @Override
    public void onIntermediateCuffPressureReceived(@NonNull final BluetoothDevice device, final float cuffPressure, final int unit,
                                                   @Nullable final Float pulseRate, @Nullable final Integer userID,
                                                   @Nullable final BPMStatus status, @Nullable final Calendar calendar) {
        runOnUiThread(() -> {
            mSystolicView.setText(String.valueOf(cuffPressure));
            mDiastolicView.setText(R.string.not_available_value);
            mMeanAPView.setText(R.string.not_available_value);
            if (pulseRate != null)
                mPulseView.setText(String.valueOf(pulseRate));
            else
                mPulseView.setText(R.string.not_available_value);
            if (calendar != null)
                mTimestampView.setText(CommonUtil.getDateStr(calendar.getTime()));
            else
                mTimestampView.setText(R.string.not_available);
            mSystolicUnitView.setText(unit == IntermediateCuffPressureCallback.UNIT_mmHg ? R.string.bpm_unit_mmhg : R.string.bpm_unit_kpa);
            mDiastolicUnitView.setText(null);
            mMeanAPUnitView.setText(null);
        });
    }

    @Override
    public void onBatteryLevelChanged(@NonNull final BluetoothDevice device, final int batteryLevel) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBatteryLevelView.setText(BPMActivity.this.getString(R.string.battery, batteryLevel));
            }
        });
    }

    public void toBPMHistory(View view) {
        startActivity(new Intent(this, BPMHistoryActivity.class));
    }

    public void testNet(View view) {
        EditText month,day,hour,min,systolic,diastolic;
        month = findViewById(R.id.t_month);
        day = findViewById(R.id.t_date);
        hour = findViewById(R.id.t_hour);
        min = findViewById(R.id.t_min);
        systolic = findViewById(R.id.t_systolic);
        diastolic = findViewById(R.id.t_diastolic);
        String monthS,dayS,hourS,minS,systolicS,diastolicS;
        monthS = month.getText().toString();
        dayS = day.getText().toString();
        hourS = hour.getText().toString();
        minS = min.getText().toString();
        systolicS = systolic.getText().toString();
        diastolicS = diastolic.getText().toString();
        if(TextUtils.isEmpty(monthS) || TextUtils.isEmpty(dayS) || TextUtils.isEmpty(hourS) || TextUtils.isEmpty(minS) || TextUtils.isEmpty(systolicS) || TextUtils.isEmpty(diastolicS)){
            Toast.makeText(getApplicationContext(),"得输入完整信息测试呀~",Toast.LENGTH_SHORT).show();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, Integer.valueOf(monthS)-1, Integer.valueOf(dayS),Integer.valueOf(hourS),Integer.valueOf(minS));
        if (null == mBPMEntity) {
            mBPMEntity = new BPMEntity();
        }
        mBPMEntity.setUser_id(UserDao.getCurrentUserId());
        mBPMEntity.setSystolic(Integer.parseInt(systolicS));
        mBPMEntity.setDiastolic(Integer.parseInt(diastolicS));
        mBPMEntity.setMean(80);
        mBPMEntity.setPulse(70);
        mBPMEntity.setCheck_time(CommonUtil.getDateStr(calendar.getTime()));
        mBPMEntity.setUnit(getString(R.string.bpm_unit_mmhg));
        mBPMEntity.setRobot_id(CommonUtil.getUUID(getApplicationContext()));
        mBPMEntity.setId(CommonUtil.getRandomId());
        updateViews();
        insertToNet(true);

    }

    private void insertToNet(boolean isSave){
        RetrofitHelper.getInstance().getRetrofit()
                .create(Api.class)
                .insertBPM("insert",mBPMEntity)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribeWith(new DisposableSingleObserver<NetResponse>() {

                    @Override
                    public void onSuccess(NetResponse value) {
                        LogUtil.i("value"+value.toString());
                        if(value.getStatus() == 0 && isSave) {
                            JSONObject object;
                            try {
                                object = new JSONObject(CommonUtil.entityToJson(value.getData()));
                                mBPMEntity.setResult(Integer.parseInt(object.optString("result")));
                                BPMTableController.getInstance(getApplicationContext()).insert(mBPMEntity);
                                normalDiastolic.setText("低压正常范围为"+object.optString("diastolic"));
                                normalSystolic.setText("高压正常范围为"+object.optString("systolic"));
                                switch (mBPMEntity.getResult()){
                                    case -2:
                                        bPressType.setText("过低");
                                        bPressType.setTextColor(getColor(R.color.red));
                                        break;
                                    case -1:
                                        bPressType.setText("偏低");
                                        bPressType.setTextColor(getColor(R.color.yellow));
                                        break;
                                    case 0:
                                        bPressType.setText("正常");
                                        bPressType.setTextColor(getColor(R.color.green));
                                        break;
                                    case 1:
                                        bPressType.setText("偏高");
                                        bPressType.setTextColor(getColor(R.color.yellow));
                                        break;
                                    case 2:
                                        bPressType.setText("过高");
                                        bPressType.setTextColor(getColor(R.color.red));
                                        break;
                                }

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
}
