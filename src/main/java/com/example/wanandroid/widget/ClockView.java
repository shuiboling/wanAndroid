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

public class ClockView extends View {
    private int clockRaduis = 100;
    private float textSize = 12 * getContext().getResources().getDisplayMetrics().density;
    private Paint paint;
    private Paint paintNum;
    private Paint paintLine;
    private float nowDegree = 0;
    private int nowX;
    private int secondDegree = 0;

    public ClockView(Context context) {
        super(context);
        paint = new Paint();
        paintNum = new Paint();

    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paintNum = new Paint();
        paintLine = new Paint();

        TypedArray typedArray = context.getResources().obtainAttributes(attrs, R.styleable.RoundView);
        clockRaduis = (int)typedArray.getDimension(R.styleable.RoundView_raduis,clockRaduis);
        nowX = clockRaduis;
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        float r = clockRaduis -5;
        canvas.drawCircle(clockRaduis,clockRaduis,clockRaduis,paint);

        paintNum.setTextSize(textSize);
        paintNum.setColor(Color.BLACK);
        paintNum.setTextAlign(Paint.Align.CENTER);

        double y = 0;
        double x = 0;
        double reg = 0;
        float distance = r-textSize;
        for(int i = 0;i < 12;i++){
            reg = nowDegree/180*Math.PI;
            x = clockRaduis + Math.sin(reg)*distance;
            y = clockRaduis - Math.cos(reg)*distance +6;

//            a = (distance * Math.sin(i.toDouble() * 30.0 * Math.PI / 180) + mX).toFloat()
//            b = (mY - distance * Math.cos(i.toDouble() * 30.0 * Math.PI / 180)).toFloat()
            if(i == 0){
                canvas.drawText("12",(float) x,(float) y,paintNum);
            }else {
                canvas.drawText(i+"", (float) x, (float) y, paintNum);
            }
            nowDegree += 30;
        }
        paintLine.setColor(Color.RED);
        canvas.rotate(secondDegree,clockRaduis,clockRaduis);
        secondDegree += 6;
        canvas.drawLine(clockRaduis,clockRaduis,clockRaduis,clockRaduis-textSize*2,paintLine);
        postInvalidateDelayed(1000);

    }
}
