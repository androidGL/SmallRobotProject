package com.pcare.rebot.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pcare.common.base.BaseActivity;
import com.pcare.common.entity.NetResponse;
import com.pcare.common.entity.UserEntity;
import com.pcare.common.net.Api;
import com.pcare.common.net.RetrofitHelper;
import com.pcare.common.table.UserDao;
import com.pcare.common.util.CommonUtil;
import com.pcare.common.util.LogUtil;
import com.pcare.common.view.CheckBoxGroup;
import com.pcare.common.view.CommonAlertDialog;
import com.pcare.common.view.ScreenCheckBox;
import com.pcare.common.view.YearPickerDialog;
import com.pcare.rebot.R;
import com.pcare.rebot.contract.RegisterContract;
import com.pcare.rebot.presenter.RegisterPresenter;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * @Author: gl
 * @CreateDate: 2019/11/12
 * @Description: 注册页面
 */
public class RegisterActivity extends BaseActivity<RegisterPresenter> implements RegisterContract.View {

    private TextView yearView;
    private ScreenCheckBox male, female;
    private EditText infoNameView, infoNickNameView, infoStatureView, infoWeightView, healthyView4;
    private TextView infoTypeNameView;
    private UserEntity mUserInfo;
    private int selectYear;
    private int userType;
    private String userTypeName;
    private String infoHistory;
    private CheckBoxGroup historyCheckboxView;
    private String[] history;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected RegisterPresenter bindPresenter() {
        return new RegisterPresenter((RegisterContract.View) getSelfActivity());
    }

    @Override
    public void initView() {
        super.initView();
        yearView = findViewById(R.id.info_year);
        male = findViewById(R.id.checkbox_male);
        female = findViewById(R.id.checkbox_female);
        infoTypeNameView = findViewById(R.id.info_type_name);
        infoNameView = findViewById(R.id.info_name);
        infoNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s))
                    presenter.verifiedName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        infoNickNameView = findViewById(R.id.info_nick_name);
        infoStatureView = findViewById(R.id.info_stature);
        infoWeightView = findViewById(R.id.info_weight);
        infoWeightView = findViewById(R.id.info_weight);
        healthyView4 = findViewById(R.id.healthy4);
        historyCheckboxView = findViewById(R.id.view_history_checkbox);
        history = getResources().getStringArray(R.array.history);
        for (String name : history) {
            historyCheckboxView.addView(new ScreenCheckBox(this, name));
        }

    }

    @Override
    public void start() {
        super.start();
        mUserInfo = new UserEntity();

    }

    @SuppressWarnings("ResourceType")
    public void selectYear(View view) {
        YearPickerDialog.Builder(this)
                .setOnConfirmClickListener(currentYear -> {
                    selectYear = currentYear;
                    yearView.setText(selectYear + "年");
                })
                .build()
                .shown();
    }

    private void setUserType(int type) {
        userType = type;
        switch (type) {
            case 0:
                male.setChecked(true);
                female.setChecked(false);
                userTypeName = "爷爷";
                break;
            case 1:
                female.setChecked(true);
                male.setChecked(false);
                userTypeName = "奶奶";
                break;
            case 2:
                male.setChecked(true);
                female.setChecked(false);
                userTypeName = "叔叔";
                break;
            case 3:
                female.setChecked(true);
                male.setChecked(false);
                userTypeName = "阿姨";
                break;
        }
    }

    public void selectID(View view) {
        setUserType(Integer.parseInt(view.getTag().toString()));
        infoTypeNameView.setText(userTypeName);
    }

    @Override
    public void setUserId(String id) {
        //TODO 拿到了userId
        LogUtil.i("id: " + id);
        if (!TextUtils.isEmpty(id)) {
            mUserInfo.setUser_id(id);
            startActivityForResult(new Intent(this, FaceActivity.class)
                    .putExtra("type", 0)
                    .putExtra("userId", id), 100);
        }

    }

    @Override
    public void saveUser(UserEntity userInfo) {
        mUserInfo = userInfo;
    }


    @Override
    public void verifiedName(boolean visible) {
        findViewById(R.id.view_infoname_tip).setVisibility(visible ? View.GONE : View.VISIBLE);
    }

    public void toRegister(View view) {
        if (TextUtils.isEmpty(userTypeName)) {
            Toast.makeText(getApplicationContext(), "请您选择身份类型", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(infoNameView.getText().toString()) || TextUtils.isEmpty(infoNickNameView.getText().toString())) {
            Toast.makeText(getApplicationContext(), "请您完善用户名和昵称", Toast.LENGTH_SHORT).show();
            return;
        }
        if (findViewById(R.id.view_infoname_tip).getVisibility() == View.VISIBLE) {
            Toast.makeText(getApplicationContext(), "请您重新设置用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        infoHistory = "";
        for (int i = 0; i < historyCheckboxView.getChildCount(); i++) {
            ScreenCheckBox checkBox = (ScreenCheckBox) historyCheckboxView.getChildAt(i);
            if (checkBox.isChecked()) {
                infoHistory += checkBox.getTextString() + ",";
            }
        }
        if (!TextUtils.isEmpty(infoHistory)) {
            infoHistory = infoHistory.substring(0, infoHistory.length() - 1);
        }
        mUserInfo.setUserName(infoNameView.getText().toString());
        mUserInfo.setNickName(infoNickNameView.getText().toString());
        mUserInfo.setPassword("123456");
        mUserInfo.setUserType(userType);
        if (male.isChecked())
            mUserInfo.setUserGender(0);
        else
            mUserInfo.setUserGender(1);
        mUserInfo.setUserBirthYear(selectYear);
        if (!TextUtils.isEmpty(infoWeightView.getText().toString()))
            mUserInfo.setUserWeight(Integer.parseInt(infoWeightView.getText().toString()));
        if (!TextUtils.isEmpty(infoStatureView.getText().toString()))
            mUserInfo.setUserStature(Integer.parseInt(infoStatureView.getText().toString()));
        mUserInfo.setUserRobotId(CommonUtil.getUUID(getApplicationContext()));
        mUserInfo.setUserHistoty(infoHistory);

        LogUtil.i(CommonUtil.entityToJson(mUserInfo));
        presenter.register(mUserInfo);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 200) {
            LogUtil.i("注册成功，保存用户数据");
            UserDao.get(getApplicationContext()).insertUser(mUserInfo);
            UserDao.get(getApplicationContext()).setCurrentUser(mUserInfo);
            LogUtil.i(CommonUtil.entityToJson(mUserInfo));
            startActivity(new Intent(this, MainActivity.class));
        }else if(requestCode == 100 && resultCode == -1){
            JSONObject object = new JSONObject();
            try {
                object.putOpt("user_id",mUserInfo.getUser_id());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RetrofitHelper.getInstance().getRetrofit()
                    .create(Api.class)
                    .deleteDeepUser(object)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribeWith(new DisposableSingleObserver<NetResponse>() {
                        @Override
                        public void onSuccess(NetResponse value) {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
        }
    }
}
