package com.pcare.common.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pcare.common.R;
import com.pcare.common.util.LogUtil;
import com.pcare.common.view.scroll_picker_view.ScrollPickerAdapter;
import com.pcare.common.view.scroll_picker_view.ScrollPickerView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author: gl
 * @CreateDate: 2019/11/12
 * @Description:
 */
public class YearPickerDialog extends DatePickerDialog {
    private final OnCancleClickListener onCancleClickListener;//取消的点击事件
    private final OnConfirmClickListener onConfirmClickListener;//确认的点击事件
    private int currentYear;

    public interface OnConfirmClickListener{
        void onClick(int currentYear);
    }
    public interface OnCancleClickListener{
        void onClick(View view);
    }

    //    构造方法
    private YearPickerDialog(Context context,Builder builder) {
        super(context, R.style.AlertDialogUtil);
        this.onCancleClickListener = builder.mOnCancleClickListener;
        this.onConfirmClickListener = builder.mOnConfirmClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_picker_dialog);
        initView();
    }
    public static Builder Builder(Context context){
        return new Builder(context);
    }
    private void initView() {
        ImageView btnConfirm = findViewById(R.id.confirm);
        ImageView btnCancel = findViewById(R.id.close);
        List<Integer> list = new ArrayList<>();
       int y =  Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1900; i <= y; i++) {
            list.add(i);
        }
        ScrollPickerView pickerView = findViewById(R.id.picker_view);
        pickerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pickerView.setAdapter(new ScrollPickerAdapter.ScrollPickerAdapterBuilder<Integer>(getContext())
                .setDataList(list)
                .visibleItemNumber(11)
                .setSelectedPosition(y - 50)
                .setDivideLineColor("#E5E5E5")
                .setItemViewProvider(null)
                .setOnScrolledListener(currentItemView -> {
                    if(null == currentItemView || null == currentItemView.getTag())
                        currentYear = 0;
                    else
                        currentYear = (int) currentItemView.getTag();
                }).build());
        btnConfirm.setOnClickListener(v -> {
            if(null != onConfirmClickListener){
                onConfirmClickListener.onClick(currentYear);
            }
            dismiss();
        });
        btnCancel.setOnClickListener(v -> {
            if(null != onCancleClickListener){
                onCancleClickListener.onClick(v);
            }
            dismiss();
        });
    }
    public YearPickerDialog shown(){
        show();
        return this;
    }
    //创建Builder类
    public static class Builder{
        private OnConfirmClickListener mOnConfirmClickListener;
        private OnCancleClickListener mOnCancleClickListener;
        private Context mContext;

        private Builder(Context mContext) {
            this.mContext = mContext;
        }

        public Builder setOnConfirmClickListener(OnConfirmClickListener mOnConfirmClickListener) {
            this.mOnConfirmClickListener = mOnConfirmClickListener;
            return this;
        }

        public Builder setOnCancleClickListener(OnCancleClickListener mOnCancelClickListener) {
            this.mOnCancleClickListener = mOnCancelClickListener;
            return this;
        }

        //build时返回dialog对象
        public YearPickerDialog build(){
            return new YearPickerDialog(mContext, this);
        }
    }

}
