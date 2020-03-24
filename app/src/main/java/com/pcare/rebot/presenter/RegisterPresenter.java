package com.pcare.rebot.presenter;

import android.util.Log;
import android.widget.Toast;

import com.pcare.common.base.BasePresenter;
import com.pcare.common.entity.NetResponse;
import com.pcare.common.entity.UserEntity;
import com.pcare.common.table.UserTableController;
import com.pcare.common.util.LogUtil;
import com.pcare.rebot.contract.RegisterContract;
import com.pcare.rebot.model.RegisterModel;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.SingleObserver;
import io.reactivex.observers.DisposableSingleObserver;

/**
 * @Author: gl
 * @CreateDate: 2019/11/18
 * @Description:
 */
public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter {

    private RegisterContract.Model model;

    public RegisterPresenter(RegisterContract.View view) {
        super(view);
        model = new RegisterModel();
    }

    @Override
    public void register(UserEntity u) {
        DisposableSingleObserver observer = new DisposableSingleObserver<NetResponse>() {

            @Override
            public void onSuccess(NetResponse value) {
                if (value.getStatus() == 0) {
                    getView().setUserId(value.getMsg());
                } else {
                    Toast.makeText(getView().getSelfActivity(), value.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        };
        addDisposable(observer);
        model.register(u, observer);
    }


    @Override
    public void verifiedName(String userName) {
        DisposableSingleObserver observer = new DisposableSingleObserver<NetResponse>() {
            @Override
            public void onSuccess(NetResponse response) {
                if (0 == response.getStatus())
                    getView().verifiedName(true);
                else
                    getView().verifiedName(false);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getView().verifiedName(false);
            }
        };
        addDisposable(observer);
        model.verifiedName(userName, observer);
    }

}
