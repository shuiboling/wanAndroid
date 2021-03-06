package com.example.wanandroid.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.annotation.RequiresApi;

/**
 *
 * 自定义PopWindow类，封装了PopWindow的一些常用属性，用Builder模式支持链式调用
 * author:zy
 */

public class CommonPopWindow implements PopupWindow.OnDismissListener{
    private static final String TAG = "CommonPopWindow";
    private static final float DEFAULT_ALPHA = 0.7f;
    private Context mContext;
    private int mWidth;
    private int mHeight;
    private boolean mIsFocusable = true;
    private boolean mIsOutside = true;
    private int mResLayoutId = -1;
    private View mContentView;
    private PopupWindow mPopupWindow;
    private int mAnimationStyle = -1;

    private boolean mClippEnable = true;//default is true
    private boolean mIgnoreCheekPress = false;
    private int mInputMode = -1;
    private PopupWindow.OnDismissListener mOnDismissListener;
    private int mSoftInputMode = -1;
    private boolean mTouchable = true;//default is ture
    private View.OnTouchListener mOnTouchListener;

    private Window mWindow;//当前Activity 的窗口
    /**
     * 弹出PopWindow 背景是否变暗，默认不会变暗。
     */
    private boolean mIsBackgroundDark = false;
    private float mBackgroundDarkValue = 0;// 背景变暗的值，0 - 1
    private Drawable mBackground;
    /**
     * 设置是否允许点击 PopupWindow之外的地方，关闭PopupWindow
     */
    private boolean enableOutsideTouchDisMiss = true;// 默认点击pop之外的地方可以关闭

    private CommonPopWindow(Context context){
        mContext = context;
    }

    public int getWidth() {
        return mPopupWindow.getWidth();
    }

    public int getHeight() {
        return mPopupWindow.getHeight();
    }

    public void updateHeightAndWidth(int height,int width){
        if(mPopupWindow.isShowing()){
            mPopupWindow.update(width,height);
        }
    }

    public void setHeight(int height){
        mPopupWindow.setHeight(height);
    }

    public void setWidth(int width){
        mPopupWindow.setWidth(width);
    }
    /**
     *  显示PopWindow
     */
    public CommonPopWindow showAsDropDown(View anchor, int xOff, int yOff){
        if(mPopupWindow!=null){
            if(mIsBackgroundDark){
                setBackgroundDark();
            }
            mPopupWindow.showAsDropDown(anchor,xOff,yOff);
        }
        return this;
    }

    public CommonPopWindow showAtBottom(View anchor){
        if(mPopupWindow!=null) {
            if(mIsBackgroundDark){
                setBackgroundDark();
            }
            mPopupWindow.showAtLocation(anchor, Gravity.BOTTOM,0, 0);
        }
        return this;
    }

