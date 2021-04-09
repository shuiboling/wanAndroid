package com.example.wanandroid.ui.activity;

import android.view.View;
import android.widget.EditText;

import com.example.wanandroid.R;
import com.example.wanandroid.framework.mvp.BaseActivity;
import com.example.wanandroid.framework.mvp.RxPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class RichTextActivity extends BaseActivity {
    @BindView(R.id.et_rich)
    EditText editText;

    @OnClick({R.id.bt_cu,R.id.bt_xie,R.id.bt_xia})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_cu:
                break;
            case R.id.bt_xie:
                break;
            case R.id.bt_xia:
                break;
        }
    }

    @Override
    public int getLayout() {
        return R.layout.rich_text_activity;
    }

    @Override
    public void initEventAndView() {

    }

    @Override
    public RxPresenter setPresenter() {
        return null;
    }
}
