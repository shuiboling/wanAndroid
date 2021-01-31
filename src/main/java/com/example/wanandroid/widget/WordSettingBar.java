package com.example.wanandroid.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.wanandroid.R;

import java.util.ArrayList;
import java.util.List;

public class WordSettingBar extends View {

    private float smallCircleRadius = 2*getContext().getResources().getDisplayMetrics().density;    //小圆点半径
    private float bigCircleRadius = 5*getContext().getResources().getDisplayMetrics().density;      //大圆半径
    private int lineColor = Color.GRAY;      //线颜色
    private int bigCircleColor = Color.RED; //大圆颜色
    private int cellNumber = 1; //左边格数（为有中心点，左右格数需相等）
    private float marginTop = 15*getContext().getResources().getDisplayMetrics().density;   //字和线间的距离

    private float barWidth;
    private float barHeight;
    private float cellWidth;
    private float textSize;
    private Paint linePaint;
    private Paint circlePaint;

    private float paddingHorizontal;
    private float paddingVertical;

    private float currentX = 0;
    private List<Float> points = new ArrayList<>(); //存放小圆点x轴坐标

    public WordSettingBar(Context context) {
        super(context);
    }

    public WordSettingBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.getResources().obtainAttributes(attrs, R.styleable.WordSettingBar);
        smallCircleRadius = typedArray.getDimension(R.styleable.WordSettingBar_smallCircleRadius,smallCircleRadius);
        bigCircleRadius = typedArray.getDimension(R.styleable.WordSettingBar_bigCircleRadius,bigCircleRadius);
        lineColor = typedArray.getColor(R.styleable.WordSettingBar_lineColor,lineColor);
        bigCircleColor = typedArray.getColor(R.styleable.WordSettingBar_bigCircleColor,bigCircleColor);
        cellNumber = typedArray.getColor(R.styleable.WordSettingBar_cellNumber,cellNumber);
        typedArray.recycle();

        textSize = bigCircleRadius * 2;
    }

    public WordSettingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WordSettingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        barWidth = getMeasuredWidth();
        barHeight = getMeasuredHeight();

        paddingHorizontal = (getPaddingLeft() + getPaddingRight())/2;
        paddingVertical = (getPaddingTop() + getPaddingBottom())/2;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        cellWidth = (barWidth - bigCircleRadius*2-paddingHorizontal*2)/(cellNumber*2);
        float nowX = bigCircleRadius+paddingHorizontal;
        for(int i = 0;i<=cellNumber*2;i++){
            if(i == 0){
                points.add(nowX);
            }else {
                nowX += cellWidth;
                points.add(nowX);
            }
        }

        if(currentX == 0){
            currentX = points.get(0);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //drawLine();
        if(linePaint == null){
            linePaint = new Paint();
        }
        linePaint.setColor(lineColor);
        linePaint.setTextSize(textSize);

        if(circlePaint == null){
            circlePaint = new Paint();
        }

        float y = bigCircleRadius+textSize+marginTop;
        float delt = (barHeight - y)/2;
        y = y + delt;

        canvas.drawText("小",paddingHorizontal,textSize+delt,linePaint);
        canvas.drawText("中",paddingHorizontal+cellWidth*cellNumber
                ,textSize+delt,linePaint);
        canvas.drawText("大",paddingHorizontal+cellWidth*cellNumber*2,textSize+delt,linePaint);

        canvas.drawLine(points.get(0),y,points.get(cellNumber*2),y,linePaint);

        for(float point:points) {
            canvas.drawCircle(point, y, smallCircleRadius, linePaint);
        }

       // drawBigCircle();
        circlePaint.setColor(bigCircleColor);
        canvas.drawCircle(currentX,y,bigCircleRadius,circlePaint);
    }

    private boolean isBigCircle;
    private boolean isSmallCircle;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float down_x = event.getX();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isBigCircle = isDownBigCircle(down_x);
                isSmallCircle = isDownSmallCircle(down_x);
                break;
            case MotionEvent.ACTION_MOVE:
                if(isBigCircle && down_x >= points.get(0) && down_x <= points.get(points.size()-1)){
                    currentX = down_x;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                int position = getNearestPoint(down_x);
                if(isBigCircle || isSmallCircle){
                    currentX = points.get(position);

                    onWordSizeChangeListener.onSizeChanged(position);
                }
                invalidate();
                break;
        }

        return true;
    }

    private boolean isDownSmallCircle(float x) {
        for (int i = 0; i < points.size(); i++) {
            float point = points.get(i);
            if (Math.abs(point - x) < smallCircleRadius + 20) {
                return true;
            }
        }
        return false;
    }

    private boolean isDownBigCircle(float x){
        return Math.abs(currentX - x) < bigCircleRadius + 20;
    }
    private int getNearestPoint(float x) {
        if(x < points.get(0)) {
            return 0;
        } else if(x > points.get(points.size()-1)){
            return points.size()-1;
        }
        for (int i = 0; i < points.size(); i++) {
            float point = points.get(i);
            if (Math.abs(point - x) < cellWidth/2) {
                return i;
            }


        }
        return 0;
    }

    public void initBigCircle(int position){
        currentX = points.get(position);
        invalidate();
    }

    private OnWordSizeChangeListener onWordSizeChangeListener;

    public interface OnWordSizeChangeListener{
        public void onSizeChanged(int position);
    }

    public void setOnWordSizeChangeListener(OnWordSizeChangeListener onWordSizeChangeListener){
        this.onWordSizeChangeListener = onWordSizeChangeListener;
    }
}
