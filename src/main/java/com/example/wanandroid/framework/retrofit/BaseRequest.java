package com.example.wanandroid.framework.retrofit;

import com.example.wanandroid.framework.mvp.RxPresenter;

public class BaseRequest {
    protected NetApi webRequest;
    protected RxPresenter rxPresenter;

    public BaseRequest(RxPresenter rxPresenter){
        webRequest = RetrofitService.getInstance().createRetrofit().create(NetApi.class);
        this.rxPresenter = rxPresenter;
    }
}
