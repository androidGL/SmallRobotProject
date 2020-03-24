package com.pcare.common.view.scroll_picker_view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pcare.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: gl
 * @CreateDate: 2020/3/23
 * @Description:
 */
public class ScrollPickerAdapter<T> extends RecyclerView.Adapter<ScrollPickerAdapter.ScrollPickerAdapterHolder> implements ScrollPickerView.IPickerViewOperation {
    private List<T> mDataList;
    private Context mContext;
    private OnClickListener mOnItemClickListener;
    private OnScrollListener mOnScrollListener;
    private int mSelectedItemOffset;
    private int mVisibleItemNum = 3;
    private int mSelectedPosition;
    private IViewProvider mViewProvider;
    private int mLineColor;
    private int maxItemH = 0;
    private int maxItemW = 0;

    private ScrollPickerAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ScrollPickerAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mViewProvider == null) {
            mViewProvider = new DefaultItemViewProvider();
        }
        return new ScrollPickerAdapterHolder(LayoutInflater.from(mContext).inflate(mViewProvider.resLayout(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ScrollPickerAdapterHolder holder, int position) {
        mViewProvider.onBindView(holder.itemView, mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getSelectedItemOffset() {
        return mSelectedItemOffset;
    }

    @Override
    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    @Override
    public int getVisibleItemNumber() {
        return mVisibleItemNum;
    }

    @Override
    public int getLineColor() {
        return mLineColor;
    }

    @Override
    public void updateView(View itemView, boolean isSelected) {
        mViewProvider.updateView(itemView, isSelected);
        adaptiveItemViewSize(itemView);
        if (isSelected && mOnScrollListener != null) {
            mOnScrollListener.onScrolled(itemView);
        }
        itemView.setOnClickListener(isSelected ? new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onSelectedItemClicked(v);
                }
            }
        } : null);
    }

    private void adaptiveItemViewSize(View itemView) {
        int h = itemView.getHeight();
        if (h > maxItemH) {
            maxItemH = h;
        }

        int w = itemView.getWidth();
        if (w > maxItemW) {
            maxItemW = w;
        }

        itemView.setMinimumHeight(maxItemH);
        itemView.setMinimumWidth(maxItemW);
    }

    static class ScrollPickerAdapterHolder extends RecyclerView.ViewHolder {
        private View itemView;

        private ScrollPickerAdapterHolder(@NonNull View view) {
            super(view);
            itemView = view;
        }
    }

    public interface OnClickListener {
        void onSelectedItemClicked(View v);
    }

    public interface OnScrollListener {
        void onScrolled(View currentItemView);
    }

    public static class ScrollPickerAdapterBuilder<T> {
        private ScrollPickerAdapter mAdapter;

        public ScrollPickerAdapterBuilder(Context context) {
            mAdapter = new ScrollPickerAdapter<T>(context);
        }


        public ScrollPickerAdapterBuilder<T> setDataList(List<T> list) {
            mAdapter.mDataList=list;
            return this;
        }

        public ScrollPickerAdapterBuilder<T> setOnClickListener(OnClickListener listener) {
            mAdapter.mOnItemClickListener = listener;
            return this;
        }

        //设置显示的数量
        public ScrollPickerAdapterBuilder<T> visibleItemNumber(int num) {
            mAdapter.mVisibleItemNum = num;
            mAdapter.mSelectedItemOffset = num/2;
            return this;
        }

        public ScrollPickerAdapterBuilder<T> setItemViewProvider(IViewProvider viewProvider) {
            mAdapter.mViewProvider = viewProvider;
            return this;
        }
        public ScrollPickerAdapterBuilder<T> setSelectedPosition(int position) {
            mAdapter.mSelectedPosition = position;
            return this;
        }

        public ScrollPickerAdapterBuilder<T> setDivideLineColor(String colorString) {
            mAdapter.mLineColor = Color.parseColor(colorString);
            return this;
        }

        public ScrollPickerAdapterBuilder<T> setOnScrolledListener(OnScrollListener listener) {
            mAdapter.mOnScrollListener = listener;
            return this;
        }

        public ScrollPickerAdapter build() {
            LogUtil.i("多加的数"+mAdapter.mVisibleItemNum/2);
                for (int i = 0;i <  mAdapter.mVisibleItemNum/2-1;i++) {
                    mAdapter.mDataList.add(0,null);
                    mAdapter.mDataList.add(null);
                }
                if( mAdapter.mVisibleItemNum % 2 == 0)
                    mAdapter.mDataList.add(0,null);

            mAdapter.notifyDataSetChanged();
            return mAdapter;
        }


    }
}