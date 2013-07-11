package com.pps.news.widget;

import com.pps.news.R;
import com.pps.news.util.UIUtil;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class ExtendedSubLayout extends ExtendedLinearLayout {

	public ExtendedSubLayout(Context context) {
		super(context);
	}

	public ExtendedSubLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int height = getMeasuredHeight() / 3;
		ViewGroup.LayoutParams lp = news_top.getLayoutParams();
		lp.height = height;
		news_top.setLayoutParams(lp);
		
		lp = news_bottom.getLayoutParams();
		lp.height = height;
		news_bottom.setLayoutParams(lp);
		
		lp = news_middle.getLayoutParams();
		lp.height = height;
		news_middle.setLayoutParams(lp);
		
		lp = news_left_top.getLayoutParams();
		lp.width = UIUtil.dip2px(getContext(), 175);
		news_left_top.setLayoutParams(lp);
		
		lp = news_left_bottom.getLayoutParams();
		lp.width = UIUtil.dip2px(getContext(), 150);
		news_left_bottom.setLayoutParams(lp);
	}
	

	@Override
	protected int getLayoutResId() {
		// TODO Auto-generated method stub
		return R.layout.sub_container_view;
	}
}
