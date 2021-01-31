package com.example.wanandroid.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.wanandroid.R;

import java.util.Random;

public class NumberRainItem extends View {
    private int nowHeight = 0;
    public float textSize = 40*getContext().getResources().getDisplayMetrics().density;
    public long startOffset = 0;
    private Paint paint;
    public int highLightColor = Color.YELLOW;
    public int highLightNumIndex = 0;
    public int normalColor = Color.GREEN;
    public int charType = 0;

    public NumberRainItem(Context context) {
        super(context);
    }

    public NumberRainItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumberRainItem);
        normalColor = typedArray.getColor(R.styleable.NumberRainItem_normal_color, normalColor);
        highLightColor = typedArray.getColor(R.styleable.NumberRainItem_height_light_color, highLightColor);
//        startOffset = typedArray.getInt(R.styleable.NumberRainItem_startOffset, 0);
        textSize = typedArray.getDimension(R.styleable.NumberRainItem_text_size, textSize);
        float i = typedArray.getDimension(R.styleable.NumberRainItem_type,0);

        typedArray.recycle();
    }

    public NumberRainItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NumberRainItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setColor(normalColor);
        paint.setTextSize(textSize);
        if(isShowAll()){
            drawAll(canvas);
        }else {
            drawPart(canvas);
        }
    }

    private void drawPart(Canvas canvas) {
        int count = (int)(nowHeight/textSize);
        nowHeight += textSize;
        drawNumbers(canvas,count);
    }

    private void drawAll(Canvas canvas) {
        int count = (int)(nowHeight/textSize);
        drawNumbers(canvas,count);
    }

    private void drawNumbers(Canvas canvas,int count) {
       if(count == 0){
           postInvalidateDelayed(startOffset);
       }else {
           int offset = 0;
           for(int i=0;i<count;i++){

               String text = "";
               if(charType == 0){
                   text = (int)(Math.random()*9) + "";
               }else if(charType == 1){
                   text = getRandomChar() + "";
               }else if(charType == 2){
                   text = (new Random(47).nextInt()+'a')+"";
               }else {
                   text = "烫";
               }

               if(highLightNumIndex == i){
                   paint.setColor(highLightColor);
               }else {
                   paint.setColor(normalColor);
               }
               canvas.drawText(text,0,offset+textSize,paint);
               offset += textSize;
           }
           if(!isShowAll()){
               highLightNumIndex++;
           }else {
               highLightNumIndex = (++highLightNumIndex) % count;
               highLightNumIndex = (int)(Math.random() * count);
           }
           postInvalidateDelayed(100);
       }

    }

    public boolean isShowAll(){
        //是否大于view的高度
        return nowHeight >= getHeight();
    }

    public static char getRandomChar() {
        return (char) (0x4e00 + (int) (Math.random() * (0x9fa5 - 0x4e00 + 1)));
    }
}
