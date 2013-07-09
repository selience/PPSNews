package com.pps.news;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
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
import com.pps.news.constant.Constants;
import com.pps.news.parser.CommentParser;
import com.pps.news.task.GenericTask;
import com.pps.news.task.TaskListener;
import com.pps.news.util.Utility;

public class CommentActivity extends BaseActivity implements OnClickListener {

	private TextView txtTitle;
	private TextView txtSummuy;
	private TextView txtTips;
	private ListView listView;
	private ImageView imageView;
	private ImageView imageTrash;
	
	private int status;
	private ArrayList<Comment> comments;
	private CommentsAdapter commentAdapter;
	
	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.comment_layout);
		txtTitle = (TextView) findViewById(R.id.title);
		imageView = (ImageView) findViewById(R.id.ic_back);
		txtSummuy = (TextView) findViewById(R.id.subTitle);
		txtTips = (TextView) findViewById(android.R.id.empty);
		listView = (ListView) findViewById(android.R.id.list);
		imageTrash = (ImageView) findViewById(R.id.icon_trash);
		imageView.setImageResource(R.drawable.ic_comment);
		imageView.setOnClickListener(this);
		
		status = getIntent().getIntExtra(Constants.NEWS_DETAIL_EXTRAS, Constants.NEWS_DETAIL_SELF_COMMENT);
		if (status == Constants.NEWS_DETAIL_SELF_COMMENT) { //自己评论
			txtTitle.setText(R.string.comment_title_self_label);
		} else if (status == Constants.NEWS_DETAIL_FRIEND_COMMENT) { //网友评论
			imageTrash.setVisibility(View.GONE);
			txtTitle.setText(R.string.comment_title_friend_label);
		}
		comments = new CommentParser().parse(Utility.readComment(this));
		onRefresh(comments);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ic_back:
			finish();
			break;
		case R.id.icon_trash:
			Intent intent = new Intent(this, CommentProfileActivity.class);
			intent.putParcelableArrayListExtra(Constants.NEWS_DETAIL_EXTRAS, comments);
			startActivity(intent);
			break;
		}
	}

	private void onRefresh(List<Comment> data) {
		if (data != null && data.size() > 0) {
			txtSummuy.setText(data.size()+"");
			imageTrash.setEnabled(true);
			txtTips.setVisibility(View.GONE);
			if (commentAdapter == null) {
				commentAdapter = new CommentsAdapter(this, data);
				listView.setAdapter(commentAdapter);
			} else {
				commentAdapter.addAll(data);
				commentAdapter.notifyDataSetChanged();
			}
		} else {
			txtTips.setVisibility(View.VISIBLE);
			imageTrash.setEnabled(false);
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
