package com.example.wanandroid.framework.retrofit;

import com.example.wanandroid.framework.mvp.RxPresenter;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class SubscribeHelper<T> implements Observer<T> {

    public RxPresenter rxPresenter;

    protected SubscribeHelper(RxPresenter rxPresenter){
        this.rxPresenter = rxPresenter;
    }

    @Override
    public void onSubscribe(Disposable d) {
        rxPresenter.addSubscribe(d);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {

    }
}
