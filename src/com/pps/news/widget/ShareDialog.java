package com.pps.news.widget;

import java.util.ArrayList;
import java.util.List;
import com.pps.news.R;
import com.pps.news.util.UIUtil;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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

	private Context context;
	private String content; // 分享的内容
	private List<AppInfo> appList;

	public ShareDialog(Context context, String content) {
		this(context, R.style.Theme_Dialog, content);
	}

	public ShareDialog(Context context, int theme, String content) {
		super(context, theme);
		this.context = context;
		this.content = content;
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

	private List<AppInfo> getShareApplist(Intent intent) {
		PackageManager packageManager = getContext().getPackageManager();
		List<AppInfo> list = new ArrayList<ShareDialog.AppInfo>();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(
				intent, 0);
		
		String packageName = null;
		for (ResolveInfo it : activities) {
			packageName = it.activityInfo.packageName;
			if (packageName.equals("com.sina.weibo")) {
				list.add(new AppInfo("新浪微博", it));
			}
			else if (packageName.equals("com.tencent.WBlog")) {
				appList.add(new AppInfo("腾讯微博", it));
			}
			else if (packageName.equals("com.tencent.mm")) {
				list.add(new AppInfo("微信", it));
			}
			else if (packageName.equals("com.tencent.mobileqq")) {
				list.add(new AppInfo("QQ", it));
			}
			else if (packageName.equals("com.qzone")) {
				list.add(new AppInfo("QQ空间", it));
			}
			else if (packageName.equals("com.renren.mobile.android")) {
				list.add(new AppInfo("人人网", it));
			}
			else if (packageName.equals("com.android.mms")) {
				list.add(new AppInfo("信息", it));
			}
			else if (packageName.equals("com.android.email")) {
				list.add(new AppInfo("邮件", it));
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
			final Intent resolvedIntent = new Intent(newShareIntent(content));
	        ActivityInfo ai = appItem.resolveInfo.activityInfo;
	        resolvedIntent.setComponent(new ComponentName(ai.applicationInfo.packageName, ai.name));
	        resolvedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        getContext().startActivity(resolvedIntent);
	        dismiss();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	

	static class AppListAdapter extends BaseAdapter {

		private List<AppInfo> mList;
		private LayoutInflater mInflater;
		private PackageManager packageManager;
		
		public AppListAdapter(Context context, List<AppInfo> infos) {
			mInflater=LayoutInflater.from(context);
			this.mList=infos;
			packageManager=context.getPackageManager();
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
				holder.imageView.setImageDrawable(appItem.resolveInfo.loadIcon(packageManager));
			}
			return convertView;
		}

		static class ViewHolder {
			ImageView imageView;
			TextView label;
		}	
	}
	
	class AppInfo {
		
		String label;
		
		ResolveInfo resolveInfo;
		
		public AppInfo(String label, ResolveInfo info) {
			this.label = label;
			this.resolveInfo = info;
		}
	}
}
