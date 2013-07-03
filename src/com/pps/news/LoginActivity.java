package com.pps.news;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.pps.news.app.BaseActivity;

public class LoginActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.login_layout);
		TextView txtTitle = (TextView)findViewById(R.id.title);
		txtTitle.setText(R.string.login_title_label);
		ImageView iconBack = (ImageView)findViewById(R.id.ic_back);
		iconBack.setImageResource(R.drawable.ic_login);
		iconBack.setOnClickListener(this);
		TextView txtSummury = (TextView)findViewById(R.id.subTitle);
		txtSummury.setText("Login");
		findViewById(R.id.login).setOnClickListener(this);
		findViewById(R.id.register).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ic_back:
			finish();
			break;
		case R.id.register:
			break;
		case R.id.login:
			break;
		}
	}

}
