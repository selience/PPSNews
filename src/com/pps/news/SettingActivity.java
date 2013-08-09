package com.pps.news;

import java.io.File;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.pps.news.app.BaseActivity;
import com.pps.news.constant.PreferenceUtils;
import com.pps.news.network.RequestExecutor;
import com.pps.news.util.CacheUtil;
import com.pps.news.util.FileUtils;
import com.pps.news.util.ImageCache;
import com.pps.news.util.Log;
import com.pps.news.util.UIUtil;
import com.pps.news.widget.BaseAlertDialog;

public class SettingActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {
	private static final String TAG = "SettingActivity";
	private static final int MESSAGE_COLLECT_CACHE_SIZE = 0x100;
	
	private TextView txtView;
	private CheckBox ckIsAuto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_layout);
		TextView txtTitle = (TextView)findViewById(R.id.title);
		txtTitle.setText(R.string.setting_title_label);
		ImageView iconBack = (ImageView)findViewById(R.id.icon);
		iconBack.setImageResource(R.drawable.ic_setting);
		iconBack.setOnClickListener(this);
		TextView txtSummury = (TextView)findViewById(R.id.summary);
		txtSummury.setText("Setting");
		txtView	= (TextView) findViewById(R.id.setting_clear_cache);
		txtView.setText(R.string.setting_clear_cache);
		txtView.setOnClickListener(this);
		
		ckIsAuto = (CheckBox)findViewById(R.id.setting_auto_clear);
		ckIsAuto.setChecked(PreferenceUtils.getIsAutoClearCache(this));
		ckIsAuto.setOnCheckedChangeListener(this);
		
		collectCacheSize();
	}

	// 收集缓存数据大小
	private void collectCacheSize() {
		if (UIUtil.isSDCardAvailable()) {
			RequestExecutor.doAsync(new Runnable() {
				@Override
				public void run() {
					Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
					// 统计缓存数据大小
					File baseDirectory = new File(Environment.getExternalStorageDirectory(), "PPSNews/"+ImageCache.CACHE_DIRECTORY);
					String totalFileSize = FileUtils.getPathSize(baseDirectory.getAbsolutePath());
					Message.obtain(mHandler, MESSAGE_COLLECT_CACHE_SIZE, totalFileSize).sendToTarget();
				}
			});
		}
	}
	
	// 手动清除缓存内容，包括新闻内容和图片
	private void clearAll() {
		if (UIUtil.isSDCardAvailable()) {
			RequestExecutor.doAsync(new Runnable() {
				@Override
				public void run() {
					File dirPath = new File(Environment.getExternalStorageDirectory(), CacheUtil.CACHE_DIRECTORY);
					FileUtils.deleteAllFile(dirPath.getAbsolutePath()); // 删除新闻缓存
					ImageCache.getInstance().clear(); // 删除图片缓存
					collectCacheSize(); 
				}
			});
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.icon:
			finish();
			break;
		case R.id.setting_clear_cache:
			new BaseAlertDialog(this).
			setMessage(R.string.setting_clear_cache_tips)
			.setPositiveClickListener(new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					clearAll();
				}
			}).show();
			break;
		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_COLLECT_CACHE_SIZE:
				txtView.setText(getString(R.string.setting_clear_cache_label, msg.obj));
				break;
			}
		}
	};

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Log.d(TAG, "onCheckedChanged::"+isChecked);
		PreferenceUtils.storeIsAutoClearCache(this, isChecked);
	}

}
