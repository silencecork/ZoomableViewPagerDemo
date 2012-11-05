package com.silencecork.example.zoompager;

import com.silencecork.example.zoompager.ViewPagerEx.OnScrollableListener;
import com.silencecork.widget.ImageViewTouch;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends Activity {
	
	private ViewPagerEx mPager;
	private static final int[] RES = {R.drawable.image01, R.drawable.image02, R.drawable.image03};
	private Bitmap [] mBitmaps;
	private ImageViewTouch mTouches[];
	private int mPrevSelectedPos = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// initialize all view and related content
		setContentView(R.layout.main);
		
		mPager = (ViewPagerEx) findViewById(R.id.pagger);
		mBitmaps = new Bitmap[RES.length];
		mTouches = new ImageViewTouch[RES.length];
		for (int i = 0; i < RES.length; i++) {
			mBitmaps[i] = BitmapFactory.decodeResource(getResources(), RES[i]);
			mTouches[i] = new ImageViewTouch(this);
			mTouches[i].initTouch(true, false, null);
			mTouches[i].setImageBitmapResetBase(mBitmaps[i], true);
			mTouches[i].setTag(i);
		}
		
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if (mPrevSelectedPos != -1 && position != mPrevSelectedPos) {
					mTouches[mPrevSelectedPos].resetToBeginState();
				}
				mPrevSelectedPos = position;
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}
			
			@Override
			public void onPageScrollStateChanged(int position) {
			}
		});
		
		mPager.setOnScrollableListener(new OnScrollableListener() {
			
			@Override
			public boolean canScroll(MotionEvent event) {
				// following code trying to find out current ImageViewTouch is exceed to the limitation
				// of content in ImageViewTouch
				ImageViewTouch current = mTouches[mPrevSelectedPos];
				Matrix mat = current.getImageViewMatrix();
		    	float[] leftTop = { 0.F, 0.F };
		    	float[] rightBottom =  { current.getDisplayedBitmap().getWidth(),	current.getDisplayedBitmap().getHeight() };
				
		    	// mapping pointer to current content matrix
		    	current.translatePoint(mat, leftTop);
		    	current.translatePoint(mat, rightBottom);
		    	
		    	int viewWidth = current.getWidth();
		    	
		    	// if > 0 means exceed to the limitation of left
		    	if (leftTop[0] >= 0) {
		    		return true;
		    	} 
		    	
		    	// if > current view width, means exceed to the limitation of right
		    	if (rightBottom[0] <= viewWidth) {
		    		return true;
		    	}
		    	
		    	// Touch scale records the touch zoom, double tap zoom related zooming ratio
				return (current.getTouchScale() == 1.0F); 
			}
		});
		
		mPager.setAdapter(new PhotoAdapter());
	}
	
	
	class PhotoAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return RES.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return (arg0 == arg1);
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPagerEx)container).addView(mTouches[position], 0);
			return mTouches[position];
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPagerEx)container).removeView(mTouches[position]);
		}
		
	}
}
