package com.pps.news;

import tv.pps.vipmodule.vip.AccountVerify;
import tv.pps.vipmodule.vip.protol.ProtocolLogin;
import tv.pps.vipmodule.vip.protol.BaseProtocol.RequestCallBack;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.pps.news.app.BaseActivity;
import com.pps.news.constant.PreferenceUtils;
import com.pps.news.util.ToastUtils;
import com.pps.news.util.UIUtil;

public class LoginActivity extends BaseActivity implements OnClickListener,  RequestCallBack<Boolean> {

	private EditText edtAccount;
	private EditText edtPassword;
	private CheckBox ckRememberPwd;
	private ProgressDialog dialog;
	
	private String userName;
	private String userPass;
	private boolean isAutoLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		TextView txtTitle = (TextView)findViewById(R.id.title);
		txtTitle.setText(R.string.login_title_label);
		ImageView iconBack = (ImageView)findViewById(R.id.icon);
		iconBack.setImageResource(R.drawable.ic_login);
		iconBack.setOnClickListener(this);
		TextView txtSummury = (TextView)findViewById(R.id.summary);
		txtSummury.setText("Login");
		edtAccount = (EditText)findViewById(R.id.account);
		edtPassword = (EditText)findViewById(R.id.pass);
		ckRememberPwd = (CheckBox)findViewById(R.id.rememberUserPass);
		findViewById(R.id.login).setOnClickListener(this);
		findViewById(R.id.register).setOnClickListener(this);
		
		userPass = PreferenceUtils.getUserPassword();
		userName = AccountVerify.getInstance().getmUserName();
		ckRememberPwd.setChecked(PreferenceUtils.getIsAutoLogin());

		if (!TextUtils.isEmpty(userName)) {
			edtAccount.setText(userName);
		}
		if (!TextUtils.isEmpty(userPass)) {
			edtPassword.setText(userPass);
		}
		
		dialog = new ProgressDialog(this);
		dialog.setMessage(getString(R.string.login_tips));
		dialog.setOwnerActivity(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.icon:
			finish();
			break;
		case R.id.login:
			startUserLogin();
			break;
		case R.id.register:
			startActivity(new Intent(this, RegisterActivity.class));
			break;
		}
	}

	// 验证用户信息不为空
	private boolean validate() {
		if (TextUtils.isEmpty(edtAccount.getText()) ||
				TextUtils.isEmpty(edtPassword.getText())) {
			ToastUtils.showMessage(this, R.string.vip_login_name_password_empty, Toast.LENGTH_SHORT);
			return false;
		}
		return true;
	}
	
	// 开始登陆
	private void startUserLogin() {
		if (validate()) {
			isAutoLogin = ckRememberPwd.isChecked();
			userName = edtAccount.getText().toString().trim();
			userPass = edtPassword.getText().toString().trim();
			ProtocolLogin protocolLogin = new ProtocolLogin(this);
			protocolLogin.fetch(userName, userPass, isAutoLogin, this);
		}
	}

	@Override
	public void onRequestStart() {
		// TODO Auto-generated method stub
		UIUtil.hideSoftInputFromWindow(edtPassword);
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
		// TODO Auto-generated method stub
		Log.i("onRequestSuccess", successMessage);
		AccountVerify accountVerify = AccountVerify.getInstance();
		PreferenceUtils.storeUser(accountVerify); //存储用户信息
		if (ckRememberPwd.isChecked()) { //存储用户密码
			PreferenceUtils.storeUserPass(userPass);
		} else {
			PreferenceUtils.removeUserPass();
		}
		// 是否记住密码
		PreferenceUtils.storeIsAutoLogin(ckRememberPwd.isChecked());
		
		ToastUtils.showMessage(this, R.string.login_success, Toast.LENGTH_SHORT);
		dialog.dismiss();
		finish();
	}

	@Override
	public void onRequestLoading(long current, long total) {
	}

	@Override
	public void onCancel() {
		dialog.dismiss();
	}
}
