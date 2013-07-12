package com.pps.news;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.pps.news.app.BaseActivity;

public class DownloadActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.download_layout);
		TextView txtTitle = (TextView)findViewById(R.id.title);
		txtTitle.setText(R.string.offline_title_label);
		ImageView iconBack = (ImageView)findViewById(R.id.icon);
		iconBack.setImageResource(R.drawable.ic_download);
		iconBack.setOnClickListener(this);
		TextView txtSummary = (TextView)findViewById(R.id.summary);
		ListView listView = (ListView)findViewById(android.R.id.list);
		TextView txtTips = (TextView) findViewById(android.R.id.empty);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.icon:
			finish();
			break;
		}
	}

}
