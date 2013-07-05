package com.pps.news.fragment;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.pps.news.NewsDetailActivity;
import com.pps.news.NewsActivity.OnRefreshItemListener;
import com.pps.news.app.BaseFragment;
import com.pps.news.bean.News;
import com.pps.news.util.CacheUtil;
import com.pps.news.widget.ExtendedSubLayout;

public class MainFragment extends BaseFragment implements OnClickListener, OnRefreshItemListener {
	private int pageNo = 1;

	private ExtendedSubLayout mPanel;
	
	public static MainFragment newInstance(int pageNum) {
		MainFragment fragment = new MainFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("pageNo", pageNum);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mPanel = new ExtendedSubLayout(getActivity());
		mPanel.setOnItemsClickListener(this);
		return mPanel;
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
