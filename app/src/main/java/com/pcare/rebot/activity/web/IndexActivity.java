package com.pcare.rebot.activity.web;

/**
 * @Author: gl
 * @CreateDate: 2020/3/9
 * @Description:
 */
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.core.app.ActivityCompat;

import com.pcare.common.base.BaseActivity;
import com.pcare.common.base.IPresenter;
import com.pcare.common.net.Api;
import com.pcare.common.util.TTSUtil;
import com.pcare.common.util.WakeUtil;
import com.pcare.rebot.R;
import com.pcare.rebot.activity.MainActivity;

public class IndexActivity extends BaseActivity {
    private final String GUOPAGE = "guo", MEDICINEPAGE = "medicine", DIAGNOSEPAGE = "diagnose",NCP_SCREEN="ncp_screen";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_index;
    }

    @Override
    protected IPresenter bindPresenter() {
        return null;
    }

    public void toNextPage(View view) {
        Intent intent = new Intent(this, WebViewActivity.class);
        switch (view.getTag().toString()) {
            case GUOPAGE:
                intent.putExtra("type", Api.WEB_GUO);
                break;
            case DIAGNOSEPAGE:
                intent.putExtra("type", Api.WEB_DIAGNOSE);
                break;
            case MEDICINEPAGE:
                intent.putExtra("type", Api.WEB_MEDICINE);
                break;
            case NCP_SCREEN:
                intent.putExtra("type", Api.WEB_NCP);
                break;
            default:
                intent.putExtra("type", Api.WEB_GUO);
                break;
        }
        startActivity(intent);
    }
    public void openWake(View view) {
        TTSUtil.getInstance(getApplicationContext()).speaking("打开了唤醒功能");
        WakeUtil.start(getApplicationContext(),"com.pcare.rebot.activity", MainActivity.class.getName());
    }
    public void closeWake(View view) {
        TTSUtil.getInstance(getApplicationContext()).speaking("关闭了唤醒功能");
        WakeUtil.stop();
    }
    private void requestPermissions(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if(permission!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[] {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.LOCATION_HARDWARE,Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_SETTINGS,Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_CONTACTS},0x0010);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
