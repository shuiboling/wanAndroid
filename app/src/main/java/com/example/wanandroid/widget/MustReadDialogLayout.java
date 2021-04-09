package com.example.wanandroid.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class MustReadDialogLayout extends LinearLayout {

    public MustReadDialogLayout(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public MustReadDialogLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

    }

    public MustReadDialogLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }



//    @Override
//    protected void onDraw(Canvas canvas) {
//
//        Paint paint = new Paint();
//        paint.setStrokeWidth(2);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setColor(Color.BLACK);
//
//        int padding = getPaddingLeft();
//        int centerRaduis = (int)(getWidth() * 0.14);    //40
//        int centerGap = centerRaduis / 4;       //10
//        int startY = centerRaduis - centerGap;
//
//        Path path = new Path();
//        path.moveTo(0,startY);
//        path.lineTo((getWidth()/2)-centerRaduis,startY);
//        RectF oval = new RectF( (getWidth()/2)-centerRaduis,0,
//                (getWidth()/2)+centerRaduis, startY*2);
//        path.addArc(oval,-180,180);
//        path.lineTo(getWidth(),startY);
//        path.lineTo(getWidth(),getHeight());
//        path.lineTo(0,getHeight());
//        path.lineTo(0,startY);
//
//        canvas.drawPath(path,paint);
//        canvas.clipPath(path);
////        super.draw(canvas);
//        super.onDraw(canvas);
//
//
//    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);

        int padding = getPaddingLeft();
        int centerRaduis = (int)(getWidth() * 0.14);    //40
        int centerGap = centerRaduis / 4;       //10
        int startY = centerRaduis - centerGap;

        Path path = new Path();
        path.moveTo(0,startY);
        path.lineTo((getWidth()/2)-centerRaduis,startY);
        RectF oval = new RectF( (getWidth()/2)-centerRaduis,0,
                (getWidth()/2)+centerRaduis, startY*2);
        path.addArc(oval,-180,180);
        path.lineTo(getWidth(),startY);
        path.lineTo(getWidth(),getHeight());
        path.lineTo(0,getHeight());
        path.lineTo(0,startY);

        canvas.drawPath(path,paint);
        canvas.clipPath(path);

        super.draw(canvas);
    }
}
