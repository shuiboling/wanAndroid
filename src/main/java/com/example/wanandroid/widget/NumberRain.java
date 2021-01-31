package com.example.wanandroid.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.wanandroid.R;

public class NumberRain extends LinearLayout {

    private int normalColor = Color.GREEN;
    private int highLightColor = Color.YELLOW;
    private int textSize = (int)(20*getContext().getResources().getDisplayMetrics().density);
    private int charType = 0;
    private int startOffset = 0;

    public NumberRain(Context context) {
        super(context);
        initNormal();

    }

    public NumberRain(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initNormal();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumberRainItem);
        normalColor = typedArray.getColor(R.styleable.NumberRainItem_normal_color, normalColor);
        highLightColor = typedArray.getColor(R.styleable.NumberRainItem_height_light_color, highLightColor);
        textSize = (int)typedArray.getDimension(R.styleable.NumberRainItem_text_size, textSize);
        charType = typedArray.getInt(R.styleable.NumberRainItem_charType,charType);
//        startOffset = typedArray.getInt(R.styleable.NumberRainItem_startOffset,startOffset);
        typedArray.recycle();

    }

    public NumberRain(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initNormal();

    }

    public NumberRain(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initNormal();

    }

    private void initNormal() {
        setOrientation(HORIZONTAL);
        setBackgroundColor(Color.BLACK);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        addRainItems();

    }

    private void addRainItems()
    {
        int count = getMeasuredWidth() / textSize;
        for (int i = 0;i<count;i++) {
            NumberRainItem numberRainItem = new NumberRainItem(getContext());
            numberRainItem.highLightColor = highLightColor;
            numberRainItem.textSize = textSize;
            numberRainItem.normalColor = normalColor;
            numberRainItem.charType = charType;
            LayoutParams layoutParams = new LayoutParams(textSize + 10, getMeasuredHeight());
            numberRainItem.startOffset = (long)(Math.random() * 1000);
            addView(numberRainItem, layoutParams);
        }

    }
}
