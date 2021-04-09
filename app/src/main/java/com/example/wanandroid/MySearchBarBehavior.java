package com.example.wanandroid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class MySearchBarBehavior extends CoordinatorLayout.Behavior<RelativeLayout> {

    /**
     * 处于中心时候原始X轴
     */
    private int mOriginalHeaderX = 0;
    /**
     * 处于中心时候原始Y轴
     */
    private int mOriginalHeaderY = 0;
    private int mOriginalWidth = 0;
    private int endY = 26;
    private int endX = 150;
    private int endWidth = 500;


    public MySearchBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, RelativeLayout child, View dependency) {
        return dependency instanceof RelativeLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, RelativeLayout child, View dependency) {
        //效果怎么样，全看数学好不好，，，

        // 计算X轴坐标
        if (mOriginalHeaderX == 0) {
            this.mOriginalHeaderX = dependency.getWidth() / 2 - child.getWidth() / 2;
        }
        // 计算Y轴坐标
        if (mOriginalHeaderY == 0) {
            mOriginalHeaderY = dependency.getHeight() - child.getHeight();
        }

        if(mOriginalWidth == 0){
            mOriginalWidth = child.getWidth();
            endWidth = mOriginalWidth * 1/3;
        }
        //X轴百分比
        float mPercentX = dependency.getY() / mOriginalHeaderY;
        if (mPercentX >= 1) {
            mPercentX = 1;
        }
        //Y轴百分比
        float mPercentY = (dependency.getY()) / (mOriginalHeaderY - endY);
        if (mPercentY >= 1) {
            mPercentY = 1;
        }

        float x = mOriginalHeaderX - mOriginalHeaderX * mPercentX;
        if (x <= endX) {
            x = endX;
        }

        int width = (int) (mOriginalWidth - mOriginalWidth * mPercentX);
        if(width <= endWidth){
            width = endWidth;
        }
        ViewGroup.LayoutParams params =  child.getLayoutParams();
        params.width = width;
        child.setLayoutParams(params);

//        child.setX(x);
        child.setY(mOriginalHeaderY - (mOriginalHeaderY - endY) * mPercentX);
        return true;
    }
}
