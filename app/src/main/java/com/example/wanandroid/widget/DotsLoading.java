package com.example.wanandroid.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.example.wanandroid.R;

public class DotsLoading extends View {
    private Paint paint;
    private ObjectAnimator objectAnimator;
    private int count = 3;
    private int[] colors = new int[count];
    private int radius = 25;
    private int[] index = new int[]{0,1,2};
    private int padding = 15;

    public DotsLoading(Context context) {
        super(context);
    }

    public DotsLoading(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        initCircle();
        initAnimate();

        TypedArray typedArray = context.getResources().obtainAttributes(attrs,R.styleable.DotsLoading);
        radius = typedArray.getDimensionPixelSize(R.styleable.DotsLoading_dots_radius,radius);
        padding = typedArray.getDimensionPixelSize(R.styleable.DotsLoading_dots_padding,padding);
        count = typedArray.getInteger(R.styleable.DotsLoading_dots_count,count);

        typedArray.recycle();
    }

    private void initAnimate() {
        objectAnimator = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f);//添加旋转动画，旋转中心默认为控件中点
        objectAnimator.setDuration(3000);//设置动画时间
        objectAnimator.setInterpolator(new LinearInterpolator());//动画时间线性渐变
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.setRepeatMode(ObjectAnimator.RESTART);
    }

    public void initCircle() {
        colors[0] = getContext().getColor(R.color.color_pink_lowst);
        colors[1] = getContext().getColor(R.color.color_81D81BA9);
        colors[2] = getContext().getColor(R.color.color_pink_dark_1);
    }

    public DotsLoading(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DotsLoading(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

//        setPadding(2,2,2,2);
        if(widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){
            widthSize = getPaddingLeft()+getPaddingRight()+radius*2*count+padding*(count-1);
            heightSize = getPaddingTop()+getPaddingBottom()+radius*2;
            setMeasuredDimension(widthSize,heightSize);
        }else if(widthMode == MeasureSpec.AT_MOST){
            widthSize = getPaddingLeft()+getPaddingRight()+radius*2*count+padding*(count-1);
            setMeasuredDimension(widthSize,heightMeasureSpec);
        }else if(heightMode == MeasureSpec.AT_MOST){
            heightSize = getPaddingTop()+getPaddingBottom()+radius*2;
            setMeasuredDimension(widthMeasureSpec,heightSize);
        }else {
            setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int x = radius+getPaddingLeft(),y = radius+getPaddingTop();
        for(int i=0;i<count;i++) {
            paint.setColor(colors[index[i]]);
            canvas.drawCircle(x, y, radius, paint);
            x += padding+radius*2;

            if(index[i]==count-1){
                index[i] = 0;
            }else {
                index[i]++;
            }
        }

        postInvalidateDelayed(200);

    }
}
