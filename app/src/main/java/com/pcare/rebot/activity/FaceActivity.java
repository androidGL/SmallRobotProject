package com.pcare.rebot.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.CountDownTimer;
import android.view.TextureView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pcare.common.base.BaseActivity;
import com.pcare.common.base.IPresenter;
import com.pcare.common.entity.UserEntity;
import com.pcare.common.net.Api;
import com.pcare.common.table.UserDao;
import com.pcare.common.table.UserTableController;
import com.pcare.common.util.FaceUtil;
import com.pcare.common.util.LogUtil;
import com.pcare.rebot.R;

public class FaceActivity extends BaseActivity {

    private TextureView textureView;
    private FaceUtil faceUtil;
    private TextView titleView;
    private RelativeLayout faceView;
    private int type;//0表示人脸注册，1表示人脸登陆
    private boolean isSuccess = false;//表示是否成功的标志


    @Override
    public int getLayoutId() {
        return R.layout.activity_face;
    }

    @Override
    public void start() {
        super.start();
        titleView = findViewById(R.id.face_title);
        textureView = findViewById(R.id.look_container);
        faceView = findViewById(R.id.look_container_layout);
        faceUtil = new FaceUtil(this, textureView, this.getResources().getConfiguration().orientation)
                .setTimeOut(10000,1000)
                .setFaceDetectListener(new FaceUtil.FaceDetectListener() {
                    @Override
                    public void detectSucess() {
                        isSuccess = true;
//                        Toast.makeText(getApplicationContext(), "添加新用户成功", Toast.LENGTH_SHORT).show();
//                        setResult(200);
//
//                        finish();
                    }

                    @Override
                    public void detectFail() {
//                        Toast.makeText(getApplicationContext(), "人脸注册失败", Toast.LENGTH_SHORT).show();
//                        setResult(-1);
//                        finish();
                    }
                })
                .setFaceCompareListener(new FaceUtil.FaceCompareListener() {
                    @Override
                    public void compareSucess(String userId) {
                        if (UserDao.get(getSelfActivity()).setCurrentUser(userId)) {

                            Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                            setResult(200);
//                                startActivity(new Intent(FaceActivity.this, MainActivity.class));

                        }

                        finish();
                    }

                    @Override
                    public void compareFail() {
                        Toast.makeText(getApplicationContext(), "人脸登陆失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        type = getIntent().getIntExtra("type", 0);
        if (type == 0) {
            faceUtil.init(Api.FACEURL, getIntent().getStringExtra("userId"));
            titleView.setText("人脸注册");
            setTimeOut();
        } else {
            faceUtil.init(Api.FACEURL, null);
            titleView.setText("登陆");
        }
    }

    private void setTimeOut(){
        new CountDownTimer(40000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                LogUtil.i("时间："+ millisUntilFinished);
                if(millisUntilFinished < 30000){
                    faceView.setVisibility(View.GONE);
                    stop();
                }

            }

            @Override
            public void onFinish() {
                if(isSuccess){
                    LogUtil.i("添加新用户成功");
                    Toast.makeText(getApplicationContext(), "添加新用户成功", Toast.LENGTH_SHORT).show();
                    setResult(200);
                }else {
                    Toast.makeText(getApplicationContext(), "人脸注册失败", Toast.LENGTH_SHORT).show();
                    setResult(-1);
                }
                finish();
            }
        }.start();
    }

    @Override
    protected IPresenter bindPresenter() {
        return null;
    }

    public void stop() {
        if (null != faceUtil)
            faceUtil.closeSession();
    }

    public void back(View view) {
        stop();
        finish();
    }

    @Override
    protected void onStop() {
        stop();
        super.onStop();
    }
}
