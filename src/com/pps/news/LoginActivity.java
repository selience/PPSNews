package com.pps.news;

import org.json.JSONException;
import org.json.JSONObject;
import tv.pps.vipmodule.vip.protol.ProtocolLogin;
import tv.pps.vipmodule.vip.protol.BaseProtocol.RequestCallBack;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.pps.news.app.NewsApplication;
import com.pps.news.constant.Config;
import com.pps.news.constant.PreferenceUtils;
import com.pps.news.util.ToastUtils;
import com.pps.news.util.UIUtil;

public class LoginActivity extends BaseActivity implements OnClickListener,  RequestCallBack<Boolean> {

	private EditText edtAccount;
	private EditText edtPassword;
	private CheckBox ckRememberPwd;
	
	private boolean isAutoLogin;
	private String account, passwd;

	private ProgressDialog dialog;
	private SharedPreferences mPrefs;
	
	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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
		
		mPrefs = NewsApplication.mPrefs;
		account = PreferenceUtils.getUserName(mPrefs);
		passwd = PreferenceUtils.getUserPass(mPrefs);
		isAutoLogin = PreferenceUtils.getIsAutoLogin(mPrefs);
		if (!TextUtils.isEmpty(account)) {
			edtAccount.setText(account);
		}
		if (!TextUtils.isEmpty(passwd)) {
			edtPassword.setText(passwd);
		}
		ckRememberPwd.setChecked(isAutoLogin);
		
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
			if (validate()) {
				startLogin();
			}
			break;
		case R.id.register:
			startActivity(new Intent(this, RegisterActivity.class));
			break;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		PreferenceUtils.storeIsAutoLogin(mPrefs, ckRememberPwd.isChecked());
	}
	
	private boolean validate() {
		if (TextUtils.isEmpty(edtAccount.getText()) ||
				TextUtils.isEmpty(edtPassword.getText())) {
			ToastUtils.showMessage(this, R.string.vip_login_name_password_empty, Toast.LENGTH_SHORT);
			return false;
		}
		return true;
	}
	
	private void startLogin() {
		account = edtAccount.getText().toString().trim();
		passwd = edtPassword.getText().toString().trim();
		ProtocolLogin protocolLogin = new ProtocolLogin(this);
		protocolLogin.fetch(account, passwd, isAutoLogin, this);
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
		ToastUtils.showMessage(this, errorMessage, Toast.LENGTH_SHORT);
	}

	@Override
	public void onRequestSuccess(Boolean formData, String successMessage) {
		// TODO Auto-generated method stub
		Log.i("onRequestSuccess", successMessage);
		String userId = parseUserId(successMessage);
		Config.userId = userId;
		PreferenceUtils.storeUserId(mPrefs, userId);
		PreferenceUtils.storeUserName(mPrefs, account);
		PreferenceUtils.storeUserPass(mPrefs, passwd);
		dialog.dismiss();
		finish();
	}

	@Override
	public void onRequestLoading(long current, long total) {
	}

	@Override
	public void onCancel() {
	}
	
	// 获取UserId的Value
	private String parseUserId(String content) {
		try {
			JSONObject json = new JSONObject(content);
			json = json.getJSONObject("data");
			json = json.getJSONObject("userinfo");
			return json.getString("uid");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
