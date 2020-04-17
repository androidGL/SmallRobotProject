package com.pcare.rebot.contract;

import com.pcare.common.base.IView;
import com.pcare.common.entity.NetResponse;
import com.pcare.common.entity.UserEntity;

import java.util.List;

import io.reactivex.SingleObserver;

/**
 * @Author: gl
 * @CreateDate: 2019/11/28
 * @Description:
 */
public interface UserListContract {
    interface Model{
        void getUserList(String robotId,SingleObserver<NetResponse> observer);
        void deleteUser(String userId, SingleObserver<NetResponse> observer);
    }
    interface View extends IView {
        void refreshList(String msg,String id);
        void setUserList(List<UserEntity> list);
    }
    interface Presenter{
        void deleteUser(String userId);
        void getUserList(String robotId);
    }
}
