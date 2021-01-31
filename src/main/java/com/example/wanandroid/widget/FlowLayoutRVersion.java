package com.example.wanandroid.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.wanandroid.R;

public class FlowLayoutRVersion extends RelativeLayout {
    private int screenWidth;
    private int marginVertical;
    private int marginHorizontal;

    public FlowLayoutRVersion(Context context) {
        super(context);
    }

    public FlowLayoutRVersion(Context context, AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowLayoutRVersion);
        marginHorizontal = typedArray.getDimensionPixelSize(R.styleable.FlowLayoutRVersion_margin_horizontal,10);
        marginVertical = typedArray.getDimensionPixelSize(R.styleable.FlowLayoutRVersion_margin_vertical,10);
        typedArray.recycle();
    }

    public FlowLayoutRVersion(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FlowLayoutRVersion(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childWidth = 0 ;
        int lineNum = 1;
        int childPaddingHorizontal = 0;
        int childPaddingVertical = 0;
        int childWidthTotal = 0;

        for(int i = 0;i<getChildCount();i++){
            View childView = getChildAt(i);
            childPaddingHorizontal = childView.getPaddingLeft() + childView.getPaddingRight();
            childPaddingVertical = childView.getPaddingTop() + childView.getPaddingBottom();
            childWidth = childView.getMeasuredWidth() + childPaddingHorizontal + marginHorizontal;
            childWidthTotal += childWidth;

            if(childWidthTotal > screenWidth){ //换行
                childWidthTotal = childWidth;
                if(i != 0 ) {
                    lineNum++;
                }

            }
            //如果子view宽大于屏幕宽度
            if(childWidth >= screenWidth){
                ViewGroup.LayoutParams layoutParams = childView.getLayoutParams();
                layoutParams.width = screenWidth - marginHorizontal*2;
                childView.setLayoutParams(layoutParams);

                childView.layout(marginHorizontal ,
                        (lineNum-1)*(childPaddingVertical + childView.getMeasuredHeight()
                                + marginVertical) + marginVertical + childView.getPaddingTop(),
                        screenWidth - marginHorizontal,
                        (lineNum)*(childPaddingVertical + childView.getMeasuredHeight() + marginVertical));
            }else {

                childView.layout(childWidthTotal - childWidth + marginHorizontal ,
                        (lineNum-1)*(childPaddingVertical + childView.getMeasuredHeight()
                                + marginVertical) + marginVertical + childView.getPaddingTop(),
                        childWidthTotal,
                        (lineNum)*(childPaddingVertical + childView.getMeasuredHeight() + marginVertical));
            }
        }

    }
}
