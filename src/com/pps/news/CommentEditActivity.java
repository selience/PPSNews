package com.pps.news;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import com.pps.news.adapter.CommentsListAdapter;
import com.pps.news.app.BaseActivity;
import com.pps.news.bean.Comment;
import com.pps.news.constant.Constants;

public class CommentEditActivity extends BaseActivity implements OnClickListener {

	private TextView txtSelect;
	private ListView listView;
	private CommentsListAdapter commentAdapter;
	private List<Integer> dataSet = new ArrayList<Integer>();
	
	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.comment_edit_layout);
		findViewById(R.id.cancel).setOnClickListener(this);
		findViewById(R.id.edit).setOnClickListener(this);
		listView=(ListView)findViewById(android.R.id.list);
		txtSelect =(TextView)findViewById(R.id.txtSelect);
		txtSelect.setText(getString(R.string.comment_edit_bar_label, 0));
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Comment comment = (Comment) parent.getItemAtPosition(position);
				Integer rn = Integer.valueOf(comment.getRn());
				CheckBox ck = (CheckBox) view.findViewById(R.id.ckSelect);
				ck.setChecked(!ck.isChecked());
				
				if (ck.isChecked()) {
					dataSet.add(rn);
				} else {
					if (dataSet.contains(rn)) {
						dataSet.remove(rn);
					}
				}
				txtSelect.setText(getString(R.string.comment_edit_bar_label, dataSet.size()));
			}
		});
		ArrayList<Comment> comments = getIntent().getParcelableArrayListExtra(Constants.NEWS_DETAIL_EXTRAS);
		onRefresh(comments);
	}

	private void onRefresh(List<Comment> data) {
		if (data != null && data.size() > 0) {
			if (commentAdapter == null) {
				commentAdapter = new CommentsListAdapter(this, data);
				listView.setAdapter(commentAdapter);
			} else {
				commentAdapter.addAll(data);
				commentAdapter.notifyDataSetChanged();
			}
		} 
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cancel:
			finish();
			break;
		case R.id.edit:
			break;
		}
	}

}
