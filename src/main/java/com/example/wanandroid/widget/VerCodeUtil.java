package com.example.wanandroid.widget;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.Log;

import java.util.Random;

public class VerCodeUtil {
    private char[] codeChars = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };
    private int codeNum = 4;    //验证码长度
    private String codeString = "";   //验证码
    private Random random = new Random();

    private int textSize = 50;
    private Paint textPaint, linePaint;
    private int lineCount = 7;

    private Bitmap bitmap;

    private int defaultWidth, defaultHeight;
    private int paddingVertical = 50;
    private int paddingHorizontal = 55;
    private int wordPaddingLeft = 15;

    public VerCodeUtil() {
        init();
    }

    public void init() {

        defaultWidth = (int) (codeNum * textSize + paddingHorizontal * 2 + (codeNum - 1) * wordPaddingLeft);
        defaultHeight = (int) (textSize + paddingVertical * 2);

        textPaint = new Paint();
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);

        linePaint = new Paint();
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStrokeWidth(2);

    }

    public Bitmap getCodePic() {
        bitmap = Bitmap.createBitmap(defaultWidth, defaultHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        getCodes();

        Paint bgRect = new Paint();
        bgRect.setStyle(Paint.Style.FILL);
        bgRect.setColor(Color.WHITE);
        RectF rectF = new RectF(0, 0, defaultWidth, defaultHeight);
        canvas.drawRect(rectF, bgRect);

        for (int i = 0; i < lineCount; i++) {
            drawOneLine(canvas);
        }

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        float baseline = rectF.centerY() + distance;

        for (int i = 0; i < codeNum; i++) {

            textPaint.setTextSize(getTextSize());
            textPaint.setTextSkewX(random.nextFloat() - 0.5f);
            textPaint.setColor(getTextColor());
            canvas.drawText(codeString.charAt(i) + "",
                    paddingHorizontal + (textSize / 2) + i * (wordPaddingLeft + textSize),
                    baseline, textPaint);
        }

        return bitmap;
    }

    private void drawOneLine(Canvas canvas) {
        int startX = random.nextInt(defaultWidth);
        int startY = random.nextInt(defaultHeight);
        int endX = startX + random.nextInt(defaultWidth);
        int endY = startY + random.nextInt(defaultWidth);

        linePaint.setColor(getTextColor());

        canvas.drawLine(startX, startY, endX, endY, linePaint);
    }

    private int getTextColor() {
        return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    private float getTextSize() {
        return random.nextInt(textSize) + 50;
    }

    public void getCodes() {
        codeString = "";

        for (int i = 0; i < codeNum; i++) {
            codeString += codeChars[random.nextInt(codeChars.length)];
        }
        Log.d("zyy", codeString + "");
    }

    public Bitmap isEqualCode(String num) {
        int length = 0;

        if (!TextUtils.isEmpty(num) && !TextUtils.isEmpty(codeString)) {
            if (num.length() != codeString.length()) {
                return getCodePic();
            } else {
                length = num.length();
            }
        } else {
            return getCodePic();
        }

        for (int i = 0; i < length; i++) {

            String str1 = num.charAt(i) + "";
            String str2 = codeString.charAt(i) + "";

            if (!str1.toLowerCase().equals(str2.toLowerCase())) {
                return getCodePic();
            }
        }

        return null;
    }
}
