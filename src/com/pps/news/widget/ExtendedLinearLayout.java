package com.pps.news.widget;

import java.util.List;
import com.pps.news.R;
import com.pps.news.bean.News;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public abstract class ExtendedLinearLayout extends LinearLayout {

	protected NewsItemView news_top;
	protected LinearLayout news_middle;
	protected LinearLayout news_bottom;
	protected NewsItemView news_left_top;
	protected NewsItemView news_right_top;
	protected NewsItemView news_left_bottom;
	protected NewsItemView news_right_bottom;

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
	}

	public void setItems(List<News> items) {
		if (items == null)
			return;

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
