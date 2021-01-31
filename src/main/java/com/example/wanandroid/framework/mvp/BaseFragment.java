package com.example.wanandroid.framework.mvp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<T extends RxPresenter> extends Fragment {
    protected T mPresenter;
    protected Context mContext;
    private Unbinder unbinder;
    protected View mView;
    protected Activity mActivity;

    //与activity完成绑定时回调该方法
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = (Activity)context;
    }

    //创建视图
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayout(),null);
        return mView;
    }

    //在onCreateView返回之后立刻调用
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = setPresenter();
        if(mPresenter != null) {
           mPresenter.attachView(view);
        }
        unbinder = ButterKnife.bind(this,view);
        initEventAndView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter != null){
            mPresenter.detachView();
        }
    }

    public abstract int getLayout();
    public abstract void initEventAndView();
    public abstract T setPresenter();
}
