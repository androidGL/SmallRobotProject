package com.pcare.rebot.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.pcare.common.base.BaseActivity;
import com.pcare.common.base.IPresenter;
import com.pcare.common.entity.UserEntity;
import com.pcare.common.table.UserDao;
import com.pcare.common.util.APPConstant;
import com.pcare.common.util.CommonUtil;
import com.pcare.common.util.LogUtil;
import com.pcare.common.util.PermissionHelper;
import com.pcare.common.view.CommonAlertDialog;
import com.pcare.rebot.R;
import com.pcare.rebot.activity.flutter.FlutterFragmentPage;
import com.pcare.rebot.activity.web.IndexActivity;

@Route(path = "app/main")
public class MainActivity extends BaseActivity {
    private TextView currentUserView,exchangeView,emptyView,emptyLoginView;
    private DrawerLayout drawerLayout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main_drawer;
    }

    @Override
    protected IPresenter bindPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        super.initView();
        //申请权限
        PermissionHelper.requestAllPermission(this);
        currentUserView = findViewById(R.id.view_current_user);
        exchangeView = findViewById(R.id.view_exchange);
        emptyView = findViewById(R.id.view_empty_user);
        emptyLoginView = findViewById(R.id.view_empty_user_login);
        drawerLayout = findViewById(R.id.main_drawer);
        emptyLoginView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);  //下划线
        emptyLoginView.getPaint().setAntiAlias(true);//设置抗锯齿，使线条平滑
    }

    @Override
    protected void initResumeData() {
        super.initResumeData();
        if(isLogin()) {
            LogUtil.i("currentUser: "+UserDao.get(getApplicationContext()).getCurrentUser().toString());
            currentUserView.setVisibility(View.VISIBLE);
            exchangeView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            emptyLoginView.setVisibility(View.GONE);
            currentUserView.setText(UserDao.get(getApplicationContext()).getCurrentUser().getNickName());
        }else {
            currentUserView.setVisibility(View.GONE);
            exchangeView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            emptyLoginView.setVisibility(View.VISIBLE);
        }
    }

    public void toInquiryPage(View view) {
//        if (!isLogin()) {
//            Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
//            return;
//        }
        if(null != UserDao.get(getApplicationContext()).getUserById("77a6188f-598d-3a90-b298-d50864548ee9")){
            UserDao.get(getApplicationContext()).setCurrentUser("77a6188f-598d-3a90-b298-d50864548ee9");
        }
//        if(null == UserDao.get(getApplicationContext()).getUserById("d507a1ad-b2a8-3b51-a2f7-846f8f58b0f0")){
//            UserEntity entity = new UserEntity();
//            entity.setUser_id("d507a1ad-b2a8-3b51-a2f7-846f8f58b0f0");
//            entity.setName("葛");
//            entity.setNickName("葛兰");
//            entity.setType(3);
//            entity.setBirth_year(1901);
//            entity.setHeight(189);
//            entity.setWeight(45);
//            UserDao.get(getApplicationContext()).insertUser(entity);
//            UserDao.get(getApplicationContext()).setCurrentUser("d507a1ad-b2a8-3b51-a2f7-846f8f58b0f0");
//        }
        startActivity(new Intent(MainActivity.this, IndexActivity.class));

    }

    public void toECGPage(View view) {
        if (!isLogin()) {
            Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = getPackageManager().getLaunchIntentForPackage(APPConstant.INQUIRY);
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void toPLUPage(View view) {
        if (!isLogin()) {
            Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }
        CommonAlertDialog.Builder(this)
                .setMessage("您是否空腹测量")
                .setOnConfirmClickListener(view1 -> {
                    ARouter.getInstance().build("/glu/main")
                            .withString("key1", "血糖检测")
                            .withString("emptiness","1")
                            .navigation();

                })
                .setOnCancleClickListener(view2 -> {
                    ARouter.getInstance().build("/glu/main")
                            .withString("key1", "血糖检测")
                            .withString("emptiness","0")
                            .navigation();

                }).setConfirmText("是").setCancelText("否").build().shown();

    }

    public void toBPPage(View view) {
        if (!isLogin()) {
            Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }
        ARouter.getInstance().build("/bp/main")
                .withString("key1", "血压检测")
                .navigation();
    }

    private boolean isLogin() {
        return null != UserDao.getCurrentUserId(getApplicationContext());
    }

    public void exchangeUser(View view) {
        //跳转到人脸识别的界面
        startActivityForResult(new Intent(this, FaceActivity.class)
                .putExtra("type", 1),200);
    }

    public void clickMenu(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void toSettingPage(View view) {
//        startActivity(new Intent(MainActivity.this, SettingActivity.class));
        startActivity(new Intent(MainActivity.this, FlutterFragmentPage.class));
    }

    public void toRegisterPage(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
    public void toLogin(View view) {
        //跳转到人脸识别的界面
        startActivityForResult(new Intent(this, FaceActivity.class)
                .putExtra("type", 1),100);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 200) {
            LogUtil.i("登陆成功，保存用户数据");
            initResumeData();
        }
    }
}
