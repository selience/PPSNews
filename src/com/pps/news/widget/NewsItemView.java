package com.pps.news.widget;

import com.pps.news.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NewsItemView extends RelativeLayout {

	private ImageView imageView;
	private TextView txtTitle;
	private ImageView loadingView;
	private AnimationDrawable mAnimation;

	public NewsItemView(Context context) {
		super(context);
		initialize();
	}

	public NewsItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public void initialize() {
		setClickable(true);
		setBackgroundResource(R.color.news_default_bg);
		LayoutInflater.from(getContext()).inflate(R.layout.news_item_view, this, true);
		imageView = (ImageView) findViewById(R.id.news_image);
		txtTitle = (TextView) findViewById(R.id.news_title);
		loadingView = (ImageView) findViewById(R.id.progress);
		mAnimation = (AnimationDrawable) loadingView.getDrawable();
		startAnimation();
	}

	/** 启动动画   */
	public void startAnimation() {
		if (!mAnimation.isRunning()) {
			loadingView.post(new Runnable() {
				@Override
				public void run() {
					mAnimation.start();
				}
			});
		}
	}

	/** 停止动画  */
	public void stopAnimation() {
		if (mAnimation.isRunning()) {
			mAnimation.stop();
		}
	}

	/** 设置新闻标题  */
	public void setTitle(CharSequence text) {
		txtTitle.setText(text);
	}

	/** 设置新闻图片 */
	public void setImageBitmap(Bitmap bitmap) {
		imageView.setImageBitmap(bitmap);
	}
}
