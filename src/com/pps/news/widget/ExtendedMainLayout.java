package com.pps.news.widget;

import java.util.List;
import com.pps.news.R;
import com.pps.news.bean.News;
import com.pps.news.util.UIUtil;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class ExtendedMainLayout extends LinearLayout {

	private NewsItemView news_top;
	private NewsItemView news_left_top;
	private NewsItemView news_right_top;
	private NewsItemView news_left_bottom;
	private NewsItemView news_right_bottom;
	private LinearLayout news_middle, news_bottom;
	
	private int mHeight;
	
	public ExtendedMainLayout(Context context) {
		super(context);
		init(context);
	}
	
	public ExtendedMainLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mHeight = UIUtil.getScreenHeight(context);
		setOrientation(LinearLayout.VERTICAL);
		
		LayoutInflater.from(context).inflate(R.layout.main_container_view, this, true);
		news_middle = (LinearLayout) findViewById(R.id.news_middle);
		news_bottom = (LinearLayout) findViewById(R.id.news_bottom);
		news_top = (NewsItemView) findViewById(R.id.news_top);
		news_left_top = (NewsItemView) findViewById(R.id.news_left_top);
		news_right_top = (NewsItemView) findViewById(R.id.news_right_top);
		news_left_bottom = (NewsItemView) findViewById(R.id.news_left_bottom);
		news_right_bottom = (NewsItemView) findViewById(R.id.news_right_bottom);
	}

	public void setItems(List<News> items) {
		if (items == null) return;
		
		for (News news : items) {
			switch (news.getPosition_flag()) {
				case News.NEWS_POSITION_FLAG_TOP:
					news_top.setDataSource(news);
					break;
				case News.NEWS_POSITION_FLAG_LEFT_TOP:
					news_left_top.setDataSource(news);
					break;
				case News.NEWS_POSITION_FLAG_RIGHT_TOP:
					news_right_top.setDataSource(news);
					break;
				case News.NEWS_POSITION_FLAG_LEFT_BOTTOM:
					news_left_bottom.setDataSource(news);
					break;
				case News.NEWS_POSITION_FLAG_RIGHT_BOTTOM:
					news_right_bottom.setDataSource(news);
					break;
			}
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int height = mHeight - UIUtil.dip2px(getContext(), 20);
		height = height / 3;
		ViewGroup.LayoutParams lp = news_top.getLayoutParams();
		lp.height = height;
		news_top.setLayoutParams(lp);
		
		lp = news_middle.getLayoutParams();
		lp.height = height;
		news_middle.setLayoutParams(lp);
		news_bottom.setLayoutParams(lp);
		
		lp = news_left_top.getLayoutParams();
		lp.width = UIUtil.dip2px(getContext(), 150);
		news_left_top.setLayoutParams(lp);
		
		lp = news_left_bottom.getLayoutParams();
		lp.width = UIUtil.dip2px(getContext(), 175);
		news_left_bottom.setLayoutParams(lp);
	}
	
	public void setOnItemsClickListener(OnClickListener mOnClickListener) {
		news_top.setOnClickListener(mOnClickListener);
		news_left_top.setOnClickListener(mOnClickListener);
		news_right_top.setOnClickListener(mOnClickListener);
		news_left_bottom.setOnClickListener(mOnClickListener);
		news_right_bottom.setOnClickListener(mOnClickListener);
	}
	
	
	public void removeObservers() {
		news_top.deleteObserver();
		news_left_top.deleteObserver();
		news_right_bottom.deleteObserver();
		news_left_bottom.deleteObserver();
		news_right_bottom.deleteObserver();
	}
}
