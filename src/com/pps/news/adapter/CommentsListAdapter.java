package com.pps.news.adapter;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.WeakHashMap;
import com.pps.news.R;
import com.pps.news.bean.Comment;
import com.pps.news.util.ImageCache;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentsListAdapter extends BaseAdapter implements Observer {

	private Context context;;
	private List<Comment> comments;
	private ImageCache imageFetcher;
	private SparseBooleanArray dataSet;
	private Handler mHandler = new Handler();
	private Map<String, ImageView> mPhotosMap;
	
	public CommentsListAdapter(Context context, List<Comment> datas) {
		this.context = context;
		this.comments = datas;
		this.imageFetcher = ImageCache.getInstance();
		this.dataSet = new SparseBooleanArray();
		this.mPhotosMap = new WeakHashMap<String, ImageView>();
	}
	
	@Override
	public int getCount() {
		if (comments == null) {
			return 0;
		}
		return comments.size();
	}

	@Override
	public Object getItem(int position) {
		if (comments == null) {
			return null;
		}
		return comments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void addAll(List<Comment> data) {
		this.comments = data;
	}
	
	public void clearAll() {
		dataSet.clear();
	}
	
	public void deleteObservers() {
		mPhotosMap.clear();
		imageFetcher.deleteObserver(this);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		StateHolder mHolder = null;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.comment_post_item_view, null);
			mHolder = new StateHolder();
			mHolder.iconView = (ImageView) convertView.findViewById(R.id.iconView);
			mHolder.titleView = (TextView) convertView.findViewById(R.id.titleView);
			mHolder.descView = (TextView) convertView.findViewById(R.id.descView);
			mHolder.selectView = (CheckBox) convertView.findViewById(R.id.ckSelect);
			convertView.setTag(mHolder);
		} else {
			mHolder = (StateHolder)convertView.getTag();
		}

		Comment comment = comments.get(position);
		if (comment != null) {
			mHolder.titleView.setText(comment.getNick_name());
			setPhotos(comment.getUser_face(), mHolder.iconView);
			mHolder.descView.setText(comment.getCmt_text());
		}
		mHolder.selectView.setOnCheckedChangeListener(null);
		mHolder.selectView.setChecked(dataSet.get(position));
		mHolder.selectView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				dataSet.put(position, isChecked);
			}
		});
		
		return convertView;
	}

	private void setPhotos(String photoUrl, ImageView imageView) {
		if (imageFetcher.exists(photoUrl)) {
			Bitmap bitmap = imageFetcher.displayBitmap(photoUrl);
			imageView.setImageBitmap(bitmap);
		} else {
			imageView.setImageResource(R.drawable.ic_avatar);
			if (!mPhotosMap.containsKey(photoUrl)) {
				mPhotosMap.put(photoUrl, imageView);
			}
			imageFetcher.request(photoUrl);
		}
	}
	
	class StateHolder {
		ImageView iconView;
		TextView titleView;
		TextView descView;
		CheckBox selectView;
	}

	@Override
	public void update(Observable observable, Object data) {
		final String url = data.toString();
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				for (String item : mPhotosMap.keySet()) {
					if (item.equals(url)) {
						setPhotos(item, mPhotosMap.get(item));
					}
				}
			}
		});
	}

}
