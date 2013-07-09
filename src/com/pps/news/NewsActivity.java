package com.pps.news;

import java.util.List;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshVerticalViewPager;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.pps.news.app.BaseActivity;
import com.pps.news.app.NewsApplication;
import com.pps.news.bean.Group;
import com.pps.news.bean.News;
import com.pps.news.bean.Result;
import com.pps.news.constant.Constants;
import com.pps.news.constant.PreferenceUtils;
import com.pps.news.fragment.MainFragment;
import com.pps.news.fragment.NewsFragment;
import com.pps.news.network.BetterHttp;
import com.pps.news.parser.NewsParser;
import com.pps.news.task.GenericTask;
import com.pps.news.task.TaskListener;
import com.pps.news.util.CacheUtil;
import com.pps.news.widget.SlideNavigationView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.VerticalViewPager;
import android.support.v4.view.VerticalViewPager.OnPageChangeListener;
import android.text.format.DateFormat;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * @file MainActivity.java
 * @create 2013-6-4 下午03:45:41
 * @author lilong
 * @description TODO 新闻主界面
 */
public class NewsActivity extends BaseActivity implements OnClickListener, TaskListener, OnPageChangeListener,
	OnRefreshListener<VerticalViewPager>, OnPullEventListener<VerticalViewPager> {
	
	private ImageView slideMenu;
	private SlideNavigationView navigationView;
	private VerticalViewPager viewPager;
	private PullToRefreshVerticalViewPager mPullToRefreshViewPager;
	
	private SparseArray<Fragment> registeredFragments;

	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		navigationView = new SlideNavigationView(this);
		navigationView.setContent(R.layout.main);
		navigationView.setMenu(R.layout.news_nav_item);
		navigationView.setBehindOffsetRes(R.dimen.news_nav_offset);
		navigationView.setMode(SlideNavigationView.RIGHT);
		navigationView.setShadowWidthRes(R.dimen.slide_shadow_width);
		navigationView.setShadowDrawable(R.drawable.slide_shadow_right);
		navigationView.setTouchModeAbove(SlideNavigationView.TOUCHMODE_FULLSCREEN);
		setContentView(navigationView);

		findViewById(R.id.setting).setOnClickListener(this);
		findViewById(R.id.comment).setOnClickListener(this);
		findViewById(R.id.offline).setOnClickListener(this);
		findViewById(R.id.rl_alarm).setOnClickListener(this);
		findViewById(R.id.rl_weather).setOnClickListener(this);
		findViewById(R.id.icon_avatar).setOnClickListener(this);
		slideMenu = (ImageView) findViewById(R.id.slide_menu);
		slideMenu.setOnClickListener(this);
		
		mPullToRefreshViewPager = (PullToRefreshVerticalViewPager) findViewById(R.id.pull_refresh_vertical_viewpager);
		mPullToRefreshViewPager.setOnRefreshListener(this);
		mPullToRefreshViewPager.setOnPullEventListener(this);
		mPullToRefreshViewPager.setRefreshing(false);
		mPullToRefreshViewPager.setScrollingWhileRefreshingEnabled(false);
		setLastUpdateTimeStamp();
		
		registeredFragments = new SparseArray<Fragment>(2);
		viewPager = mPullToRefreshViewPager.getRefreshableView();
		viewPager.setAdapter(new NewsFragmentAdapter(getSupportFragmentManager()));
		viewPager.setOnPageChangeListener(this);
	}

	private void setLastUpdateTimeStamp() {
		long lastUpdateTimestamp = PreferenceUtils.getLastUpdateTimeStamp(NewsApplication.mPrefs);
		if (lastUpdateTimestamp > 0) {
			CharSequence dateString = DateFormat.format("kk:mm", lastUpdateTimestamp);
			mPullToRefreshViewPager.getLoadingLayoutProxy().setLastUpdatedLabel("更新时间:"+dateString);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.slide_menu:
			navigationView.toggle(true);
			break;
		case R.id.icon_avatar:
			startActivity(new Intent(this, LoginActivity.class));
			break;
		case R.id.comment:
			startActivity(new Intent(this, CommentActivity.class));
			break;
		case R.id.offline:
			startActivity(new Intent(this, DownloadActivity.class));
			break;
		case R.id.setting:
			startActivity(new Intent(this, SettingActivity.class));
			break;
		case R.id.rl_alarm:
			startActivity(new Intent(this, AlarmActivity.class));
			break;
		case R.id.rl_weather:
			startActivity(new Intent(this, AlarmAlertFullScreen.class));
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK && navigationView.isMenuShowing()) {
			navigationView.showContent(true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onTaskStart(String taskName) {
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onTaskFinished(String taskName, Result result) {
		notifyErrorMessage(result.getException());
		mPullToRefreshViewPager.onRefreshComplete();
		PreferenceUtils.saveLastUpdateTimeStamp(NewsApplication.mPrefs, System.currentTimeMillis());
		if (result.getValue() != null) {
			Group<News> listNews = (Group<News>)result.getValue();
			for (int i=0;i<registeredFragments.size();i++) {
				Fragment fragment = registeredFragments.get(i);
				if (fragment instanceof OnRefreshItemListener) {
					((OnRefreshItemListener)fragment).onRefresh(listNews);
				}
			}
			CacheUtil.saveNewsCache(listNews);
		}
	}

	
	
	@Override
	public void onRefresh(PullToRefreshBase<VerticalViewPager> refreshView) {
		setLastUpdateTimeStamp();
		new GetNewsListTask(this, null).execute();
	}
	
	@Override
	public void onPullEvent(PullToRefreshBase<VerticalViewPager> refreshView, State state, Mode direction) {
		if (state == State.PULL_TO_REFRESH) {
			slideMenu.setVisibility(View.INVISIBLE);
		} else if (state == State.RESET) {
			refreshView.postDelayed(new Runnable() {
				@Override
				public void run() {
					slideMenu.setVisibility(View.VISIBLE);
				}
			}, 200L);
		} else if (state == State.RELEASE_TO_REFRESH) {
		}
	}
	
	
	class GetNewsListTask extends GenericTask {

		public GetNewsListTask(TaskListener taskListener, String taskName) {
			super(taskListener, taskName);
		}

		@Override
		protected Result doInBackground(String... params) {
			BetterHttp httpClient = BetterHttp.getInstance();
			Result result = httpClient.doHttpGet(Constants.getNewsList());
			Group<News> listNews = new NewsParser().parse(result.getMessage());
			result.addResult(listNews);
			return result;
		}
	}
	
	class NewsFragmentAdapter extends FragmentStatePagerAdapter {
		public NewsFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0)
				return NewsFragment.newInstance(position+1);
			else 
				return MainFragment.newInstance(position+1);
		}

		@Override
		public int getCount() {
			return 2;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Fragment fragment = (Fragment) super.instantiateItem(container, position);
			registeredFragments.put(position, fragment);
			return fragment;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			registeredFragments.remove(position);
			super.destroyItem(container, position, object);
		}
	}

	
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		for (int i=0;i<registeredFragments.size();i++) {
			Fragment fragment = registeredFragments.get(i);
			if (fragment instanceof OnRefreshItemListener) {
				((OnRefreshItemListener)fragment).hideView(i==position);
			}
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}
	
	
	public static interface OnRefreshItemListener {
		
		void hideView(boolean isShown);
		
		void onRefresh(List<News> result);
	}
}
