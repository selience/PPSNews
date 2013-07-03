package com.pps.news;

import java.util.List;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.pps.news.adapter.CommentsAdapter;
import com.pps.news.app.BaseActivity;
import com.pps.news.bean.Comment;
import com.pps.news.bean.Group;
import com.pps.news.bean.Result;
import com.pps.news.parser.CommentParser;
import com.pps.news.task.GenericTask;
import com.pps.news.task.TaskListener;
import com.pps.news.util.Utility;

public class CommentActivity extends BaseActivity implements OnClickListener {

	private TextView txtTitle;
	private TextView txtSummuy;
	private ListView listView;
	private ImageView imageView;
	private CommentsAdapter commentAdapter;
	
	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.comment_layout);
		txtTitle = (TextView) findViewById(R.id.title);
		imageView = (ImageView) findViewById(R.id.ic_back);
		txtSummuy = (TextView) findViewById(R.id.subTitle);
		listView = (ListView) findViewById(android.R.id.list);
		txtTitle.setText(R.string.comment_title_label);
		imageView.setImageResource(R.drawable.ic_comment);
		
		Group<Comment> comments = new CommentParser().parse(Utility.readComment(this));
		onRefresh(comments);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ic_back:
			finish();
			break;
		}
	}

	private void onRefresh(List<Comment> data) {
		if (data != null && data.size() > 0) {
			txtSummuy.setText(data.size()+"");
			if (commentAdapter == null) {
				commentAdapter = new CommentsAdapter(this, data);
				listView.setAdapter(commentAdapter);
			} else {
				commentAdapter.addAll(data);
				commentAdapter.notifyDataSetChanged();
			}
		}
	}
	
	class GetMyCommentTask extends GenericTask {

		public GetMyCommentTask(TaskListener taskListener, String taskName) {
			super(taskListener, taskName);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected Result doInBackground(String... params) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
