package com.lovearthstudio.duaui.base;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Author：Mingyu Yi on 2016/5/12 14:21
 * Email：461072496@qq.com
 */
public class CustomViewPager extends ViewPager {

    private boolean isSwipEnabled = true;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isSwipEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isSwipEnabled && super.onInterceptTouchEvent(event);
    }

    public void setSwipEnabled(boolean able) {
        this.isSwipEnabled = able;
    }
}
