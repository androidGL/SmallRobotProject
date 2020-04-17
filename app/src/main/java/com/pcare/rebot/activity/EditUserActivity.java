package com.pcare.rebot.activity;

import android.app.Dialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pcare.common.base.BaseActivity;
import com.pcare.common.entity.UserEntity;
import com.pcare.common.table.UserDao;
import com.pcare.common.util.CommonUtil;
import com.pcare.common.util.LogUtil;
import com.pcare.common.view.CheckBoxGroup;
import com.pcare.common.view.ScreenCheckBox;
import com.pcare.common.view.YearPickerDialog;
import com.pcare.rebot.R;
import com.pcare.rebot.contract.EditUserContract;
import com.pcare.rebot.presenter.EditUserPresenter;

/**
 * @Author: gl
 * @CreateDate: 2020/3/16
 * @Description:
 */
public class EditUserActivity extends BaseActivity<EditUserPresenter> implements EditUserContract.View {

    private TextView yearView;
    private ScreenCheckBox male, female;
    private EditText infoNameView, infoNickNameView, infoStatureView, infoWeightView, healthyView4;
    private TextView infoTypeNameView;
    private TextView titleView;
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
    protected EditUserPresenter bindPresenter() {
        return new EditUserPresenter((EditUserContract.View) getSelfActivity());
    }

    @Override
    public void initView() {
        super.initView();
        yearView = findViewById(R.id.info_year);
        male = findViewById(R.id.checkbox_male);
        female = findViewById(R.id.checkbox_female);
        infoTypeNameView = findViewById(R.id.info_type_name);
        infoNameView = findViewById(R.id.info_name);
        infoNameView.setEnabled(false);//不可编辑

        infoNickNameView = findViewById(R.id.info_nick_name);
        infoStatureView = findViewById(R.id.info_stature);
        infoWeightView = findViewById(R.id.info_weight);
        infoWeightView = findViewById(R.id.info_weight);
        titleView = findViewById(R.id.title);
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
        titleView.setText("修改用户信息");
        if (getIntent().hasExtra("userId")) {
            mUserInfo = UserDao.get(getApplicationContext()).getUserById(getIntent().getStringExtra("userId"));
            setViews(mUserInfo);
        }

    }

    private void setViews(UserEntity user) {
        yearView.setText(String.valueOf(user.getUserBirthYear()));
        infoNameView.setText(user.getUserName());
        setUserType(user.getUserType());
        infoTypeNameView.setText(userTypeName);
        infoNickNameView.setText(user.getNickName());
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

    public void toRegister(View view) {
        if (TextUtils.isEmpty(infoNickNameView.getText().toString())) {
            Toast.makeText(getApplicationContext(), "请您完善昵称", Toast.LENGTH_SHORT).show();
            return;
        }
        infoHistory = "";
        for (int i = 0; i < historyCheckboxView.getChildCount(); i++) {
            ScreenCheckBox checkBox = (ScreenCheckBox) historyCheckboxView.getChildAt(i);
            if (checkBox.isChecked()) {
                infoHistory += checkBox.getTextString() + ",";
            }
        }
        mUserInfo.setNickName(infoNickNameView.getText().toString());
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
        mUserInfo.setUserHistoty(infoHistory);
        LogUtil.i(CommonUtil.entityToJson(mUserInfo));
        presenter.editUser(mUserInfo);
    }

    @Override
    public void updateUser(boolean isUpdate) {
        if (isUpdate) {
            UserDao.get(getApplicationContext()).updateUser(mUserInfo);
            finish();
        }
    }
}
