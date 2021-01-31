package com.example.wanandroid.network;

import android.util.Log;

import com.example.wanandroid.framework.mvp.RxPresenter;
import com.example.wanandroid.framework.retrofit.BaseRequest;
import com.example.wanandroid.framework.retrofit.CommonResponse;
import com.example.wanandroid.framework.retrofit.OnNetworkListener;
import com.example.wanandroid.framework.retrofit.RxHelper;
import com.example.wanandroid.framework.retrofit.SubscribeHelper;
import com.example.wanandroid.network.response.LoginResp;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRequest extends BaseRequest {


    public LoginRequest(RxPresenter rxPresenter) {
        super(rxPresenter);
    }

    public void login(String username, String password, final OnNetworkListener listener) {
        Observable<CommonResponse<LoginResp>> observable = webRequest.login(username,password);
        observable.compose(RxHelper.rxScheduleHelper())
                .map(RxHelper.<LoginResp>result())
                .subscribe(new SubscribeHelper<CommonResponse<LoginResp>>(rxPresenter) {
                    @Override
                    public void onNext(CommonResponse<LoginResp> value) {
                        listener.onSuccess(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        listener.onFail();
                    }
                });
    }

    public void register(String username, String password, final OnNetworkListener listener) {
        Observable<ResponseBody> observable = webRequest.register(",","","");
        observable.compose(RxHelper.rxScheduleHelper())
                .subscribe(new SubscribeHelper(rxPresenter) {

                    @Override
                    public void onNext(Object value) {
                        listener.onSuccess(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        listener.onFail();
                    }
                });
    }
}


