package com.pps.news.widget;

import com.pps.news.R;
import com.pps.news.util.UIUtil;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BaseAlertDialog extends Dialog implements View.OnClickListener {

	private TextView txtMessage;
	private View contentView;
	private View btnCancel;
	private View btnConfirm;
	private Resources mResource;
	
	private OnClickListener mPositiveClickListener;
	private OnClickListener mNegativeClickListener;
	
	public BaseAlertDialog(Context context) {
		this(context, R.style.Theme_Dialog);
	}
	
	public BaseAlertDialog(Context context, int theme) {
		super(context, theme);
		setContentView(R.layout.dialog_layout);
		mResource = context.getResources();
		contentView = findViewById(R.id.dialog);
		btnCancel = findViewById(R.id.cancel);
		btnConfirm = findViewById(R.id.confirm);
		txtMessage = (TextView) findViewById(R.id.message);
		
		btnConfirm.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		
		int width = UIUtil.getScreenWidth(context) - 80;		
		setWidth(width);
	}

	/**
	 * 设置内容
	 * @param resId
	 * @return
	 */
	public BaseAlertDialog setMessage(int resId) {
		return setMessage(mResource.getString(resId));
	}
	
	/**
	 * 设置内容
	 * @param message
	 * @return
	 */
	public BaseAlertDialog setMessage(CharSequence message) {
		txtMessage.setText(message);
		return this;
	}
	
	/**
	 * 设置高度
	 * @param width
	 * @return
	 */
	public BaseAlertDialog setWidth(int width) {
		ViewGroup.LayoutParams lp = contentView.getLayoutParams();
		if (lp == null) {
			lp = new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
		} else {
			lp.width = width;
		}
		contentView.setLayoutParams(lp);
		return this;
	}
	
	public BaseAlertDialog setPositiveClickListener(OnClickListener mPositiveListener) {
		this.mPositiveClickListener = mPositiveListener;
		return this;
	}
	
	public BaseAlertDialog setNegativeClickListener(OnClickListener mNegativeListener) {
		this.mNegativeClickListener = mNegativeListener;
		return this;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confirm:
			if (mPositiveClickListener!=null) {
				mPositiveClickListener.onClick(this, DialogInterface.BUTTON_POSITIVE);
			}
			break;
		case R.id.cancel:
			if (mNegativeClickListener!=null) {
				mNegativeClickListener.onClick(this, DialogInterface.BUTTON_NEGATIVE);
			}
			dismiss();
			break;
		}
	}
}
