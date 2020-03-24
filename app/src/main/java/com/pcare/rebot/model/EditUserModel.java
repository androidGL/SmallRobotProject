package com.pcare.rebot.model;

import com.pcare.common.entity.NetResponse;
import com.pcare.common.entity.UserEntity;
import com.pcare.common.net.Api;
import com.pcare.common.net.RetrofitHelper;
import com.pcare.rebot.contract.EditUserContract;
import com.pcare.rebot.contract.RegisterContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * @Author: gl
 * @CreateDate: 2019/11/18
 * @Description:
 */
public class EditUserModel implements EditUserContract.Model {

    @Override
    public void editUser(UserEntity userInfo, DisposableSingleObserver<NetResponse> observer) {
        RetrofitHelper.getInstance()
                .getRetrofit()
                .create(Api.class)
                .modifyUser(userInfo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribeWith(observer);
    }
}
