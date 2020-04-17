package com.pcare.rebot.model;

import com.pcare.common.entity.NetResponse;
import com.pcare.common.net.Api;
import com.pcare.common.net.RetrofitHelper;
import com.pcare.common.util.LogUtil;
import com.pcare.rebot.contract.UserListContract;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author: gl
 * @CreateDate: 2019/11/28
 * @Description: 处理用户列表的model类
 */
public class UserListModel implements UserListContract.Model {

    @Override
    public void getUserList(String robotId, SingleObserver<NetResponse> observer) {
        LogUtil.i("robot_id--------" + robotId);
        JSONObject object = new JSONObject();
        try {
            object.putOpt("robot_id",robotId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RetrofitHelper.getInstance()
                .getRetrofit()
                .create(Api.class)
                .getUserList(object)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribeWith(observer);
    }

    @Override
    public void deleteUser(String userId, SingleObserver<NetResponse> observer) {
        JSONObject object = new JSONObject();
        try {
            object.putOpt("user_id",userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RetrofitHelper.getInstance()
                .getRetrofit()
                .create(Api.class)
                .deleteUser(object)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribeWith(observer);
    }
}
