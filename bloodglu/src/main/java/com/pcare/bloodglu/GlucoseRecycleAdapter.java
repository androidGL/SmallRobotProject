package com.pcare.bloodglu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pcare.common.entity.GlucoseEntity;
import com.pcare.common.entity.NetResponse;
import com.pcare.common.net.Api;
import com.pcare.common.net.RetrofitHelper;
import com.pcare.common.util.CommonToast;
import com.pcare.common.util.CommonUtil;
import com.pcare.common.util.LogUtil;
import com.pcare.common.view.CommonAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author: gl
 * @CreateDate: 2019/12/3
 * @Description:
 */
public class GlucoseRecycleAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<GlucoseEntity> glucoseEntities;

    public GlucoseRecycleAdapter(Context context, List<GlucoseEntity> entities) {
        this.mContext = context;
        this.glucoseEntities =entities;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_feature_gls_item, parent, false);
        return new GluViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GluViewHolder) {
            ((GluViewHolder) holder).timeView.setText(glucoseEntities.get(position).getCheck_time());

            try {
                ((GluViewHolder) holder).detailsView.setText(mContext.getResources().getStringArray(R.array.gls_type)[glucoseEntities.get(position).getSample_type()]);
            } catch (final ArrayIndexOutOfBoundsException e) {
                ((GluViewHolder) holder).detailsView.setText(mContext.getResources().getStringArray(R.array.gls_type)[0]);
            }
            ((GluViewHolder) holder).concentrationView.setText(mContext.getString(R.string.gls_value,
                    glucoseEntities.get(position).getGlucose())+mContext.getString(R.string.gls_unit_mmolpl));
            try {
                ((GluViewHolder) holder).locationView.setText( mContext.getResources().getStringArray(R.array.gls_location)[glucoseEntities.get(position).getSample_location()]);
            } catch (final ArrayIndexOutOfBoundsException e) {
                ((GluViewHolder) holder).locationView.setText(mContext.getResources().getStringArray(R.array.gls_location)[0]);
            }
            ((GluViewHolder) holder).itemView.setLongClickable(true);
            ((GluViewHolder) holder).itemView.setOnLongClickListener(v -> {
                CommonAlertDialog.Builder(mContext)
                        .setCancelText("否")
                        .setConfirmText("删除")
                        .setMessage("是否删除这个结果")
                        .setOnConfirmClickListener(view -> {
                            delete(glucoseEntities.get(position).getId(),position);
                        }).build().shown();
                return true;
            });
        }
    }
    private void delete(String id,int position){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RetrofitHelper.getInstance().getRetrofit()
                .create(Api.class)
                .deleteGLU("delete", jsonObject)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribeWith(new DisposableSingleObserver<NetResponse>() {
                    @Override
                    public void onSuccess(NetResponse value) {
                        if(value.getStatus() == 0){
                            CommonToast.showShortToast(mContext,"删除数据成功", CommonToast.SUCCESS);
                            glucoseEntities.remove(position);
                            notifyDataSetChanged();
                            return;
                        }
                        CommonToast.showShortToast(mContext,"删除数据失败", CommonToast.FAILED);
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonToast.showShortToast(mContext,"删除数据失败", CommonToast.FAILED);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return glucoseEntities.size();
    }

    private class GluViewHolder extends RecyclerView.ViewHolder {
        private TextView timeView, detailsView, locationView, concentrationView;
        private View itemView;

        public GluViewHolder(@NonNull View itemView) {
            super(itemView);
            timeView = itemView.findViewById(R.id.time);
            detailsView = itemView.findViewById(R.id.details);
            locationView = itemView.findViewById(R.id.gls_location);
            concentrationView = itemView.findViewById(R.id.gls_concentration);
            this.itemView = itemView;

        }
    }
}
