package com.example.wanandroid.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.wanandroid.R;

public class NestedScroll extends LinearLayout implements NestedScrollingParent {

    private int mTopViewHeight;
    private View mTop;
    private View mNav;
    private RelativeLayout mViewPager;


    public NestedScroll(Context context) {
        super(context);
    }

    public NestedScroll(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedScroll(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NestedScroll(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        //不限制顶部的高度
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getChildAt(0).measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        params.height = getMeasuredHeight() - mNav.getMeasuredHeight();
        setMeasuredDimension(getMeasuredWidth(), mTop.getMeasuredHeight() + mNav.getMeasuredHeight() + mViewPager.getMeasuredHeight());

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int tmpHeight = 0;
        for(int i = 0;i<getChildCount();i++){
            View child = getChildAt(i);
            if(child == mTop){
                child.layout(0,-mTop.getMeasuredHeight(),child.getMeasuredWidth(), 0);
            }else {
                child.layout(0,tmpHeight,child.getMeasuredWidth(),child.getMeasuredHeight());
                tmpHeight += child.getMeasuredHeight();
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = mTop.getMeasuredHeight();
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        mTop = findViewById(R.id.id_topview);
        mNav = findViewById(R.id.nav);
        View view = findViewById(R.id.rl);
//        if (!(view instanceof ViewPager))
//        {
//            throw new RuntimeException(
//                    "id_stickynavlayout_viewpager show used by ViewPager !");
//        }
        mViewPager = (RelativeLayout) view;
    }

    //  是否接收嵌套滚动
    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes) {
//        纵向返回true
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes) {

    }

    @Override
    public void onStopNestedScroll(@NonNull View target) {

    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }

//    是否消耗滚动参数
    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        Log.d("zyye","dy:"+dy);

        //如果是上滑且顶部控件未完全隐藏，则消耗掉dy，即consumed[1]=dy;
        //以mTop的bottom=0时为原点，只要mTop露头了，scrollY就是负值
        boolean hiddenTop = dy > 0 && getScrollY() < 0;
        //-1检查view向上=手指向下滑动，1向下
//        如果是下滑且内部View已经无法继续下拉，则消耗掉dy，即consumed[1]=dy，
        boolean showTop = dy < 0 && !target.canScrollVertically(-1);
//
//        消耗掉的意思，就是自己去执行scrollBy，实际上就是我们的StickNavLayout滑动
        if(hiddenTop || showTop){
            scrollBy(0, dy);
            consumed[1] = dy;
        }


    }

//    是否消耗fling事件
    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
//        当顶部控件显示时，fling可以让顶部控件隐藏或者显示。
        return false;
    }

//    处理惯性事件
    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {

        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return 0;
    }

//    限制滑动范围
    @Override
    public void scrollTo(int x, int y) {

        if(y>0){
            y=0;
        }

        int dy = y-getScrollY();
        //down
        if(dy <= 0){
            if (Math.abs(y) >= mTopViewHeight){
                y = -mTopViewHeight;
            }
        }else {

        }

        super.scrollTo(x,y);

    }

}
