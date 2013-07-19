package com.pps.news.widget;

import com.pps.news.R;
import com.pps.news.util.ImageUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SlideRelativeLayout extends RelativeLayout {
	private static final String TAG = "SlideRelativeLayout";

	private View view = null;
	private TextView topView = null;
	private TextView bottomView = null;
	
	private int locationY = 0;	
	private Handler handler = new Handler();
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
		locationY = (int) event.getY();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Log.i(TAG, "是否点击到位=" + isActionDown(event));
				return isActionDown(event);
			case MotionEvent.ACTION_MOVE:
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				handleActionUpEvent(event);
				break;
		}
		return true;
	}

	// 是否点击到了滑动区域
	private boolean isActionDown(MotionEvent event) {
		Rect outRect = new Rect();
		view.getHitRect(outRect);
		boolean isIn = outRect.contains((int) event.getX(), (int) event.getY());
		/*if (isIn) {
			view.setVisibility(View.INVISIBLE);
			return true;
		}*/
		return isIn;
	}

	private void handleActionUpEvent(MotionEvent event) {
		System.out.println("############"+view.getBottom());
		if (locationY > 530) {
			handler.postDelayed(mRunnable, 10L);
		}
	}
	
	private Runnable mRunnable = new Runnable() {
		
		@Override
		public void run() {
			locationY = locationY - (int) (0.9 * 20L);
			if (locationY > 550) {
				handler.postDelayed(this, 20L);
				invalidate();
			}
		}
	};
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		view = findViewById(R.id.touchView);
		topView = (TextView) findViewById(R.id.turn);
		bottomView = (TextView) findViewById(R.id.turn_and_update);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		int drawY = locationY - view.getHeight();
		if (drawY < topView.getHeight() || 
				locationY > bottomView.getBottom()+10) {
//			view.setVisibility(View.VISIBLE);
		} else {
//			if (locationY > bottomView.getBottom()) {
//				return;
//			}
//			if (drawY < topView.getTop()) {
//				return;
//			}
			view.setVisibility(View.GONE);
			dragBitmap = ImageUtils.convertViewToBitmap(view);
			canvas.drawBitmap(dragBitmap, view.getLeft(), drawY, null);
		}
	}

	private void isLocked() {
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
