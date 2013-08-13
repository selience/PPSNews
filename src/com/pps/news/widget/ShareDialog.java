package com.pps.news.widget;

import java.util.ArrayList;
import java.util.List;
import com.pps.news.R;
import com.pps.news.app.NewsApplication;
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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
	private String content; // 分享的内容
	private IWXAPI api; // 微信API
	private List<AppInfo> appList;

	public ShareDialog(Context context, String content) {
		this(context, R.style.Theme_Dialog, content);
	}

	public ShareDialog(Context context, int theme, String content) {
		super(context, theme);
		this.context = context;
		this.content = content;
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
		appList = getShareApplist(newShareIntent(content));
		AppListAdapter mAdapter = new AppListAdapter(context, appList);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(this);
	}

	private Intent newShareIntent(String content) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, content);
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		return intent;
	}

	private AppInfo newAppItem(int id, String label, ResolveInfo it) {
		AppInfo appItem = new AppInfo();
		appItem.id = id;
		appItem.name = it.activityInfo.name;
		appItem.label = label;
		appItem.packageName = it.activityInfo.packageName;
		appItem.drawable = it.activityInfo.loadIcon(getContext().getPackageManager());
		return appItem;
	}
	
	private List<AppInfo> getShareApplist(Intent intent) {
		PackageManager pm = getContext().getPackageManager();
		List<AppInfo> list = new ArrayList<ShareDialog.AppInfo>();
		List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);

		String packageName = null;
		for (ResolveInfo it : activities) {
			packageName = it.activityInfo.packageName;
			if (packageName.equals("com.sina.weibo")) {
				list.add(newAppItem(SHARE_SINA_WEIBO,"新浪微博",  it));
			}
			else if (packageName.equals("com.tencent.WBlog")) {
				list.add(newAppItem(SHARE_TENCENT_WEIBO,"腾讯微博", it));
			}
			else if (packageName.equals("com.tencent.mobileqq")) {
				list.add(newAppItem(SHARE_QQ,"QQ", it));
			}
			else if (packageName.equals("com.qzone")) {
				list.add(newAppItem(SHARE_QZONE,"QQ空间", it));
			}
			else if (packageName.equals("com.renren.mobile.android")) {
				list.add(newAppItem(SHARE_RENREN,"人人网", it));
			}
			else if (packageName.equals("com.android.mms")) {
				list.add(newAppItem(SHARE_MESSAGE,"信息", it));
			}
			else if (packageName.equals("com.android.email")) {
				list.add(newAppItem(SHARE_EMAIL,"邮件", it));
			} else if (packageName.equals("com.tencent.mm")) {
				list.add(newAppItem(SHARE_WEIXIN,"微信", it));
				if (WXShare.checkWXSupportAPI(api)) { 
					AppInfo appItem = newAppItem(SHARE_WEIXIN_TIMELINE,"微信朋友圈", it);
					appItem.drawable = mRes.getDrawable(R.drawable.sns_weixin_timeline_icon);
					list.add(appItem);
				}
			}
		}
		
		return list;
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		try {
			AppInfo appItem = appList.get(position);
			if (appItem.id == SHARE_WEIXIN) {
				WXShare.sendTextToWX(api, content, false);  // 分享给指定好友
			} else if (appItem.id == SHARE_WEIXIN_TIMELINE) { 
				WXShare.sendTextToWX(api, content, true); // 分享到朋友圈
			} else {
				final Intent resolvedIntent = newShareIntent(content);
		        resolvedIntent.setComponent(new ComponentName(appItem.packageName, appItem.name));
		        resolvedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        getContext().startActivity(resolvedIntent);
			}
			dismiss();
		} catch (Exception e) {
		}
	}
	

	static class AppListAdapter extends BaseAdapter { 

		private List<AppInfo> mList;
		private LayoutInflater mInflater;
		
		public AppListAdapter(Context context, List<AppInfo> infos) {
			mInflater=LayoutInflater.from(context);
			this.mList=infos;
		}
		
		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.share_app_list_item, null);
				holder.imageView=(ImageView)convertView.findViewById(R.id.imageView);
				holder.label=(TextView)convertView.findViewById(R.id.textView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			AppInfo appItem = mList.get(position);
			if (appItem != null) {
				holder.label.setText(appItem.label);
				holder.imageView.setImageDrawable(appItem.drawable);
			}
			return convertView;
		}

		static class ViewHolder {
			ImageView imageView;
			TextView label;
		}	
	}
	
	static class AppInfo {
		int id;
		String name;
		String label;
		String packageName;
		Drawable drawable;
	}
}
