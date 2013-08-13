package com.pps.news;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.WeakHashMap;
import tv.pps.module.player.VideoInit;
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
import com.pps.news.bean.Comment;
import com.pps.news.bean.Group;
import com.pps.news.bean.News;
import com.pps.news.bean.Result;
import com.pps.news.bean.Weather;
import com.pps.news.constant.Constants;
import com.pps.news.constant.PreferenceUtils;
import com.pps.news.fragment.NewsFragment;
import com.pps.news.location.PPSNewsLocation;
import com.pps.news.task.GetNewsListTask;
import com.pps.news.task.GetUserCommentTask;
import com.pps.news.task.GetWeatherTask;
import com.pps.news.task.TaskListener;
import com.pps.news.util.CacheUtil;
import com.pps.news.util.ImageCache;
import com.pps.news.weather.WeatherActivity;
import com.pps.news.widget.BaseAlertDialog;
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
import android.text.TextUtils;
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
	private static final String GET_NEWS_LIST_TASK = "GET_NEWS_LIST_TASK";
	private static final String GET_SELF_COMMENT_TASK = "GET_SELF_COMMENT_TASK";
	private static final String GET_WEATHER_TASK = "GET_WEATHER_TASK";
	
	private View container;
	private ImageView iconAvatar;
	private TextView userView;
	private TextView summaryView;
	private ImageView slideMenu;
	private ImageView weatherImage;
	private TextView weatherCity;
	private TextView weatherTemperature;
	private TextView commentNumView;
	private SlideNavigationView navigationView;
	private VerticalViewPager viewPager;
	private PullToRefreshVerticalViewPager mPullToRefreshViewPager;
	
	private String city;
	private ImageCache imageFetcher;
	private Map<String, ImageView> mPhotoMaps;
	private SparseArray<String> userVipType;
	private SparseArray<Fragment> registeredFragments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ensureUi();
		
		userVipType = new SparseArray<String>(3);
		userVipType.put(Integer.parseInt(AccountVerify.OPT_NORMAL), "普通会员");
		userVipType.put(Integer.parseInt(AccountVerify.OPT_SILVER), "白银会员");
		userVipType.put(Integer.parseInt(AccountVerify.OPT_GOLD), "黄金会员");
		
		mPhotoMaps=new WeakHashMap<String, ImageView>(2);
		imageFetcher = ImageCache.getInstance();
		imageFetcher.addObserver(mObserver);
		
		showWeather();
		
		VideoInit.getInstance().init(this);
		VideoInit.getInstance().setLibDebug(false);
	}

	private void ensureUi() {
		navigationView = new SlideNavigationView(this);
		navigationView.setContent(R.layout.main);
		navigationView.setMenu(R.layout.news_nav_item);
		navigationView.setBehindOffsetRes(R.dimen.news_nav_offset);
		navigationView.setMode(SlideNavigationView.RIGHT);
		navigationView.setShadowWidthRes(R.dimen.slide_shadow_width);
		navigationView.setShadowDrawable(R.drawable.slide_shadow_right);
		navigationView.setTouchModeAbove(SlideNavigationView.TOUCHMODE_FULLSCREEN);
		setContentView(navigationView);

		container = findViewById(R.id.container);
		iconAvatar = (ImageView)findViewById(R.id.icon_avatar);
		userView = (TextView)findViewById(R.id.nav_login_label);
		summaryView = (TextView)findViewById(R.id.nav_sub_desc);
		commentNumView = (TextView)findViewById(R.id.commentNum);
		weatherImage = (ImageView)findViewById(R.id.weatherImage);
		weatherCity = (TextView)findViewById(R.id.weatherCity);
		weatherTemperature=(TextView)findViewById(R.id.weatherTemperature);
		slideMenu = (ImageView) findViewById(R.id.slide_menu);
		slideMenu.setOnClickListener(this);
		findViewById(R.id.setting).setOnClickListener(this);
		findViewById(R.id.comment).setOnClickListener(this);
		findViewById(R.id.offline).setOnClickListener(this);
		findViewById(R.id.rl_alarm).setOnClickListener(this);
		findViewById(R.id.weather).setOnClickListener(this);
		findViewById(R.id.ic_alarm).setOnClickListener(this);
		findViewById(R.id.weatherImage).setOnClickListener(this);
		
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
			container.setOnClickListener(mUserLogoutListener); // 获取评论总数目
			new GetUserCommentTask(this, GET_SELF_COMMENT_TASK).execute("1", Constants.COMMENTS_LIST_PAGESIZE);
		} else {
			setCommentSize(0); // 未登录状态，评论数目为0
			userView.setText(R.string.nav_login_lable);
			iconAvatar.setImageResource(R.drawable.ic_avatar);
			summaryView.setText(R.string.nav_sub_desc);
			container.setOnClickListener(this);
		}
	}
	
	// 获取天气信息
	// 获取当前位置信息，如果为null，则采用上次的位置;
	private void showWeather() {
		city = PPSNewsLocation.myLocation.getCity();
		if (TextUtils.isEmpty(city)) {
			city = PreferenceUtils.getWeatherCity(this);
			if (TextUtils.isEmpty(city)) {
				city = "上海";
			}
		} else {
			StringBuilder sb = new StringBuilder(city);
			if (city.contains("市")) {
				int position = sb.indexOf("市");
				sb.deleteCharAt(position);
			}
			city = sb.toString();
		}
		setWeatherInfo();
		new GetWeatherTask(this, GET_WEATHER_TASK).execute(city);
	}

	private void setWeatherInfo() { // 设置默认天气
		String icon = PreferenceUtils.getWeatherIcon(this);
		String temper = PreferenceUtils.getWeatherTemper(this);
		weatherCity.setText(city);
		weatherTemperature.setText(temper==null?"未知":temper);
		if (icon != null) {
			setPhotos(getWeatherIconUrl(icon), weatherImage);
		} else {
			weatherImage.setImageResource(R.drawable.weather_big_error);
		}
	}
	
	// 设置评论数目
	private void setCommentSize(int totalSize) {
		commentNumView.setText(String.valueOf(totalSize));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		checkIfUserLogin();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mPhotoMaps.clear();
		userVipType.clear();
		registeredFragments.clear();
		imageFetcher.deleteObservers();
		VideoInit.getInstance().destory();
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
			if (AccountVerify.getInstance().isLogin()) {
				startActivity(new Intent(this, CommentActivity.class));
			} else {
				new BaseAlertDialog(this)
				.setMessage(R.string.view_comment_tips)
				.setPositiveClickListener(new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						startActivity(new Intent(NewsActivity.this, LoginActivity.class));	
					}
				}).show();
			}
			break;
		case R.id.offline:
			startActivity(new Intent(this, DownloadActivity.class));
			break;
		case R.id.setting:
			startActivity(new Intent(this, SettingActivity.class));
			break;
		case R.id.rl_alarm:
		case R.id.ic_alarm:
			startActivity(new Intent(this, AlarmActivity.class));
			break;
		case R.id.weather:
		case R.id.weatherImage:
			Intent intent = new Intent(this, WeatherActivity.class);
			intent.putExtra("city", city);
			startActivity(intent);
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
		if (taskName.equals(GET_NEWS_LIST_TASK)) {
			handlerException(result.getException());
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
		} else if (taskName.equals(GET_SELF_COMMENT_TASK)) {
			if (result.getValue()!=null) {
				Group<Comment> comments = (Group<Comment>)result.getValue();
				setCommentSize(comments.getTotal()); // 评论总数目
			}
		} else if (taskName.equals(GET_WEATHER_TASK)) {
			weatherCity.setText(city);
			if (result.getValue()!=null) {
				Weather weather = (Weather) result.getValue();
				weatherTemperature.setText(weather.getTempInfo());
				setPhotos(getWeatherIconUrl(weather.getImg()), weatherImage);
				PreferenceUtils.storeWeather(this, city, weather);
			}
		}
	}

	@Override
	public void onRefresh(PullToRefreshBase<VerticalViewPager> refreshView) {
		setLastUpdateTimeStamp();
		new GetNewsListTask(this, GET_NEWS_LIST_TASK).execute();
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
	
	
	// 设置用户图片
	private void setPhotos(String photoUrl, ImageView imageView) {
		if (imageFetcher.exists(photoUrl)) {
			Bitmap bitmap = imageFetcher.displayBitmap(photoUrl);
			imageView.setImageBitmap(bitmap);
		} else {
			mPhotoMaps.put(photoUrl, imageView);
			imageFetcher.request(photoUrl);
		}
	}
	
	private Observer mObserver = new Observer() {
		
		@Override
		public void update(Observable observable, Object data) {
			final String imageUrl = data.toString();
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					for (String key : mPhotoMaps.keySet()) {
						if (key.equals(imageUrl)) {
							setPhotos(key, mPhotoMaps.get(key));
						}
					}
				}
			});
		}
	};

	// 注销用户
	private OnClickListener mUserLogoutListener = new OnClickListener() {
		
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
		PreferenceUtils.clearUser(); // 清空本地存储状态
		AccountVerify.getInstance().setmLogin(false);
		AccountVerify.getInstance().setmUID(null);
		checkIfUserLogin();
	}

	@Override
	public void onRequestLoading(long current, long total) {
	}

	@Override
	public void onCancel() {
	}
	
	// 获取天气的图标
	private String getWeatherIconUrl(String imgId) {
		return "http://image1.webscache.com/vodguide/style/img/mobile/android/w"+ imgId + ".png";
	}
	
	public static interface OnRefreshItemListener {
		
		void hideView(boolean isShown);
		
		void onRefresh(List<News> result);
	}
}
