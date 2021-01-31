package com.example.wanandroid.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.example.wanandroid.R;
import com.example.wanandroid.framework.mvp.BaseActivity;
import com.example.wanandroid.framework.mvp.RxPresenter;
import com.example.wanandroid.util.ImageUtils;
import com.example.wanandroid.widget.SignatureView;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import butterknife.BindView;
import butterknife.OnClick;

public class SignActivity extends BaseActivity {
    @BindView(R.id.sign)
    SignatureView signatureView;
    @BindView(R.id.tv_sign_hint)
    TextView tvHint;

    @OnClick({R.id.save,R.id.clear})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.save:
                ImageUtils.saveBmp2Gallery(mContext,signatureView.save(),"sign_"+System.currentTimeMillis());
                break;
            case R.id.clear:
                signatureView.clear();
        }
    }

    @Override
    public int getLayout() {
        return R.layout.sign_activity;
    }

    @Override
    public void initEventAndView() {
        signatureView.setOnSwitchHintListener(new SignatureView.OnSwitchHintListener() {
            @Override
            public void hideHint() {
                tvHint.setVisibility(View.GONE);
            }

            @Override
            public void showHint() {
                tvHint.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public RxPresenter setPresenter() {
        return null;
    }
}
