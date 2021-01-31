package com.example.wanandroid.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.wanandroid.R;

import java.util.ArrayList;
import java.util.List;

public class FlowLayoutVGVersion extends ViewGroup {

    private List<ChildPos> allChildren;
    private int mHorizontal = 10;   //两个item之间的间隔
    private int mVertical = 10; //两行之间的间隔

    private class ChildPos{
        int left,top,right,bottom;

        ChildPos(int left,int top,int right,int bottom){
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }
    }

    //new时调用的
    public FlowLayoutVGVersion(Context context) {
        super(context);
    }

    //xml中，属性，反射
    public FlowLayoutVGVersion(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,R.styleable.FlowLayoutRVersion);
        mHorizontal = typedArray.getDimensionPixelSize(R.styleable.FlowLayoutRVersion_margin_horizontal,mHorizontal);
        mVertical = typedArray.getDimensionPixelOffset(R.styleable.FlowLayoutRVersion_margin_vertical,mVertical);
        //释放
        typedArray.recycle();
    }

    //style
    public FlowLayoutVGVersion(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FlowLayoutVGVersion(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(){
        if(allChildren == null){
            allChildren = new ArrayList<>();
        }else {
            allChildren.clear();
        }
    }

    //测量
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        init();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //处理自己的padding和子view的margin
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();


        int lineHeight = 0;
        int usedWidth = paddingLeft + mHorizontal;
        int parentWidth = 0;
        int parentHeight = paddingTop + mVertical;
        //1.度量孩子
        for(int i = 0;i<getChildCount();i++){

            View childView = getChildAt(i);

            measureChild(childView,widthMeasureSpec,heightMeasureSpec);

            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            //换行
            if(usedWidth + childWidth + mHorizontal > widthSize - paddingRight){

                parentWidth = Math.max(parentWidth,usedWidth);
                parentHeight = parentHeight + lineHeight + mVertical;

                usedWidth = paddingLeft + mHorizontal;

                allChildren.add(new ChildPos(usedWidth,parentHeight
                        ,usedWidth+childWidth,parentHeight+childHeight));

                lineHeight = 0;

            }else {

                allChildren.add(new ChildPos(usedWidth, parentHeight
                        , usedWidth + childWidth, parentHeight + childHeight));
            }

            usedWidth = usedWidth + childWidth + mHorizontal;
            lineHeight = Math.max(lineHeight,childHeight);

        }

        //处理wrap_content
        int realWidth = widthMode == MeasureSpec.AT_MOST ? parentWidth : widthMeasureSpec;
        int realHeight = heightMode == MeasureSpec.AT_MOST ? parentHeight : heightMeasureSpec;

        //2.度量自己
        setMeasuredDimension(realWidth,realHeight);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    //布局
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        for(int i=0;i<getChildCount();i++){
            View childView = getChildAt(i);
            ChildPos pos = allChildren.get(i);
            childView.layout(pos.left,pos.top,pos.right,pos.bottom);
        }
    }

    //绘制
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
