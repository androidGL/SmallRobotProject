package com.pcare.rebot.presenter;

import android.widget.Toast;

import com.pcare.common.base.BasePresenter;
import com.pcare.common.entity.NetResponse;
import com.pcare.common.entity.UserEntity;
import com.pcare.common.table.UserDao;
import com.pcare.common.table.UserTableController;
import com.pcare.common.util.CommonToast;
import com.pcare.rebot.contract.EditUserContract;
import com.pcare.rebot.contract.RegisterContract;
import com.pcare.rebot.model.EditUserModel;
import com.pcare.rebot.model.RegisterModel;

import io.reactivex.observers.DisposableSingleObserver;

/**
 * @Author: gl
 * @CreateDate: 2019/11/18
 * @Description:
 */
public class EditUserPresenter extends BasePresenter<EditUserContract.View> implements EditUserContract.Presenter {

    private EditUserContract.Model model;

    public EditUserPresenter(EditUserContract.View view) {
        super(view);
        model = new EditUserModel();
    }


    @Override
    public void editUser(UserEntity u) {
//        getView().editUser(u);
        DisposableSingleObserver observer = new DisposableSingleObserver<NetResponse>() {
            @Override
            public void onSuccess(NetResponse response) {
                if(response.getStatus() == 0) {
                    CommonToast.showShortToast(getView().getSelfActivity(),response.getMsg(),CommonToast.SUCCESS);
                    getView().updateUser(true);
                }else {
                    CommonToast.showShortToast(getView().getSelfActivity(),response.getMsg(),CommonToast.FAILED);
                    getView().updateUser(false);
                }

            }

            @Override
            public void onError(Throwable e) {
                CommonToast.showShortToast(getView().getSelfActivity(),"修改用户信息失败",CommonToast.FAILED);
                getView().updateUser(false);
                e.printStackTrace();
            }
        };
        addDisposable(observer);
        model.editUser(u,observer);
    }

}
