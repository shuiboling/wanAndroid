package com.example.wanandroid.presenter;

import android.util.Log;

import com.example.wanandroid.contract.LoginContract;
import com.example.wanandroid.framework.mvp.RxPresenter;
import com.example.wanandroid.framework.retrofit.CommonResponse;
import com.example.wanandroid.network.LoginRequest;
import com.example.wanandroid.framework.retrofit.OnNetworkListener;
import com.example.wanandroid.network.response.LoginResp;

public class LoginPresenter extends RxPresenter<LoginContract.View> implements LoginContract.Presenter {
    LoginRequest loginRequest = new LoginRequest(this);

    @Override
    public void login(String name,String password){
        loginRequest.login(name, password, new OnNetworkListener<CommonResponse<LoginResp>>() {

            @Override
            public void onSuccess(CommonResponse<LoginResp> data) {
                mView.loginSuccess();
            }

            @Override
            public void onFail() {
                mView.loginFail();
            }
        });
    }

    @Override
    public void cloud() {
        loginRequest.cloud(new OnNetworkListener() {
            @Override
            public void onSuccess(Object data) {
                Log.d("zyy",data+"");
            }

            @Override
            public void onFail() {

            }
        });
    }

    public void register(){
        loginRequest.register("", "", new OnNetworkListener() {
            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onFail() {

            }
        });
    }

}
