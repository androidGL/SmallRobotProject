package com.pcare.common.view.scroll_picker_view;

import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Author: gl
 * @CreateDate: 2020/3/23
 * @Description:
 */
public interface IViewProvider<T> {
    @LayoutRes
    int resLayout();

    void onBindView(@NonNull View view, @Nullable T itemData);

    void updateView(@NonNull View itemView, boolean isSelected);
}
