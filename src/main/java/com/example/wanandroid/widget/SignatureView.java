package com.example.wanandroid.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class SignatureView extends View {

    private Path path;
    private Paint paint;

    private Canvas cacheCanvas;
    private Bitmap cacheBitmap;
    private int mBackColor;

    private float xWidth, yHeight;

    public SignatureView(Context context) {
        super(context);
        init();
    }

    public SignatureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SignatureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SignatureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        path = new Path();

        paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(cacheBitmap, 0, 0, paint);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(onSwitchHintListener != null){
                    onSwitchHintListener.hideHint();
                }

                xWidth = x;
                yHeight = y;

                path.reset();
                //设置path起点
                path.moveTo(xWidth, yHeight);

                break;
            case MotionEvent.ACTION_MOVE:

                //绘制贝塞尔曲线
                //防锯齿，终点为连线中点
                float cX = (x + xWidth) / 2;
                float cY = (y + yHeight) / 2;
                // 二次贝塞尔，实现平滑曲线；前2为操作点，后2为终点
                path.quadTo(xWidth, yHeight, cX, cY);

                xWidth = x;
                yHeight = y;

                break;
            case MotionEvent.ACTION_UP:

                cacheCanvas.drawPath(path, paint);
                path.reset();

                break;
        }

        invalidate();
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cacheBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas(cacheBitmap);
        ColorDrawable colorDrawable = (ColorDrawable) getBackground();
        mBackColor = colorDrawable.getColor();
        cacheCanvas.drawColor(mBackColor);
    }

    public void clear() {
        if (onSwitchHintListener != null){
            onSwitchHintListener.showHint();
        }

        if (cacheCanvas != null) {
            cacheCanvas.drawColor(mBackColor, PorterDuff.Mode.CLEAR);
            //再设置一遍背景，以防清除后背景为黑色
            cacheCanvas.drawColor(mBackColor);
            invalidate();
        }
    }

    //            ImageUtils.saveBmp2Gallery(mContext,signatureView.save(),"signature_"+System.currentTimeMillis());
    public Bitmap save() {
        return cacheBitmap;
    }

    private OnSwitchHintListener onSwitchHintListener;

    public void setOnSwitchHintListener(OnSwitchHintListener onSwitchHintListener){
        this.onSwitchHintListener = onSwitchHintListener;
    }

    public interface OnSwitchHintListener {
        void hideHint();
        void showHint();
    }

}
