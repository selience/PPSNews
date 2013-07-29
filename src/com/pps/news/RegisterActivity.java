package com.pps.news;

import tv.pps.vipmodule.vip.protol.ProtocolRegister;
import tv.pps.vipmodule.vip.protol.BaseProtocol.RequestCallBack;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.pps.news.app.BaseActivity;
import com.pps.news.util.ToastUtils;
import com.pps.news.util.UIUtil;

public class RegisterActivity extends BaseActivity implements OnClickListener,RequestCallBack<Boolean> {

	private EditText postView;
	private EditText passView;
	private EditText rePassView;
	private ProgressDialog dialog;
	
	private String account;
	private String passwd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
		TextView txtTitle = (TextView) findViewById(R.id.title);
		txtTitle.setText(R.string.register_title_label);
		ImageView iconBack = (ImageView) findViewById(R.id.icon);
		iconBack.setImageResource(R.drawable.ic_login);
		iconBack.setOnClickListener(this);
		TextView txtSummury = (TextView) findViewById(R.id.summary);
		txtSummury.setText("Register");
		findViewById(R.id.cancel).setOnClickListener(this);
		findViewById(R.id.register).setOnClickListener(this);
		postView = (EditText)findViewById(R.id.postView);
		passView = (EditText)findViewById(R.id.passView);
		rePassView = (EditText)findViewById(R.id.rePassView);
		
		dialog = new ProgressDialog(this);
		dialog.setMessage(getString(R.string.register_tips));
		dialog.setOwnerActivity(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.icon:
		case R.id.cancel:
			finish();
			break;
		case R.id.register:
			if (validate()) {
				account = postView.getText().toString();
				passwd = passView.getText().toString();
				ProtocolRegister protocolRegister = new ProtocolRegister(this);
				protocolRegister.fetch(account, passwd, this);
			}
			break;
		}
	}

	private boolean validate() {
		if (TextUtils.isEmpty(postView.getText())
				|| TextUtils.isEmpty(passView.getText())
				|| TextUtils.isEmpty(rePassView.getText())) {
			ToastUtils.showMessage(this, "用户名或密码不能为空", Toast.LENGTH_SHORT);
			return false;
		} else if (!passView.getText().toString().equals(rePassView.getText().toString())) {
			ToastUtils.showMessage(this, "两次密码输入不一致", Toast.LENGTH_SHORT);
			return false;
		}
		return true;
	}

	@Override
	public void onRequestStart() {
		// TODO Auto-generated method stub
		UIUtil.hideSoftInputFromWindow(postView);
		dialog.show();
	}

	@Override
	public void onRequestError(String errorCode, String errorMessage) {
		// TODO Auto-generated method stub
		dialog.dismiss();
		ToastUtils.showMessage(this, errorMessage, Toast.LENGTH_SHORT);
	}

	@Override
	public void onRequestSuccess(Boolean formData, String successMessage) {
		Log.d("onRequestSuccess", successMessage);
		ToastUtils.showMessage(this, R.string.register_success, Toast.LENGTH_SHORT);
		dialog.dismiss();
		finish();
	}

	@Override
	public void onRequestLoading(long current, long total) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		dialog.dismiss();
	}
}
