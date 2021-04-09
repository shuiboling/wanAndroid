package com.example.wanandroid.framework.retrofit;

public interface OnNetworkListener<T> {

    public void onSuccess(T data);

    public void onFail();
}
