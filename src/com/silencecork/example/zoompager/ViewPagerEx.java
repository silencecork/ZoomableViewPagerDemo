package com.silencecork.example.zoompager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ViewPagerEx extends ViewPager {

	interface OnScrollableListener {
		/**
		 * When this function been called means you can decide this touch event
		 * will be handled by general touch behavior in ViewPager or handled by your self
		 * 
		 * @param event current touch event
		 * @return false means you want to handle touch event yourself
		 */
		public boolean canScroll(MotionEvent event);
	}
	
	private OnScrollableListener mListener;
	
	public ViewPagerEx(Context context) {
		super(context);
	}
	
	public ViewPagerEx(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setOnScrollableListener(OnScrollableListener l) {
		mListener = l;
	}
	
	/**
	 * Override original method in ViewPager, because some case we do not want view flip to next one
	 * If the internal content of view is larger than view's size
	 * 
	 * Here is a chance to prevent the horizontal scroll event been eaten by ViewPager
	 * set the on scrollable listener and returned false can prevent touch event been eaten
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (mListener != null) {
			if (!mListener.canScroll(event)) return false;
		}
		return super.onInterceptTouchEvent(event);
	}
	

}
