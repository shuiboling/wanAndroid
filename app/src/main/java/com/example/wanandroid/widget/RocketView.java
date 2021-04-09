package com.example.wanandroid.widget;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class RocketView extends View {
    private Paint paint,txtPaint;
    private Path path,arcPath;
    private int mWidth,mHeight,xWidth,yHeight,xSize,ySize,arcHeight;
    private boolean isSuccess;
    private String text = "发射";
    AnimatorSet animSet;

    public RocketView(Context context) {
        super(context);
        init();
    }

    public RocketView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public RocketView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    public RocketView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();

    }

    private void init() {
        paint = new Paint();
        txtPaint = new Paint();
        path = new Path();
        arcPath = new Path();
        animSet = new AnimatorSet();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        xSize = mWidth * 1/10;
        ySize = mHeight * 1/10;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //确定小火箭控制点的范围
        if (xWidth < xSize) {
            xWidth = xSize;
        }
        if (xWidth > mWidth * 9 / 10) {
            xWidth = mWidth * 9 / 10;
        }
        if (yHeight > mHeight * 8 / 10) {
            yHeight = mHeight * 8 / 10;
        }
        if (yHeight > mHeight * 7 / 10 && xWidth < mWidth * 4 / 10) {
            yHeight = mHeight * 7 / 10;
        }
        if (yHeight > mHeight * 7 / 10 && xWidth > mWidth * 6 / 10) {
            yHeight = mHeight * 7 / 10;
        }

        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        path.reset();
        //绘制小火箭
        path.moveTo(xWidth - xSize*1/2,yHeight - ySize*1/2);
        path.lineTo(xWidth,yHeight - ySize);
        path.lineTo(xWidth + xSize*1/2,yHeight - ySize*1/2);
        path.close();
        path.moveTo(xWidth - xSize*1/3,yHeight - ySize*1/2);
        path.lineTo(xWidth - xSize*1/3,yHeight);
        path.lineTo(xWidth + xSize*1/3,yHeight);
        path.lineTo(xWidth + xSize*1/3,yHeight - ySize*1/2);
        canvas.drawPath(path, paint);

        //绘制发射台
        paint.setStrokeWidth(10);
        arcPath.reset();
        arcPath.moveTo(mWidth * 1 / 10, mHeight * 7 / 10);
        if (yHeight > mHeight * 7 / 10 && xWidth > mWidth * 4 / 10 && xWidth < mWidth * 6 / 10) {
            arcHeight = yHeight + yHeight - mHeight * 7 / 10;
        } else {
            arcHeight = mHeight * 7 / 10;
        }
        //前2个是控制点坐标
        arcPath.quadTo(mWidth * 5 / 10, arcHeight, mWidth * 9 / 10, mHeight * 7 / 10);
        canvas.drawPath(arcPath, paint);

        //绘制成功后的文字
//        if (isSuccess && yHeight < 0) {
//            txtPaint.setTextSize(80);
//            txtPaint.setColor(Color.RED);
//            txtPaint.getTextBounds(text, 0, text.length(), mRect);
//            canvas.drawText(text, mWidth * 1 / 2 - mRect.width() / 2, mHeight * 1 / 2 + mRect.height() * 1 / 2, txtPaint);
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isSuccess = false;
                break;
            case MotionEvent.ACTION_MOVE:
                xWidth = x;
                yHeight = y;
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (yHeight > mHeight * 7 / 10 && xWidth > mWidth * 4 / 10 && xWidth < mWidth * 6 / 10) {
                    startAnim();
                }
                break;
        }
        return true;
    }

    private void startAnim() {
        //动画实现
        ValueAnimator animator = ValueAnimator.ofInt(yHeight, -ySize);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                yHeight = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animSet.setDuration(800);
        animSet.play(animator);
        animSet.start();
        isSuccess = true;
    }

}
