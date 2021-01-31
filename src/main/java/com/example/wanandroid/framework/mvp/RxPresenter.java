package com.example.wanandroid.framework.mvp;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class RxPresenter<T> implements BasePresenter<T> {

    private CompositeDisposable compositeDisposable;
    public T mView;

    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    public void addSubscribe(Disposable disposable){
        if(compositeDisposable == null){
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    public void clearSubscribe(){
        if(compositeDisposable != null)
            compositeDisposable.clear();
    }

    @Override
    public void detachView() {
        mView = null;
        clearSubscribe();
    }

}
