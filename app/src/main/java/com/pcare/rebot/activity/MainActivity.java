package com.pcare.rebot.activity;

import android.content.Intent;
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
import com.pcare.common.table.UserDao;
import com.pcare.common.util.APPConstant;
import com.pcare.common.util.PermissionHelper;
import com.pcare.common.view.CommonAlertDialog;
import com.pcare.rebot.R;
import com.pcare.rebot.activity.web.IndexActivity;

@Route(path = "app/main")
public class MainActivity extends BaseActivity {
    private TextView currentUserView,exchangeView,emptyView;
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
        drawerLayout = findViewById(R.id.main_drawer);
    }

    @Override
    protected void initResumeData() {
        super.initResumeData();
        if(isLogin()) {
            currentUserView.setVisibility(View.VISIBLE);
            exchangeView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            currentUserView.setText(UserDao.get(getApplicationContext()).getCurrentUser().getUserName());
        }else {
            currentUserView.setVisibility(View.GONE);
            exchangeView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    public void toInquiryPage(View view) {
//        if (!isLogin()) {
//            Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
//            return;
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
        return null != UserDao.getCurrentUserId() && !TextUtils.isEmpty(UserDao.getCurrentUserId());
    }

    public void exchangeUser(View view) {
        //跳转到人脸识别的界面
        startActivity(new Intent(this, FaceActivity.class)
                .putExtra("type", 1)
                .putExtra("resource", "login"));
    }

    public void clickMenu(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void toSettingPage(View view) {
        startActivity(new Intent(MainActivity.this, SettingActivity.class));
    }

    public void toRegisterPage(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
