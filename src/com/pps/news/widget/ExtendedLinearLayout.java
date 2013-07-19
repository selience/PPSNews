package com.pps.news.widget;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.WeakHashMap;
import com.pps.news.R;
import com.pps.news.bean.News;
import com.pps.news.util.ImageCache;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public abstract class ExtendedLinearLayout extends LinearLayout implements Observer {

	protected NewsItemView news_top;
	protected LinearLayout news_middle;
	protected LinearLayout news_bottom;
	protected NewsItemView news_left_top;
	protected NewsItemView news_right_top;
	protected NewsItemView news_left_bottom;
	protected NewsItemView news_right_bottom;

	private ImageCache imageFetcher;
	private Handler mHandler = new Handler();
	private Map<String, NewsItemView> mPhotosMap;
	
	ExtendedLinearLayout(Context context) {
		super(context);
		initialize();
	}

	ExtendedLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	protected abstract int getLayoutResId();

	
	protected void initialize() {
		setOrientation(LinearLayout.VERTICAL);
		LayoutInflater.from(getContext()).inflate(getLayoutResId(), this, true);
		news_middle = (LinearLayout) findViewById(R.id.news_middle);
		news_bottom = (LinearLayout) findViewById(R.id.news_bottom);
		news_top = (NewsItemView) findViewById(R.id.news_top);
		news_left_top = (NewsItemView) findViewById(R.id.news_left_top);
		news_right_top = (NewsItemView) findViewById(R.id.news_right_top);
		news_left_bottom = (NewsItemView) findViewById(R.id.news_left_bottom);
		news_right_bottom = (NewsItemView) findViewById(R.id.news_right_bottom);
		
		imageFetcher = ImageCache.getInstance();
		imageFetcher.addObserver(this);
		mPhotosMap = new WeakHashMap<String, NewsItemView>();
	}

	public void setItems(List<News> items) {
		if (items == null)
			return;

		for (News news : items) {
			switch (news.getPosition_flag()) {
			case News.NEWS_POSITION_FLAG_TOP:
				setDataSource(news_top, news);
				break;
			case News.NEWS_POSITION_FLAG_LEFT_TOP:
				setDataSource(news_left_top, news);
				break;
			case News.NEWS_POSITION_FLAG_RIGHT_TOP:
				setDataSource(news_right_top, news);
				break;
			case News.NEWS_POSITION_FLAG_LEFT_BOTTOM:
				setDataSource(news_left_bottom, news);
				break;
			case News.NEWS_POSITION_FLAG_RIGHT_BOTTOM:
				setDataSource(news_right_bottom, news);
				break;
			}
		}
	}

	private void setDataSource(NewsItemView view, News news) {
		if (news != null) {
			view.setTag(news);
			view.setTitle(news.getMain_title());
			setPhotos(news.getThumb_url(), view);
		}
	}
	
	private void setPhotos(String photoUrl, NewsItemView imageView) {
		if (imageFetcher.exists(photoUrl)) {
			imageView.stopAnimation();
			Bitmap bitmap = imageFetcher.displayBitmap(photoUrl);
			imageView.setImageBitmap(bitmap);
			if (mPhotosMap.containsKey(photoUrl)) {
				mPhotosMap.remove(photoUrl);
			}
		} else {
			imageView.setImageBitmap(null);
			imageView.startAnimation();
			mPhotosMap.put(photoUrl, imageView);
			imageFetcher.request(photoUrl);
		}
	}
	
	@Override
	public void update(Observable observable, Object data) {
		final String url = data.toString();
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				for (String item : mPhotosMap.keySet()) {
					if (item.equals(url)) {
						setPhotos(item, mPhotosMap.get(item));
					}
				}
			}
		});
	}
	
	/** 设置监听事件  */
	public void setOnItemsClickListener(OnClickListener mOnClickListener) {
		news_top.setOnClickListener(mOnClickListener);
		news_left_top.setOnClickListener(mOnClickListener);
		news_right_top.setOnClickListener(mOnClickListener);
		news_left_bottom.setOnClickListener(mOnClickListener);
		news_right_bottom.setOnClickListener(mOnClickListener);
	}

	/** 移除图片监听  */
	public void removeObservers() {
		mPhotosMap.clear();
		imageFetcher.deleteObserver(this);
	}
}
