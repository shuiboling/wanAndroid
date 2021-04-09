package com.example.wanandroid.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.wanandroid.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DimpleView extends View {

    class Particle{
        public float x;//X坐标
        public float y;//Y坐标
        public float radius;//半径
        public float speed;//速度
        public float alpha;//透明度
        public float maxOffset = 300f;
        public float angle;
        public float offset;
        public int color;

        Particle(float x,float y,float radius,float speed,float alpha,float angle,float offset,int color){
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.speed = speed;
            this.alpha = alpha;
            this.angle = angle;
            this.offset = offset;
            this.color = color;
        }
    }

    private List<Particle> particleList;
    private Paint paint;
    private int count = 800;

    public DimpleView(Context context) {
        super(context);

        particleList = new ArrayList<>();
        paint = new Paint();
    }

    public DimpleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        particleList = new ArrayList<>();
        paint = new Paint();
    }

    public DimpleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DimpleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private float centerX,centerY;
    private ValueAnimator valueAnimator;

    public void init(){
        valueAnimator = ValueAnimator.ofFloat(0f,1f);
        valueAnimator.setDuration(2000);
        valueAnimator.setRepeatCount(-1);//INFINITE
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                    updateParticle();
                    invalidate();//重绘界面

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateParticle(){
        particleList.forEach((it)->{
            if(it.offset > it.maxOffset){
                it.offset = 0;
                it.speed = new Random().nextInt(10)+5;
            }
            it.alpha= ((1f - it.offset / it.maxOffset)  * 225f);
            float rad = (float) Math.PI / 180 *it.angle; // 弧度
            it.x = centerX + (float) (Math.sin(rad)*(280f+it.offset));
            it.y = centerY + (float) (Math.cos(rad)*(280f+it.offset));
            it.offset += it.speed;
        });
    }

    private Path path = new Path();
    private Random random = new Random();
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        centerX = w/2f;
        centerY = h/2f;

        //绘制圆圈路径
        path.addCircle(centerX, centerY, 280f, Path.Direction.CCW); //ccw逆时针，cw顺时针
        //追踪路径，获取圆圈路径上的每个点
        PathMeasure pathMeasure = new PathMeasure(path,false);

        float[] pos = new float[2];
        float[] tan = new float[2];

        for(float i =0 ;i<count;i++){
            pathMeasure.getPosTan(i/count * pathMeasure.getLength(), pos, tan);
            float nextX = pos[0]+random.nextInt(6) - 3f; //X值随机偏移
            float nextY=  pos[1]+random.nextInt(6) - 3f;//Y值随机偏移
            int speed = random.nextInt(10)+5;
            int color = getColor(random.nextInt(4));
            float angle = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI); //角度
            Particle particle = new Particle(nextX,nextY,3f,speed,100,angle,0,color);
            particleList.add(particle);
        }

        init();
        valueAnimator.start();
    }

    public int getColor(int num){
        if(num == 0)
            return Color.BLUE;
        if (num == 1)
            return Color.RED;
        if (num == 2)
            return Color.GREEN;
        if(num == 3)
            return Color.YELLOW;
        return  R.color.purple;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setAntiAlias(true);   //抗锯齿

        for(int i =0 ;i<count;i++){

            Particle particle = particleList.get(i);
            paint.setAlpha((int) particle.alpha);
            paint.setColor(particle.color);
            canvas.drawCircle(particle.x, particle.y, particle.radius, paint);
        }
    }
}
