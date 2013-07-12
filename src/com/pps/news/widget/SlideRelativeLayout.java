package com.pps.news.widget;

import com.pps.news.R;
import com.pps.news.util.ImageUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class SlideRelativeLayout extends RelativeLayout {

	private static final String TAG = "SlideRelativeLayout";

	private int offsetY = 0;
	private View view = null;
	private Handler handler = null;
	private Bitmap dragBitmap = null;
	
	public SlideRelativeLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public SlideRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				offsetY = (int) event.getY();  
				return isActionDown(event);
			case MotionEvent.ACTION_MOVE:
				offsetY = (int) event.getY();  
				invalidate();
				return true;
			case MotionEvent.ACTION_UP:
				handleActionUpEvent(event);
				return true;
		}
		return super.onTouchEvent(event);
	}

	// 是否点击到了滑动区域
	private boolean isActionDown(MotionEvent event) {
		Rect outRect = new Rect();
		view.getHitRect(outRect);
		boolean isHit = outRect.contains((int) event.getX(), (int) event.getY()); 
		return isHit;
	}

	private void handleActionUpEvent(MotionEvent event) {
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		view = findViewById(R.id.touchView);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int drawY = offsetY - view.getHeight();
		if (drawY < 0) {
			view.setVisibility(View.VISIBLE);
		} else {
			view.setVisibility(View.GONE);
			dragBitmap = ImageUtils.convertViewToBitmap(view);
			canvas.drawBitmap(dragBitmap, view.getLeft(), drawY, null);
		}
		
		/*if (offsetY > 0 && offsetY < view.getHeight() || offsetY > view.getBottom()) {
			dragBitmap = ImageUtils.convertViewToBitmap(view);
			int drawY = offsetY < view.getHeight() ? offsetY : offsetY - view.getHeight();
			canvas.drawBitmap(dragBitmap, view.getLeft(), drawY, null);
			view.setVisibility(View.GONE);
		} */
	}

	public void release() {
		view.destroyDrawingCache();
		view.setDrawingCacheEnabled(false);
		if (dragBitmap != null && !dragBitmap.isRecycled()) {
			dragBitmap.recycle();
			dragBitmap = null;
		}
	}
	
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
}
