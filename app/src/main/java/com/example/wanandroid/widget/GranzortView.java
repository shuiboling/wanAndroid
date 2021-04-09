package com.example.wanandroid.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.wanandroid.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GranzortView extends View {

    private Paint paint, holoPaint;

    public GranzortView(Context context) {
        super(context);
        init();
    }

    public GranzortView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public GranzortView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private Bitmap ufo;
    private void init() {
        initPaint();
        initAnimate();
//       initPath();
        initHandler();

        ufo = BitmapFactory.decodeResource(getResources(),R.drawable.ufo);
    }

    int radius = 290, gap = 20;
    private List<Holo> holos = new ArrayList<>();

    class Holo {
        private int alpha;
        private float radius;

        public Holo(int alpha, float radius) {
            this.alpha = alpha;
            this.radius = radius;
        }
    }

    private Random random = new Random();

    private void initPath() {
        RectF innerRect = new RectF(centerX - 220, centerY - 220, centerX + 220, centerY + 220);
        RectF outerRect = new RectF(centerX - 280, centerY - 280, centerX + 280, centerY + 280);

        innerCircle = new Path();
        outerCircle = new Path();

        innerCircle.addArc(innerRect, 150, -360F);     // 不能取360f，否则可能造成测量到的值不准确
        outerCircle.addArc(outerRect, 60, -360F);

        float[] pos = new float[2];
        pathMeasure = new PathMeasure(innerCircle, false);
        trangle1 = new Path();
        trangle2 = new Path();

        pathMeasure.getPosTan(0, pos, null);
        trangle1.moveTo(pos[0], pos[1]);
        pathMeasure.getPosTan((1 / 3f) * pathMeasure.getLength(), pos, null);
        trangle1.lineTo(pos[0], pos[1]);
        pathMeasure.getPosTan((2 / 3f) * pathMeasure.getLength(), pos, null);
        trangle1.lineTo(pos[0], pos[1]);
        trangle1.close();

        pathMeasure.getPosTan((1 / 2f) * pathMeasure.getLength(), pos, null);
        trangle2.moveTo(pos[0], pos[1]);
        pathMeasure.getPosTan((5 / 6f) * pathMeasure.getLength(), pos, null);
        trangle2.lineTo(pos[0], pos[1]);
        pathMeasure.getPosTan((1 / 6f) * pathMeasure.getLength(), pos, null);
        trangle2.lineTo(pos[0], pos[1]);
        trangle2.close();

        drawPath = new Path();

        initRingPath();

    }

    public void initRingPath(){
        for (int i = 0; i < holoCount; i++) {
            float radius = random.nextInt(290) + 50;
            Holo holo = new Holo(100, radius);
            holos.add(holo);
        }
    }

    public void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.BEVEL);
        paint.setShadowLayer(15, 0, 0, Color.WHITE);//白色光影效果

        holoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        holoPaint.setColor(Color.WHITE);
        holoPaint.setStyle(Paint.Style.STROKE);
        holoPaint.setStrokeWidth(5);
        holoPaint.setStrokeCap(Paint.Cap.ROUND);
        holoPaint.setStrokeJoin(Paint.Join.BEVEL);
        holoPaint.setShadowLayer(10, 0, 0, Color.WHITE);//白色光影效果
    }

    private ValueAnimator valueAnimator;
    private float distance;

    private void initAnimate() {
        valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                distance = (float) animation.getAnimatedValue();
                Log.d("zyy", distance + "");
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mHandler.sendEmptyMessage(0);
            }
        });
        valueAnimator.start();
    }

    private float centerX, centerY;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        centerX = w / 2;
        centerY = h / 2;

        maxOffset = w;

        initPath();
    }

    private Path innerCircle, outerCircle, trangle1, trangle2, drawPath;
    private PathMeasure pathMeasure;

    private static final int DRAW_CIRCLE = 0;
    private static final int DRAW_TRANGLE = 1;
    private static final int DRAW_TOTAL = 2;
    private static final int DRAW_RING = 3;
    private static final int DRAW_UFO = 4;
    private static final int DRAW_END = 5;

    private int status = DRAW_CIRCLE;

    private int holoCount = 10;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (status) {
            case DRAW_CIRCLE:
                drawPath.reset();
                pathMeasure.setPath(innerCircle, false);
                pathMeasure.getSegment(0, distance * pathMeasure.getLength(), drawPath, true);
                canvas.drawPath(drawPath, paint);

                drawPath.reset();
                pathMeasure.setPath(outerCircle, false);
                pathMeasure.getSegment(0, distance * pathMeasure.getLength(), drawPath, true);
                canvas.drawPath(drawPath, paint);

                break;
            case DRAW_TRANGLE:
                canvas.drawPath(innerCircle, paint);
                canvas.drawPath(outerCircle, paint);

                drawPath.reset();
                pathMeasure.setPath(trangle1, false);
                float stop, start;
                stop = distance * pathMeasure.getLength();
                start = stop - (0.5f - Math.abs(0.5f - distance)) * 200;
                pathMeasure.getSegment(start, stop, drawPath, true);
                canvas.drawPath(drawPath, paint);

                drawPath.reset();
                pathMeasure.setPath(trangle2, false);
                pathMeasure.getSegment(start, stop, drawPath, true);
                canvas.drawPath(drawPath, paint);

                break;

            case DRAW_TOTAL:
                canvas.drawPath(innerCircle, paint);
                canvas.drawPath(outerCircle, paint);
//
                drawPath.reset();
                pathMeasure.setPath(trangle1, false);
                pathMeasure.getSegment(0, distance * pathMeasure.getLength(), drawPath, true);
                canvas.drawPath(drawPath, paint);

                drawPath.reset();
                pathMeasure.setPath(trangle2, false);
                pathMeasure.getSegment(0, distance * pathMeasure.getLength(), drawPath, true);
                canvas.drawPath(drawPath, paint);

                break;

            case DRAW_RING:
                canvas.drawPath(innerCircle, paint);
                canvas.drawPath(outerCircle, paint);
                canvas.drawPath(trangle1, paint);
                canvas.drawPath(trangle2, paint);

                for (int i = 0; i < holoCount; i++) {

                    Path holoPath = new Path();
                    Holo holo = holos.get(i);
                    RectF holoRecF = new RectF(centerX - holo.radius, centerY - holo.radius, centerX + holo.radius, centerY + holo.radius);
                    holoPaint.setAlpha(holo.alpha);
                    holoPath.addArc(holoRecF, 0, 360f);
                    canvas.drawPath(holoPath, holoPaint);

                    float top = -50 + ufoPercent*(centerY - 100);

                    float bottom = top + 200;
                    RectF rectF = new RectF(centerX-100,top,centerX+100,bottom);
                    canvas.drawBitmap(ufo, null,rectF,paint);
                }

                break;

            case DRAW_UFO:
                canvas.drawPath(innerCircle, paint);
                canvas.drawPath(outerCircle, paint);
                canvas.drawPath(trangle1, paint);
                canvas.drawPath(trangle2, paint);

                float top = -50 + ufoPercent*(centerY - 100);

                float bottom = top + 200;
                RectF rectF = new RectF(centerX-100,top,centerX+100,bottom);
                canvas.drawBitmap(ufo, null,rectF,paint);

                break;

//            case DRAW_END:
//
//                canvas.drawPath(innerCircle, paint);
//                canvas.drawPath(outerCircle, paint);
//                canvas.drawPath(trangle1, paint);
//                canvas.drawPath(trangle2, paint);
//
////                for (int i = 0; i < holoCount; i++) {
////
////                    Path holoPath = new Path();
////                    Holo holo = holos.get(i);
////                    RectF holoRecF = new RectF(centerX - holo.radius, centerY - holo.radius, centerX + holo.radius, centerY + holo.radius);
////                    holoPaint.setAlpha(holo.alpha);
////                    holoPath.addArc(holoRecF, 0, 360f);
////                    canvas.drawPath(holoPath, holoPaint);
////                }
//
//                float t = (centerY-100)-percent*(centerY - 50);
//
//                float b = t + 200;
//                RectF rect = new RectF(centerX-100,t,centerX+100,b);
//                canvas.drawBitmap(ufo, null,rect,paint);
//                break;
        }
    }

    private Handler mHandler;

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (status) {
                    case DRAW_CIRCLE:
                        status = DRAW_TRANGLE;
                        valueAnimator.start();
                        break;
                    case DRAW_TRANGLE:
                        status = DRAW_TOTAL;
                        valueAnimator.start();
                        break;

                    case DRAW_TOTAL:
                        status = DRAW_RING;
                        initRingAnimate();
//                        break;
//                    case DRAW_RING:
////                        paint.setAlpha(150);
//                        status = DRAW_UFO;
                        initUFOAnimate();
                        break;

                    case DRAW_UFO:
                        status = DRAW_END;
//                        initRingPath();
                        ringAnimator.start();
                        break;
                }
            }
        };

    }

    private float ufoPercent;
    private void initUFOAnimate() {
        ValueAnimator ufoAnimate = ValueAnimator.ofFloat(0f,1f);
        ufoAnimate.setDuration(5000);
        ufoAnimate.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ufoPercent = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        ufoAnimate.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mHandler.sendEmptyMessage(0);

                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ufo, String.valueOf(View.ROTATION),0,360);
                objectAnimator.setRepeatCount(-1);
                objectAnimator.setInterpolator(new LinearInterpolator());
                objectAnimator.start();
            }
        });
        ufoAnimate.start();
    }

    private float percent;
    private ValueAnimator ringAnimator;
    private void initRingAnimate() {
        ringAnimator = ValueAnimator.ofFloat(0f, 1f);
        ringAnimator.setDuration(3000);
        ringAnimator.setRepeatCount(-1);
        ringAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                percent = (float) animation.getAnimatedValue();
                updateCircle();
                invalidate();
            }
        });

        ringAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        ringAnimator.start();
    }

    private float maxOffset;

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateCircle() {
        holos.forEach(holo -> {
            if (holo.radius > maxOffset) {
                holo.radius = 290f;
            } else {
                holo.radius += gap;
            }
//            holo.radius += (maxOffset - 290) * percent;

            holo.alpha = (int) ((1f - holo.radius / maxOffset) * 225f);

        });
    }

}
