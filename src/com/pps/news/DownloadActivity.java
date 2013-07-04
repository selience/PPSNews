package com.pps.news;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pps.news.app.BaseActivity;

public class DownloadActivity extends BaseActivity implements OnClickListener {

	private TextView txtSummury;
	private ListView listView;
	
	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.alarm_layout);
		TextView txtTitle = (TextView)findViewById(R.id.title);
		txtTitle.setText(R.string.offline_title_label);
		ImageView iconBack = (ImageView)findViewById(R.id.ic_back);
		iconBack.setImageResource(R.drawable.ic_download);
		iconBack.setOnClickListener(this);
		ImageView iconTrash = (ImageView)findViewById(R.id.add);
		iconTrash.setImageResource(R.drawable.btn_delete_selector);
		iconTrash.setOnClickListener(this);
		txtSummury = (TextView)findViewById(R.id.subTitle);
		listView = (ListView)findViewById(android.R.id.list);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ic_back:
			finish();
			break;
		case R.id.icon_trash:
			break;
		}
	}

}
