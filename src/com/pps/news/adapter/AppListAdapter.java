package com.pps.news.adapter;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.pps.news.R;
import com.pps.news.bean.AppInfo;

public class AppListAdapter extends BaseAdapter {

	private List<AppInfo> mList;
	private Context context;

	public AppListAdapter(Context context, List<AppInfo> infos) {
		this.mList = infos;
		this.context = context;
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
			convertView = View.inflate(context, R.layout.share_app_list_item, null);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.imageView);
			holder.label = (TextView) convertView.findViewById(R.id.textView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		AppInfo appItem = mList.get(position);
		if (appItem != null) {
			holder.label.setText(appItem.getLabel());
			holder.imageView.setImageDrawable(appItem.getDrawable());
		}
		return convertView;
	}

	static class ViewHolder {
		ImageView imageView;
		TextView label;
	}
}
