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
import com.pps.news.util.CacheUtil;
import com.pps.news.widget.ExtendedMainLayout;

public class NewsFragment extends BaseFragment implements OnClickListener, OnRefreshItemListener {
	private int pageNo = 1;

	public static NewsFragment newInstance(int pageNum) {
		NewsFragment fragment = new NewsFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("pageNo", pageNum);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	private ImageView imageView;
	private ExtendedMainLayout mPanel;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.news_main_layout, null);
		imageView = (ImageView) view.findViewById(R.id.imageView);
		mPanel = (ExtendedMainLayout) view.findViewById(R.id.container);
		mPanel.setOnItemsClickListener(this);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			pageNo = bundle.getInt("pageNo");
		}
		
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
			if (news.getPage_no() == pageNo) {
				listNews.add(news);
			}
		}
		mPanel.setItems(listNews);
	}
	
	@Override
	public void onClick(View v) {
		if (v.getTag() != null) {
			Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
			intent.putExtra("news", (News)v.getTag());
			startActivity(intent);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mPanel.removeObservers();
	}

}
