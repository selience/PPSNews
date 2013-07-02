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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentsAdapter extends BaseAdapter implements Observer {

	private Context context = null;;
	private List<Comment> comments = null;
	private ImageCache imageFetcher = null;
	private Handler mHandler = new Handler();
	private Map<String, ImageView> mPhotosMap = new WeakHashMap<String, ImageView>();
	
	public CommentsAdapter(Context context, List<Comment> datas) {
		this.context = context;
		this.comments = datas;
		this.imageFetcher = ImageCache.getInstance();
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
		if (data != null) {
			comments.addAll(data);
		}
	}
	
	public void deleteObservers() {
		mPhotosMap.clear();
		mPhotosMap = null;
		imageFetcher.deleteObserver(this);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		StateHolder mHolder = null;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.comment_list_item, null);
			mHolder = new StateHolder();
			mHolder.commentIcon = (ImageView) convertView.findViewById(R.id.comment_icon);
			mHolder.commentTime = (TextView) convertView.findViewById(R.id.comment_time);
			mHolder.commentTitle = (TextView) convertView.findViewById(R.id.comment_title);
			mHolder.commentDesc = (TextView) convertView.findViewById(R.id.comment_desc);
			mHolder.commentSrc = (TextView) convertView.findViewById(R.id.comment_src);
			convertView.setTag(mHolder);
		} else {
			mHolder = (StateHolder)convertView.getTag();
		}

		Comment comment = comments.get(position);
		if (comment != null) {
			String title = comment.getNick_name();
			if (comment.getChannel_id()!=0) {
				title = "[" + comment.getChannel_id() +"]";
			}
			mHolder.commentTitle.setText(title);
			setPhotos(comment.getUser_face(), mHolder.commentIcon);
			mHolder.commentTime.setText(comment.getAddtime());
			mHolder.commentDesc.setText(comment.getCmt_text());
			
			mHolder.commentSrc.setVisibility(View.VISIBLE);
			String source = context.getResources().getString(R.string.news_detail_source, String.valueOf(comment.getUpload_id()));
			mHolder.commentSrc.setText(source);
		}
		return convertView;
	}

	private void setPhotos(String photoUrl, ImageView imageView) {
		if (imageFetcher.exists(photoUrl)) {
			Bitmap bitmap = imageFetcher.displayBitmap(photoUrl);
			imageView.setImageBitmap(bitmap);
		} else {
			imageView.setImageResource(R.drawable.picture);
			if (!mPhotosMap.containsKey(photoUrl)) {
				mPhotosMap.put(photoUrl, imageView);
			}
			imageFetcher.request(photoUrl);
		}
	}
	
	class StateHolder {
		ImageView commentIcon;
		TextView commentTime;
		TextView commentTitle;
		TextView commentDesc;
		TextView commentSrc;
	}

	@Override
	public void update(Observable observable, Object data) {
		final String url = data.toString();
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				for (String item : mPhotosMap.keySet()) {
					if (item.equals(url)) {
						ImageView imageView = mPhotosMap.get(item);
						setPhotos(item, imageView);
					}
				}
			}
		});
	}
}