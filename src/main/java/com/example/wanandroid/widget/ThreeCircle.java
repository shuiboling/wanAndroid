package com.example.wanandroid.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.wanandroid.R;

public class ThreeCircle extends View {
    private int first_color = Color.BLUE;
    private int second_color = Color.BLACK;
    private int third_color = Color.GREEN;
    private int defaultLength = 200;
    private int radius;
    private int widthSize;
    private int heightSize;
    private int widthMode;
    private int heightMode;
    private Paint paint;


    public ThreeCircle(Context context) {
        super(context);
        paint = new Paint();
    }

    public ThreeCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ThreeCircle);
        first_color = typedArray.getColor(R.styleable.ThreeCircle_first_circle_color,first_color);
        second_color = typedArray.getColor(R.styleable.ThreeCircle_second_circle_color,second_color);
        third_color = typedArray.getColor(R.styleable.ThreeCircle_third_circle_color,third_color);
        typedArray.recycle();
    }

    public ThreeCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ThreeCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        widthMode = MeasureSpec.getMode(widthMeasureSpec);

        heightSize = MeasureSpec.getSize(heightMeasureSpec);
        heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //处理wrap_content
        if(widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){
            widthSize = heightSize =  defaultLength;
            setMeasuredDimension(defaultLength,defaultLength);
        }else if(widthMode == MeasureSpec.AT_MOST){
            widthSize = defaultLength;
            setMeasuredDimension(defaultLength,heightMeasureSpec);
        }else if(heightMode == MeasureSpec.AT_MOST){
            heightSize = defaultLength;
            setMeasuredDimension(widthMeasureSpec,defaultLength);
        }

        radius = Math.min(widthSize,heightSize) / 2;
    }

    //由于没有子View需要布局，所以，不用重写该方法,仅ViewGroup需要
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int x;
        int y;

        if(widthSize > radius*2){
            x = radius + (widthSize - heightSize)/2;
            y = radius;
        }else {
            x = radius;
            y = radius + (heightSize - widthSize)/2;
        }
        paint.setColor(first_color);
        canvas.drawCircle(x,y,radius,paint);
        paint.setColor(second_color);
        canvas.drawCircle(x,y,radius/2,paint);
        paint.setColor(third_color);
        canvas.drawCircle(x,y,radius/4,paint);
    }
}
