package com.pcare.rebot.model;

import com.pcare.common.entity.NetResponse;
import com.pcare.common.entity.UserEntity;
import com.pcare.common.net.Api;
import com.pcare.common.net.RetrofitHelper;
import com.pcare.rebot.contract.RegisterContract;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.ResponseBody;

/**
 * @Author: gl
 * @CreateDate: 2019/11/18
 * @Description:
 */
public class RegisterModel implements RegisterContract.Model {
    @Override
    public void register(UserEntity userInfo, DisposableSingleObserver<NetResponse> observer) {
        //第一个参数表示要修改哪个网络请求的BaseURL，第二个参数表示要修改成什么样的URL
//        RetrofitUrlManager.getInstance().putDomain(Api.URL_VALUE_SECOND, Api.BASEURL2);
        RetrofitHelper.getInstance()
                .getRetrofit()
                .create(Api.class)
                .register(userInfo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribeWith(observer);
    }


    @Override
    public void verifiedName(String userName, DisposableSingleObserver<NetResponse> observer) {
        JSONObject object = new JSONObject();
        try {
            object.putOpt("name",userName);
            RetrofitHelper.getInstance()
                    .getRetrofit()
                    .create(Api.class)
                    .varifyUserName(object)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribeWith(observer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
