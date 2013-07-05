package com.pps.news.widget;

import com.pps.news.R;
import com.pps.news.util.UIUtil;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;

public class SharePopupWindow extends PopupWindow implements OnClickListener {
	private OnClickListener mOnClickListener; 
	
	public SharePopupWindow(Context context) {
		initialize(context);
	}

	private void initialize(Context context) {
		View contentView = View.inflate(context, R.layout.share_layout, null);
		setContentView(contentView);
		setFocusable(true);
		setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		setHeight(UIUtil.getScreenHeight(context)-UIUtil.dip2px(context, 45+2));
		setAnimationStyle(R.style.PopupWindowAnimation);
		setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_share));
		
		contentView.findViewById(R.id.ic_sina).setOnClickListener(this);
		contentView.findViewById(R.id.ic_tencent).setOnClickListener(this);
		contentView.findViewById(R.id.ic_qzone).setOnClickListener(this);
	}
	
	public void showPopupWindow(View anchor) {
		int[] location = new int[2];
		anchor.getLocationOnScreen(location);
		showAtLocation(anchor, Gravity.NO_GRAVITY, location[0], location[1]-getHeight());
	}

	public void setOnClickListener(OnClickListener mClickListener) {
		this.mOnClickListener = mClickListener;
	}

	@Override
	public void onClick(View v) {
		if (mOnClickListener != null) {
			mOnClickListener.onClick(v);
		}
	}
}
