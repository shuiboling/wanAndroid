package com.example.wanandroid.widget.choose;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AddressChooseDecoration extends RecyclerView.ItemDecoration {

    private Context context;
    private Paint paint1, paint2,paint3;
    private float radius = 10;
    private int left=0,top=0;
    private List<AddressChooseBean> list;

    public AddressChooseDecoration(Context context, int color, int left, int top, List<AddressChooseBean> list){
        this.context = context;
        this.left = left;
        this.top = top;
        this.list = list;

        paint1 = new Paint();
        paint1.setAntiAlias(true);
        paint1.setColor(color);
        paint1.setStyle(Paint.Style.STROKE);

        paint2 = new Paint();
        paint2.setStrokeWidth(2);
        paint2.setColor(color);

        paint3 = new Paint();
        paint3.setColor(color);
    }

    //画item之前
    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

        Bitmap bitmap;
        int count = parent.getChildCount();
        float x = radius + left/2;
        for(int i=0;i<count;i++){
            View child = parent.getChildAt(i);
            int index = parent.getChildAdapterPosition(child);
            c.drawLine(left,child.getTop()-top,parent.getWidth(),child.getTop(),paint1);

            if(count == 1){
                drawCircle1(c,x,child.getTop()+(child.getHeight()/2));
                drawLine(c,x,child.getTop()+(child.getHeight()/2)+radius,x,child.getBottom()+top);
            } else {
                if(index != count - 1){
                    if(index != 0){
                        drawLine(c,x,child.getTop(),x,child.getTop()+(child.getHeight()/2)-radius);
                    }
                    drawCircle2(c,x,child.getTop()+(child.getHeight()/2));
                    drawLine(c,x,child.getTop()+(child.getHeight()/2)+radius,x,child.getBottom()+top);
                }else {
                    if(list.get(i).hasLeaf()){
                        drawCircle1(c,x,child.getTop()+(child.getHeight()/2));
                    }else {
                        drawCircle2(c,x,child.getTop()+(child.getHeight()/2));
                    }
                    drawLine(c,x,child.getTop(),x,child.getTop()+(child.getHeight()/2)-radius);
                }
            }
        }
    }

    //空心
    private void drawCircle1(Canvas canvas,float x,float y){
        canvas.drawCircle(x,y,radius,paint1);
    }

    //实心
    private void drawCircle2(Canvas canvas,float x,float y){
        canvas.drawCircle(x,y,radius,paint2);
    }

    private void drawLine(Canvas canvas,float x1,float y1,float x2,float y2){
        canvas.drawLine(x1,y1,x2,y2,paint3);
    }

    //画item之后
    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.left = left;
        outRect.top = top;
    }
}
