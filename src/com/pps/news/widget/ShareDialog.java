package com.pps.news.widget;

import java.util.ArrayList;
import java.util.List;
import com.pps.news.R;
import com.pps.news.adapter.AppListAdapter;
import com.pps.news.app.NewsApplication;
import com.pps.news.bean.AppInfo;
import com.pps.news.bean.News;
import com.pps.news.common.WXShare;
import com.pps.news.util.UIUtil;
import com.tencent.mm.sdk.openapi.IWXAPI;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShareDialog extends Dialog implements OnItemClickListener{

	private static final int SHARE_QQ = 0;	// QQ
	private static final int SHARE_QZONE = 1;	// QQ空间
	private static final int SHARE_RENREN = 2;	// 人人网
	private static final int SHARE_MESSAGE = 3;	// 短信
	private static final int SHARE_EMAIL = 4;	// 邮件
	private static final int SHARE_WEIXIN = 5;	// 微信
	private static final int SHARE_WEIXIN_TIMELINE = 6;	// 朋友圈
	private static final int SHARE_SINA_WEIBO = 7;	// 新浪微博
	private static final int SHARE_TENCENT_WEIBO = 8;	// 腾讯微博
	
	private Context context;
	private Resources mRes;
	private News itemNews;
	private IWXAPI api; // 微信API

	public ShareDialog(Context context, News news) {
		this(context, R.style.Theme_Dialog, news);
	}

	public ShareDialog(Context context, int theme, News news) {
		super(context, theme);
		this.context = context;
		this.itemNews = news;
		this.mRes = context.getResources();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.week_dialog_layout);
		findViewById(R.id.icon).setVisibility(View.GONE);
		((TextView) findViewById(R.id.summary)).setText("Share");
		((TextView) findViewById(R.id.title)).setText("新闻分享");
		setLayoutParams();
		ListView listView = (ListView) findViewById(android.R.id.list);
		api = NewsApplication.getInstance().getWXAPI();
		List<AppInfo> appList = getShareApplist();
		addSupportToWeiXin(appList);
		AppListAdapter mAdapter = new AppListAdapter(context, appList);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(this);
	}

	// 设置布局大小
	private void setLayoutParams() {
		int width = UIUtil.getScreenWidth(context) - UIUtil.dip2px(context, 40);
		int height = UIUtil.getScreenHeight(context) - UIUtil.dip2px(context, 160);

		RelativeLayout rl = (RelativeLayout) findViewById(R.id.dialog);
		ViewGroup.LayoutParams lp = rl.getLayoutParams();
		if (lp == null) {
			lp = new ViewGroup.LayoutParams(width, height);
		} else {
			lp.width = width;
			lp.height = height;
		}
		rl.setLayoutParams(lp);
	}
	
	private List<AppInfo> getShareApplist() {
		PackageManager pm = context.getPackageManager();
		List<AppInfo> list = new ArrayList<AppInfo>();
		List<ResolveInfo> activities = pm.queryIntentActivities(newShareIntent(), 0);

		String packageName = null;
		for (ResolveInfo it : activities) {
			packageName = it.activityInfo.packageName;
			if (packageName.equals("com.sina.weibo")) {
				list.add(new AppInfo(SHARE_SINA_WEIBO,"新浪微博",it.loadIcon(pm), it));
			}
			else if (packageName.equals("com.tencent.WBlog")) {
				list.add(new AppInfo(SHARE_TENCENT_WEIBO,"腾讯微博",it.loadIcon(pm), it));
			}
			else if (packageName.equals("com.tencent.mobileqq")) {
				list.add(new AppInfo(SHARE_QQ,"QQ",it.loadIcon(pm),  it));
			}
			else if (packageName.equals("com.qzone")) {
				list.add(new AppInfo(SHARE_QZONE,"QQ空间",it.loadIcon(pm),  it));
			}
			else if (packageName.equals("com.renren.mobile.android")) {
				list.add(new AppInfo(SHARE_RENREN,"人人网",it.loadIcon(pm),  it));
			}
			else if (packageName.equals("com.android.mms")) {
				list.add(new AppInfo(SHARE_MESSAGE,"信息",it.loadIcon(pm),  it));
			}
			else if (packageName.equals("com.android.email")) {
				list.add(new AppInfo(SHARE_EMAIL,"邮件",it.loadIcon(pm),  it));
			} 
		}
		
		return list;
	}
	
	// 添加微信分享支持
	private void addSupportToWeiXin(List<AppInfo> appList) {
		// 检测是否安装微信客户端
		if (WXShare.checkWXInstalled(api)) {
			AppInfo appItem = new AppInfo();
			appItem.setId(SHARE_WEIXIN);
			appItem.setLabel("微信");
			appItem.setDrawable(mRes.getDrawable(R.drawable.sns_weixin_icon));
			appList.add(appItem);
			
			// 4.2以上版本支持发送到朋友圈
			if (WXShare.checkWXSupportTimeline(api)) { 
				appItem = new AppInfo();
				appItem.setId(SHARE_WEIXIN_TIMELINE);
				appItem.setLabel("微信朋友圈");
				appItem.setDrawable(mRes.getDrawable(R.drawable.sns_weixin_timeline_icon));
				appList.add(appItem);
			}
		}
	}

	private Intent newShareIntent() {
		String content = itemNews.getDesc_title() + itemNews.getLink_url();
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, itemNews.getMain_title());
		intent.putExtra(Intent.EXTRA_TEXT, content);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		return intent;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		AppInfo appItem = (AppInfo) parent.getItemAtPosition(position);
		String description = itemNews.getDesc_title() + itemNews.getLink_url();
		if (appItem.getId() == SHARE_WEIXIN) {
			// 分享给指定好友
			WXShare.sendTextToWX(api, description, false);
		} else if (appItem.getId() == SHARE_WEIXIN_TIMELINE) { 
			// 分享到朋友圈
			WXShare.sendTextToWX(api, description, true);
		} else {
			final Intent resolvedIntent = newShareIntent();
	        resolvedIntent.setComponent(new ComponentName(appItem.getPackageName(), appItem.getName()));
	        resolvedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        getContext().startActivity(resolvedIntent);
	        dismiss();
		}
	}
}
