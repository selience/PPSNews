package com.pps.news;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.pps.news.app.BaseActivity;
import com.pps.news.constant.PreferenceUtils;

public class SettingActivity extends BaseActivity implements OnClickListener {

	private CheckBox ckIsAuto;
	
	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.setting_layout);
		TextView txtTitle = (TextView)findViewById(R.id.title);
		txtTitle.setText(R.string.setting_title_label);
		ImageView iconBack = (ImageView)findViewById(R.id.icon);
		iconBack.setImageResource(R.drawable.ic_setting);
		iconBack.setOnClickListener(this);
		TextView txtSummury = (TextView)findViewById(R.id.summary);
		txtSummury.setText("Setting");
		findViewById(R.id.setting_clear_cache).setOnClickListener(this);
		ckIsAuto = (CheckBox)findViewById(R.id.setting_auto_clear);
		ckIsAuto.setChecked(PreferenceUtils.getIsAutoClearCache(this));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.icon:
			finish();
			break;
		case R.id.setting_clear_cache:
			break;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		PreferenceUtils.storeIsAutoClearCache(this, ckIsAuto.isChecked());
	}
	
}
