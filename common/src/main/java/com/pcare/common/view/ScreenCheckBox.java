package com.pcare.common.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.pcare.common.R;
import com.pcare.common.util.LogUtil;

/**
 * @Author: gl
 * @CreateDate: 2019/11/12
 * @Description: 自定义选择框
 */
public class ScreenCheckBox extends RadioButton implements CompoundButton.OnCheckedChangeListener {
    public ScreenCheckBox(Context context) {
        this(context, (String) null);
    }
    public ScreenCheckBox(Context context,String text) {
        super(context);
        setTextStyle();
        setOnCheckedChangeListener(this);
        if(null != text)
            setText(text);
    }

    public ScreenCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextStyle();
        setOnCheckedChangeListener(this);

    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }

    private void setTextStyle() {
        setButtonDrawable(null);
        setBackgroundResource(R.drawable.bg_checkbox);
        setButtonTintMode(PorterDuff.Mode.CLEAR);
        setGravity(TEXT_ALIGNMENT_CENTER);
        setPadding(20,10,20,10);
        setClickable(true);
        if(isChecked()){
            setText("√" + getText());
            setTextColor(Color.parseColor("#d52c34"));
        }
    }
    public String getTextString(){
        if(getText().toString().contains("√")){
            return getText().toString().substring(1);
        }
        return getText().toString();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            setText("√"+getText());
            buttonView.setTextColor(Color.parseColor("#d52c34"));
        }else if(getText().toString().contains("√")){
            setText(getText().subSequence(1,getText().length()));
            setTextColor(Color.parseColor("#000000"));
        }
    }
}
