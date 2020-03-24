package com.pcare.rebot.contract;

import com.pcare.common.base.IView;
import com.pcare.common.entity.NetResponse;
import com.pcare.common.entity.UserEntity;

import io.reactivex.observers.DisposableSingleObserver;
import okhttp3.ResponseBody;

/**
 * @Author: gl
 * @CreateDate: 2019/11/18
 * @Description:
 */
public interface EditUserContract {
    interface Model{
        void editUser(UserEntity userInfo, DisposableSingleObserver<NetResponse> observer);
    }
    interface View extends IView{
        void updateUser(boolean isUpdate);
    }
    interface Presenter{
        void editUser(UserEntity u);
    }
}
