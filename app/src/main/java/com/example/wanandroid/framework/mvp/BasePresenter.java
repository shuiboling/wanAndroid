package com.example.wanandroid.framework.mvp;

public interface BasePresenter<T> {

    public void attachView(T view);

    public void detachView();
}
