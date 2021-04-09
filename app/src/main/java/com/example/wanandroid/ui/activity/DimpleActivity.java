package com.example.wanandroid.ui.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;
import androidx.palette.graphics.Target;

import com.example.wanandroid.R;
import com.example.wanandroid.framework.mvp.BaseActivity;
import com.example.wanandroid.framework.mvp.RxPresenter;
import com.example.wanandroid.widget.CircleImageView;

import butterknife.BindView;

public class DimpleActivity extends BaseActivity {

    @BindView(R.id.rl)
    RelativeLayout relativeLayout;
    @BindView(R.id.iv_circle)
    CircleImageView circleImageView;

    @Override
    public int getLayout() {
        return R.layout.dimple_activity;
    }

    @Override
    public void initEventAndView() {

        initAnimate();

        Bitmap bitmap = ((BitmapDrawable)(circleImageView.getDrawable())).getBitmap();
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
                int lightVibrantColor = palette.getLightVibrantColor(Color.RED);
                int vibrantColor = palette.getVibrantColor(Color.RED);
                int darkVibrantColor = palette.getLightVibrantColor(Color.RED);
                int colors[] = {vibrantColor, lightVibrantColor , darkVibrantColor };
                GradientDrawable bg = new GradientDrawable(GradientDrawable.Orientation.BL_TR, colors);
                relativeLayout.setBackground(bg);
            }
        });


    }

    private void initAnimate() {


        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(circleImageView, View.ROTATION,0,360);
        objectAnimator1.setRepeatCount(-1);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(circleImageView,View.SCALE_X,0.5f,1,0.5f);
        objectAnimator2.setRepeatCount(-1);
        ObjectAnimator objectAnimator3 =ObjectAnimator.ofFloat(circleImageView,View.SCALE_Y,0.5f,1,0.5f);
        objectAnimator3.setRepeatCount(-1);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator1,objectAnimator2,objectAnimator3);
        animatorSet.setDuration(2000).start();
//        objectAnimator.setInterpolator(new LinearInterpolator());
//        objectAnimator.start();
    }

    @Override
    public RxPresenter setPresenter() {
        return null;
    }
}
