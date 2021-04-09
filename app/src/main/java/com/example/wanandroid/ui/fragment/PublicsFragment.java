package com.example.wanandroid.ui.fragment;

import androidx.fragment.app.Fragment;

import com.example.wanandroid.R;
import com.example.wanandroid.framework.mvp.BaseFragment;
import com.example.wanandroid.framework.mvp.RxPresenter;

public class PublicsFragment extends BaseFragment {
    @Override
    public int getLayout() {
        return R.layout.publics_layout;
    }

    @Override
    public void initEventAndView() {

    }

    @Override
    public RxPresenter setPresenter() {
        return null;
    }
}
