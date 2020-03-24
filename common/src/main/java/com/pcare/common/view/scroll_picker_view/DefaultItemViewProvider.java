package com.pcare.common.view.scroll_picker_view;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.pcare.common.R;

/**
 * @Author: gl
 * @CreateDate: 2020/3/23
 * @Description:
 */
public class DefaultItemViewProvider implements IViewProvider<Object> {
    @Override
    public int resLayout() {
        return R.layout.scroll_picker_default_item_layout;
    }

    @Override
    public void onBindView(View view, Object text) {
        if(null == text)
            return;
        TextView tv = view.findViewById(R.id.tv_content);
        tv.setText(text.toString());
        view.setTag(text);
        tv.setTextSize(18);
    }

    @Override
    public void updateView(View itemView, boolean isSelected) {
        TextView tv = itemView.findViewById(R.id.tv_content);
        tv.setTextSize(isSelected ? 18 : 14);
        tv.setTextColor(Color.parseColor(isSelected ? "#ED5275" : "#000000"));
    }
}