    public CommonPopWindow showAsDropDown(View anchor){
        if(mPopupWindow!=null){
            if(mIsBackgroundDark){
                setBackgroundDark();
            }
            mPopupWindow.showAsDropDown(anchor);
        }
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CommonPopWindow showAsDropDown(View anchor, int xOff, int yOff, int gravity){
        if(mPopupWindow!=null){
            if(mIsBackgroundDark){
                setBackgroundDark();
            }
            mPopupWindow.showAsDropDown(anchor,xOff,yOff,gravity);
        }
        return this;
    }
    /**
     * 相对于父控件的位置（通过设置Gravity.CENTER，下方Gravity.BOTTOM等 ），可以设置具体位置坐标
     * @param parent 父控件
     * @param gravity
     * @param x the popup's x location offset
     * @param y the popup's y location offset
     * @return
     */
    public CommonPopWindow showAtLocation(View parent, int gravity, int x, int y){
        if(mPopupWindow!=null){
            if(mIsBackgroundDark){
                setBackgroundDark();
            }
            mPopupWindow.showAtLocation(parent,gravity,x,y);
        }
        return this;
    }

    /*
    * 设置背景变暗
    * */
    public void setBackgroundDark(){
        //如果设置的值在0 - 1的范围内，则用设置的值，否则用默认值
        final  float alpha = (mBackgroundDarkValue > 0 && mBackgroundDarkValue < 1) ? mBackgroundDarkValue : DEFAULT_ALPHA;
        setBackgroundAlpha(alpha);
    }

    public void setBackgroundAlpha(float bgAlpha) {
//        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Activity activity = (Activity)mContentView.getContext();
        if(activity!=null) {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            lp.alpha = bgAlpha; //0.0-1.0
            activity.getWindow().setAttributes(lp);
        }
    }
    /**
     * 添加一些属性设置
     * @param popupWindow
     */
    private void apply(PopupWindow popupWindow){
        popupWindow.setClippingEnabled(mClippEnable);
        if(mIgnoreCheekPress){
            popupWindow.setIgnoreCheekPress();
        }
        if(mInputMode!=-1){
            popupWindow.setInputMethodMode(mInputMode);
        }
        if(mSoftInputMode!=-1){
            popupWindow.setSoftInputMode(mSoftInputMode);
        }
        if(mOnDismissListener!=null){
            popupWindow.setOnDismissListener(mOnDismissListener);
        }
        if(mOnTouchListener!=null){
            popupWindow.setTouchInterceptor(mOnTouchListener);
        }
        popupWindow.setTouchable(mTouchable);

    }
    /*
    * 构建PopWindow
    * */
    private PopupWindow build(){

        if(mContentView == null){
            mContentView = LayoutInflater.from(mContext).inflate(mResLayoutId,null);
        }

        if(mWidth != 0 && mHeight!=0 ){
            mPopupWindow = new PopupWindow(mContentView,mWidth,mHeight);
        }else{
            mPopupWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if(mAnimationStyle!=-1){
            mPopupWindow.setAnimationStyle(mAnimationStyle);
        }
        //设置一些属性
        apply(mPopupWindow);

        if(mWidth == 0 || mHeight == 0){
            mPopupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            //如果外面没有设置宽高的情况下，计算宽高并赋值
            mWidth = mPopupWindow.getContentView().getMeasuredWidth();
            mHeight = mPopupWindow.getContentView().getMeasuredHeight();
        }

        // 添加dismiss 监听
        mPopupWindow.setOnDismissListener(this);
        mPopupWindow.setFocusable(mIsFocusable);
        if(mBackground != null){
            mPopupWindow.setBackgroundDrawable(mBackground);
        }
        mPopupWindow.setOutsideTouchable(mIsOutside);

        //2017.6.27 add:fix 设置  setOutsideTouchable（false）点击外部取消的bug.
        // 判断是否点击PopupWindow之外的地方关闭 popWindow
//        if(!enableOutsideTouchDisMiss){
//            //注意这三个属性必须同时设置，不然不能disMiss，以下三行代码在Android 4.4 上是可以，然后在Android 6.0以上，下面的三行代码就不起作用了，就得用下面的方法
//            mPopupWindow.setFocusable(true);
//            mPopupWindow.setOutsideTouchable(false);
//            mPopupWindow.setBackgroundDrawable(null);
//            //注意下面这三个是contentView 不是PopupWindow
//            mPopupWindow.getContentView().setFocusable(true);
//            mPopupWindow.getContentView().setFocusableInTouchMode(true);
//            mPopupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
//                @Override
//                public boolean onKey(View v, int keyCode, KeyEvent event) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK) {
//                        mPopupWindow.dismiss();
//
//                        return true;
//                    }
//                    return false;
//                }
//            });
//            //在Android 6.0以上 ，只能通过拦截事件来解决
//            mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//
//                    final int x = (int) event.getX();
//                    final int y = (int) event.getY();
//
//                    if ((event.getAction() == MotionEvent.ACTION_DOWN)
//                            && ((x < 0) || (x >= mWidth) || (y < 0) || (y >= mHeight))) {
//                        LogUtils.e(TAG,"out side ");
//                        LogUtils.e(TAG,"width:"+mPopupWindow.getWidth()+"height:"+mPopupWindow.getHeight()+" x:"+x+" y  :"+y);
//                        return true;
//                    } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//                        LogUtils.e(TAG,"out side ...");
//                        return true;
//                    }
//                    return false;
//                }
//            });
//        }else{
//            mPopupWindow.setFocusable(mIsFocusable);
//            mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            mPopupWindow.setOutsideTouchable(mIsOutside);
//        }
        // update
        mPopupWindow.update();

        return mPopupWindow;
    }

    @Override
    public void onDismiss() {
        dismiss();
    }

    /**
     * 关闭popWindow
     */
    public void dismiss(){

        if(mOnDismissListener!=null){
            mOnDismissListener.onDismiss();
        }
        //如果设置了背景变暗，那么在dismiss的时候需要还原
        setBackgroundAlpha(1.0f);

        if(mPopupWindow!=null && mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
        }
    }

    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }

    public boolean isShowing(){
        return mPopupWindow.isShowing();
    }

    //Builder
    public static class PopupWindowBuilder{
        private CommonPopWindow mCommonPopWindow;

        public PopupWindowBuilder(Context context){
            mCommonPopWindow = new CommonPopWindow(context);
        }
        public PopupWindowBuilder size(int width,int height){
            mCommonPopWindow.mWidth = width;
            mCommonPopWindow.mHeight = height;
            return this;
        }

        public PopupWindowBuilder setFocusable(boolean focusable){
            mCommonPopWindow.mIsFocusable = focusable;
            return this;
        }

        public PopupWindowBuilder setView(int resLayoutId){
            mCommonPopWindow.mResLayoutId = resLayoutId;
            mCommonPopWindow.mContentView = null;
            return this;
        }

        public PopupWindowBuilder setView(View view){
            mCommonPopWindow.mContentView = view;
            mCommonPopWindow.mResLayoutId = -1;
            return this;
        }

        public PopupWindowBuilder setOutsideTouchable(boolean outsideTouchable){
            mCommonPopWindow.mIsOutside = outsideTouchable;
            return this;
        }

        /**
         * 设置弹窗动画
         * @param animationStyle
         * @return
         */
        public PopupWindowBuilder setAnimationStyle(int animationStyle){
            mCommonPopWindow.mAnimationStyle = animationStyle;
            return this;
        }

        public PopupWindowBuilder setClippingEnable(boolean enable){
            mCommonPopWindow.mClippEnable =enable;
            return this;
        }

        public PopupWindowBuilder setIgnoreCheekPress(boolean ignoreCheekPress){
            mCommonPopWindow.mIgnoreCheekPress = ignoreCheekPress;
            return this;
        }

        public PopupWindowBuilder setInputMethodMode(int mode){
            mCommonPopWindow.mInputMode = mode;
            return this;
        }

        public PopupWindowBuilder setOnDismissListener(PopupWindow.OnDismissListener onDismissListener){
            mCommonPopWindow.mOnDismissListener = onDismissListener;
            return this;
        }

        public PopupWindowBuilder setSoftInputMode(int softInputMode){
            mCommonPopWindow.mSoftInputMode = softInputMode;
            return this;
        }

        public PopupWindowBuilder setTouchable(boolean touchable){
            mCommonPopWindow.mTouchable = touchable;
            return this;
        }

        public PopupWindowBuilder setTouchInterceptor(View.OnTouchListener touchInterceptor){
            mCommonPopWindow.mOnTouchListener = touchInterceptor;
            return this;
        }

//        /**
//         * 设置背景变暗是否可用
//         * @param isDark
//         * @return
//         */
//        public PopupWindowBuilder enableBackgroundDark(boolean isDark){
//            mCommonPopWindow.mIsBackgroundDark = isDark;
//            return this;
//        }

        /**
         * 设置背景变暗的值
         * @param darkValue
         * @return
         */
        public PopupWindowBuilder setBgDarkAlpha(float darkValue){
            mCommonPopWindow.mIsBackgroundDark = true;
            mCommonPopWindow.mBackgroundDarkValue = darkValue;
            return this;
        }

        public PopupWindowBuilder setBackgroundDrawable(Drawable background){
            mCommonPopWindow.mBackground = background;
            return this;
        }

//        /**
//         * 设置是否允许点击 PopupWindow之外的地方，关闭PopupWindow
//         * @param disMiss
//         * @return
//         */
//        public PopupWindowBuilder enableOutsideTouchableDismiss(boolean disMiss){
//            mCommonPopWindow.enableOutsideTouchDisMiss = disMiss;
//            return this;
//        }

        //构建PopWindow
        public CommonPopWindow create(){
            mCommonPopWindow.build();
            return mCommonPopWindow;
        }

    }

}
