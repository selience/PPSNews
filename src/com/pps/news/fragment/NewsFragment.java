package com.pps.news.fragment;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.pps.news.NewsDetailActivity;
import com.pps.news.R;
import com.pps.news.NewsActivity.OnRefreshItemListener;
import com.pps.news.app.BaseFragment;
import com.pps.news.bean.News;
import com.pps.news.constant.Constants;
import com.pps.news.util.CacheUtil;
import com.pps.news.widget.ExtendedLinearLayout;

public class NewsFragment extends BaseFragment implements OnClickListener, OnRefreshItemListener {
	private static final String FRAGMENT_ARGUMENT_EXTRAS = "_status";
	
	private int position = 0;
	private ImageView imageView;
	private ExtendedLinearLayout mPanelView;
	
	public static NewsFragment newInstance(int position) {
		NewsFragment fragment = new NewsFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(FRAGMENT_ARGUMENT_EXTRAS, position);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		Bundle bundle = getArguments();
		if (bundle != null) {
			position = bundle.getInt(FRAGMENT_ARGUMENT_EXTRAS);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = null;
		if (position == 0) {
			view = inflater.inflate(R.layout.news_main_layout, null);
		} else if (position == 1) {
			view = inflater.inflate(R.layout.news_sub_layout, null);
		}
		imageView = (ImageView)view.findViewById(R.id.imageView);
		mPanelView = (ExtendedLinearLayout)view.findViewById(R.id.container);
		mPanelView.setOnItemsClickListener(this);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		onRefresh(CacheUtil.getNewsCache());
	}

	@Override
	public void hideView(boolean isShown) {
		imageView.setVisibility(isShown?View.VISIBLE:View.GONE);
	}
	
	@Override
	public void onRefresh(List<News> result) {
		if (result == null || result.size() == 0)
			return;
		List<News> listNews = new ArrayList<News>();
		for (News news : result) {
			if (news.getPage_no()==(position+1)) {
				listNews.add(news);
			}
		}
		mPanelView.setItems(listNews);
	}
	
	@Override
	public void onClick(View v) {
		if (v.getTag() != null) {
			Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
			intent.putExtra(Constants.NEWS_DETAIL_EXTRAS, (News)v.getTag());
			startActivity(intent);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mPanelView.removeObservers();
	}
}
