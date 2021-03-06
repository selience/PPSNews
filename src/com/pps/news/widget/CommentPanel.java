package com.pps.news.widget;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.WeakHashMap;
import com.pps.news.R;
import com.pps.news.bean.Comment;
import com.pps.news.util.ImageCache;
import com.pps.news.util.UIUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommentPanel extends LinearLayout implements Observer {

	private ImageCache imageFetcher = null;
	private Handler mHandler = new Handler();
	private Map<String, ImageView> mPhotosMap = null;
	private OnClickListener mOnClickListener = null;
	
	public CommentPanel(Context context) {
		super(context);
		init(context);
	}
	
	public CommentPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	
	private void init(Context context) {
		setOrientation(LinearLayout.VERTICAL);
		imageFetcher = ImageCache.getInstance();
		mPhotosMap = new WeakHashMap<String, ImageView>();
	}

	public void deleteObservers() {
		mPhotosMap.clear();
		imageFetcher.deleteObserver(this);
	}
	
	public void setItems(List<Comment> comments) {
		if (comments == null) return;
		removeAllViews();
		
		View view = null;
		int index = 0;
		for (Comment comment : comments) {
			view = View.inflate(getContext(), R.layout.comment_list_item_view, null);
			View line = view.findViewById(R.id.line);
			ImageView commentIcon = (ImageView) view.findViewById(R.id.comment_icon);
			TextView commentTime = (TextView) view.findViewById(R.id.comment_time);
			TextView commentTitle = (TextView) view.findViewById(R.id.comment_title);
			TextView commentDesc = (TextView) view.findViewById(R.id.comment_desc);
			view.setTag(comment);
			view.setClickable(true);
			if (mOnClickListener != null) {
				view.setOnClickListener(mOnClickListener);
			}
			
			setPhotos(comment.getUser_face(), commentIcon);
			commentTitle.setText(comment.getNick_name());
			Date d = UIUtil.formatDate(comment.getAddtime());
			commentTime.setText(UIUtil.formatDate(d.getTime()));
			commentDesc.setText(comment.getCmt_text());
			
			index++;
			if (index == comments.size()) 
				line.setVisibility(View.GONE);
			
			addView(view);
		}
	}
	
	private void setPhotos(String photoUrl, ImageView imageView) {
		if (TextUtils.isEmpty(photoUrl)) return;
		if (imageFetcher.exists(photoUrl)) {
			Bitmap bitmap = imageFetcher.displayBitmap(photoUrl);
			imageView.setImageBitmap(bitmap);
		} else {
			imageView.setImageResource(R.drawable.ic_avatar);
			mPhotosMap.put(photoUrl, imageView);
			imageFetcher.request(photoUrl);
		}
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
	
	public void setOnClickListener(OnClickListener mListener) {
		this.mOnClickListener = mListener;
	}
}
