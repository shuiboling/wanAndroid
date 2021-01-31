package com.example.wanandroid.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.example.wanandroid.R;
import com.example.wanandroid.framework.mvp.BaseActivity;
import com.example.wanandroid.framework.mvp.RxPresenter;
import com.example.wanandroid.util.SpUtils;
import com.example.wanandroid.widget.WordSettingBar;

import butterknife.BindView;
import butterknife.OnClick;

public class SetWordSizeActivity extends BaseActivity {

    @BindView(R.id.wb)
    WordSettingBar bar;
    @BindView(R.id.tv)
    TextView textView;

    @OnClick(R.id.btn)
    public void click(){
        Intent intent = new Intent(SetWordSizeActivity.this,HomeActivity.class);
        intent.putExtra("state","2");
        startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.set_word_size_activity;
    }

    @Override
    public void initEventAndView() {

        int position = SpUtils.getInstance().getInt("word_size_bar_position",0);
        textView.setTextSize(getTextSize(position));

        bar.post(new Runnable() {
            @Override
            public void run() {
                bar.initBigCircle(position);
            }
        });

        bar.setOnWordSizeChangeListener(new WordSettingBar.OnWordSizeChangeListener() {
            @Override
            public void onSizeChanged(int position) {

                //改变当前页面大小
                textView.setTextSize(getTextSize(position));
                SpUtils.getInstance().setInt("word_size_bar_position",position);
                Log.d("zyy",position+"");
            }
        });
    }

    @Override
    public RxPresenter setPresenter() {
        return null;
    }

    public float getTextSize(int position){
        int dimension = getResources().getDimensionPixelSize(R.dimen.sp_14);
        //根据position 获取字体倍数
        float fontSizeScale = (float) (0.875 + 0.125 * position);
        //放大后的sp单位
        return fontSizeScale * (int) px2sp(dimension);
    }

    public float px2sp(float pxVal)
    {
        return (pxVal / mContext.getResources().getDisplayMetrics().scaledDensity);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
