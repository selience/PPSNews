package com.pps.news;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import tv.pps.vipmodule.vip.AccountVerify;
import tv.pps.vipmodule.vip.protol.ProtocolLogout;
import tv.pps.vipmodule.vip.protol.BaseProtocol.RequestCallBack;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshVerticalViewPager;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.pps.news.app.BaseActivity;
import com.pps.news.bean.Group;
import com.pps.news.bean.News;
import com.pps.news.bean.Result;
import com.pps.news.constant.PreferenceUtils;
import com.pps.news.fragment.NewsFragment;
import com.pps.news.task.GetNewsListTask;
import com.pps.news.task.TaskListener;
import com.pps.news.util.CacheUtil;
import com.pps.news.util.ImageCache;
import com.pps.news.widget.LogoutPopupWindow;
import com.pps.news.widget.SlideNavigationView;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.TextView;

/**
 * @file MainActivity.java
 * @create 2013-6-4 下午03:45:41
 * @author lilong
 * @description TODO 新闻主界面
 */
public class NewsActivity extends BaseActivity implements OnClickListener, TaskListener, OnPageChangeListener,
	OnRefreshListener<VerticalViewPager>, OnPullEventListener<VerticalViewPager>, RequestCallBack<Boolean> {

	private View container;
	private ImageView iconAvatar;
	private TextView userView;
	private TextView summaryView;
	private ImageView slideMenu;
	private SlideNavigationView navigationView;
	private VerticalViewPager viewPager;
	private PullToRefreshVerticalViewPager mPullToRefreshViewPager;
	
	private String avatarUrl;
	private ImageCache imageFetcher;
	private SparseArray<String> userVipType;
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
		container = findViewById(R.id.container);
		iconAvatar = (ImageView)findViewById(R.id.icon_avatar);
		userView = (TextView)findViewById(R.id.nav_login_label);
		summaryView = (TextView)findViewById(R.id.nav_sub_desc);
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
		
		imageFetcher = ImageCache.getInstance();
		imageFetcher.addObserver(mObserver);
		
		userVipType = new SparseArray<String>(3);
		userVipType.put(Integer.parseInt(AccountVerify.OPT_NORMAL), "普通会员");
		userVipType.put(Integer.parseInt(AccountVerify.OPT_SILVER), "白银会员");
		userVipType.put(Integer.parseInt(AccountVerify.OPT_GOLD), "黄金会员");
	}

	// 更新上次刷新时间戳
	private void setLastUpdateTimeStamp() {
		long lastUpdateTimestamp = PreferenceUtils.getLastUpdateTimeStamp(this);
		if (lastUpdateTimestamp > 0) {
			CharSequence dateString = DateFormat.format("kk:mm", lastUpdateTimestamp);
			mPullToRefreshViewPager.getLoadingLayoutProxy().setLastUpdatedLabel("更新时间:"+dateString);
		}
	}
	
	// 检查用户是否登录
	private void checkIfUserLogin() {
		AccountVerify accountVerify = AccountVerify.getInstance();
		if (accountVerify.isLogin()) {
			userView.setText(accountVerify.getmDisplayName());
			setPhotos(accountVerify.getmIconUrl(), iconAvatar);
			String userStatus = userVipType.get(Integer.parseInt(accountVerify.getVipType()));
			String summary = getString(R.string.nav_sub_summary, userStatus, accountVerify.getmLevel());
			summaryView.setText(summary);
			container.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					LogoutPopupWindow popupWindow = new LogoutPopupWindow(NewsActivity.this);
					popupWindow.showPopupWindow(container);
					popupWindow.setPositiveClickListener(new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							ProtocolLogout protocolLogout = new ProtocolLogout(NewsActivity.this);
							protocolLogout.fetch(NewsActivity.this);
						}
					});
				}
			});
		} else {
			userView.setText(R.string.nav_login_lable);
			iconAvatar.setImageResource(R.drawable.ic_avatar);
			summaryView.setText(R.string.nav_sub_desc);
			container.setOnClickListener(this);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		checkIfUserLogin();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		registeredFragments.clear();
		registeredFragments=null;
		imageFetcher.deleteObservers();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.slide_menu:
			navigationView.toggle(true);
			break;
		case R.id.container:
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
		PreferenceUtils.saveLastUpdateTimeStamp(this, System.currentTimeMillis());
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
	
	class NewsFragmentAdapter extends FragmentStatePagerAdapter {
		public NewsFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return NewsFragment.newInstance(position);
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
	
	// 设置用户图片
	private void setPhotos(String photoUrl, ImageView imageView) {
		this.avatarUrl = photoUrl;
		if (imageFetcher.exists(photoUrl)) {
			Bitmap bitmap = imageFetcher.displayBitmap(photoUrl);
			imageView.setImageBitmap(bitmap);
		} else {
			imageView.setImageResource(R.drawable.ic_avatar);
			imageFetcher.request(photoUrl);
		}
	}
	
	private Observer mObserver = new Observer() {
		
		@Override
		public void update(Observable observable, Object data) {
			final String imageUrl = data.toString();
			if (avatarUrl.equals(imageUrl)) {
				runOnUiThread(new Runnable() {
					public void run() {
						setPhotos(imageUrl, iconAvatar);
					}
				});
			}
		}
	};

	
	@Override
	public void onRequestStart() {
	}

	@Override
	public void onRequestError(String errorCode, String errorMessage) {
	}

	// 注销用户成功
	@Override
	public void onRequestSuccess(Boolean formData, String successMessage) {
		PreferenceUtils.clearUser(); // 清空登陆状态
		AccountVerify.getInstance().setmLogin(false);
		checkIfUserLogin();
	}

	@Override
	public void onRequestLoading(long current, long total) {
	}

	@Override
	public void onCancel() {
	}
}
