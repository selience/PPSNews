package com.pps.news.widget;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.WeakHashMap;
import com.pps.news.R;
import com.pps.news.bean.News;
import com.pps.news.util.ImageCache;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NewsItemView extends RelativeLayout implements Observer {

	private ImageView imageView;
	private TextView txtTitle;
	private ImageCache imageFetcher;
	private AnimationDrawable mAnimation;
	private Handler mHandler = new Handler();
	private Map<String, ImageView> mPhotosMap = null;
	
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
		imageFetcher = ImageCache.getInstance();
		imageFetcher.addObserver(this);
		mPhotosMap = new WeakHashMap<String, ImageView>();
		
		LayoutInflater.from(getContext()).inflate(R.layout.news_item_view, this, true);
		imageView = (ImageView) findViewById(R.id.news_image);
		txtTitle = (TextView) findViewById(R.id.news_title);
		ImageView progress = (ImageView) findViewById(R.id.progress);
		
		mAnimation = (AnimationDrawable) progress.getDrawable();
		mAnimation.start();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	
	public void deleteObserver() {
		if (mPhotosMap != null) {
			mPhotosMap.clear();
			mPhotosMap = null;
		}
		if (imageFetcher != null) {
			imageFetcher.deleteObserver(this);
		}
	}
	
	public void setDataSource(News news) {
		if (news != null) {
			setTag(news);
			txtTitle.setText(news.getMain_title());
			setPhotos(news.getThumb_url(), imageView);
		}
	}

	private void setPhotos(String photoUrl, ImageView imageView) {
		if (imageFetcher.exists(photoUrl)) {
			mAnimation.stop();
			Bitmap bitmap = imageFetcher.displayBitmap(photoUrl);
			imageView.setImageBitmap(bitmap);
		} else {
			mAnimation.start();
			if (!mPhotosMap.containsKey(photoUrl)) {
				mPhotosMap.put(photoUrl, imageView);
			}
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
}
