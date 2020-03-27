package com.pcare.rebot.contract;

import com.pcare.common.base.IView;
import com.pcare.common.entity.NetResponse;
import com.pcare.common.entity.UserEntity;

import org.json.JSONObject;

import io.reactivex.observers.DisposableSingleObserver;
import okhttp3.ResponseBody;

/**
 * @Author: gl
 * @CreateDate: 2019/11/18
 * @Description:
 */
public interface RegisterContract {
    interface Model{
        void register(UserEntity userInfo, DisposableSingleObserver<NetResponse> observer);
        void verifiedName(String userName, DisposableSingleObserver<NetResponse> observer);
    }
    interface View extends IView{
        void setUserId(String id);
        void saveUser(UserEntity userInfo);
        void verifiedName(boolean visible);
    }
    interface Presenter{
        void register(UserEntity u);
        void verifiedName(String userName);
    }
}
