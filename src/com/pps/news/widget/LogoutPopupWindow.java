package com.pps.news.widget;

import com.pps.news.R;
import com.pps.news.util.UIUtil;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.PopupWindow;

public class LogoutPopupWindow extends PopupWindow implements android.view.View.OnClickListener {

	private int width;
	private Context context;
	private OnClickListener mPositiveClickListener;
	
	public LogoutPopupWindow(Context context) {
		this.context = context;
		initialize();
	}
	
	private void initialize() {
		width = UIUtil.getScreenWidth(context);
		View contentView = View.inflate(context, R.layout.logout_layout, null);
		contentView.setOnClickListener(this);
		setContentView(contentView);
		setFocusable(true);
		setWidth(width-UIUtil.dip2px(context, 100));
		setHeight(UIUtil.dip2px(context, 50));
		setAnimationStyle(R.style.PopupWindowAnimation);
		setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	}

	public void showPopupWindow(View anchor) {
		if (!isShowing() && anchor != null) {
			showAsDropDown(anchor, 0, -10);
		}
	}
	
	public void setPositiveClickListener(OnClickListener mPositiveListener) {
		this.mPositiveClickListener = mPositiveListener;
	}
	
	@Override
	public void onClick(View v) {
		BaseAlertDialog dialog = new BaseAlertDialog(context);
		dialog.setMessage(R.string.logout_tips);
		dialog.setPositiveClickListener(mPositiveClickListener);
		dialog.show();
		dismiss();
	}
}
