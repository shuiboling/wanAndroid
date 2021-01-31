package com.example.wanandroid.framework.mvp;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity<T extends RxPresenter> extends AppCompatActivity {
    public T mPresenter;
    private Unbinder unbinder;
    public Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        setContentView(getLayout());

        Log.d("zyy","c");
        unbinder = ButterKnife.bind(this);
        mContext = this;

        mPresenter = setPresenter();
        if(mPresenter!=null){
            mPresenter.attachView(this);
        }

        initEventAndView();

    }

    protected void initTheme(){

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter != null)
            mPresenter.detachView();
        unbinder.unbind();
        Log.d("zyy","d");

    }



    public abstract int getLayout();
    public abstract void initEventAndView();
    public abstract T setPresenter();
}
