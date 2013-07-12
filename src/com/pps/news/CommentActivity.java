package com.pps.news;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.pps.news.adapter.CommentsAdapter;
import com.pps.news.app.BaseActivity;
import com.pps.news.bean.Comment;
import com.pps.news.bean.Group;
import com.pps.news.bean.Result;
import com.pps.news.constant.Constants;
import com.pps.news.task.AddCommentTask;
import com.pps.news.task.GetCommentsTask;
import com.pps.news.task.TaskListener;
import com.pps.news.util.CacheUtil;
import com.pps.news.util.ToastUtils;
import com.pps.news.util.UIUtil;

public class CommentActivity extends BaseActivity implements OnClickListener, TaskListener {
	private static final String ADD_COMMENT_TASK = "ADD_COMMENT_TASK";
	private static final String GET_COMMENT_TASK = "GET_COMMENT_TASK";
	
	private TextView txtTitle;
	private TextView txtSummuy;
	private TextView txtTips;
	private ListView listView;
	private ImageView imageView;
	private ImageView imageTrash;
	private TextView postView;
	private EditText messageView;
	
	private int status;
	private long newsId;
	private ArrayList<Comment> comments;
	private CommentsAdapter commentAdapter;
	
	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.comment_layout);
		txtTitle = (TextView) findViewById(R.id.title);
		imageView = (ImageView) findViewById(R.id.icon);
		txtSummuy = (TextView) findViewById(R.id.summary);
		txtTips = (TextView) findViewById(android.R.id.empty);
		listView = (ListView) findViewById(android.R.id.list);
		imageView.setImageResource(R.drawable.ic_comment);
		imageView.setOnClickListener(this);
		
		newsId = getIntent().getLongExtra(Constants.NEWS_ID_EXTRAS, 0L);
		status = getIntent().getIntExtra(Constants.NEWS_DETAIL_EXTRAS, Constants.NEWS_DETAIL_SELF_COMMENT);
		if (status == Constants.NEWS_DETAIL_SELF_COMMENT) { //自己评论
			((ViewStub)findViewById(R.id.toolbar)).inflate();
			txtTitle.setText(R.string.comment_title_self_label);
			imageTrash = (ImageView) findViewById(R.id.imageView);
			imageTrash.setOnClickListener(this);
		} else if (status == Constants.NEWS_DETAIL_FRIEND_COMMENT) { //网友评论
			((ViewStub)findViewById(R.id.viewStub)).inflate();
			txtTitle.setText(R.string.comment_title_friend_label);
			messageView=(EditText)findViewById(R.id.message);
			postView = (TextView)findViewById(R.id.postView);
			postView.setOnClickListener(this);
			
		}
		onRefresh(CacheUtil.getCommentCache(newsId));
		new GetCommentsTask(this, GET_COMMENT_TASK).execute(String.valueOf(newsId));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.icon:
			finish();
			break;
		case R.id.imageView:
			Intent intent = new Intent(this, CommentEditActivity.class);
			intent.putParcelableArrayListExtra(Constants.NEWS_DETAIL_EXTRAS, comments);
			startActivity(intent);
			break;
		case R.id.postView: // 发表评论
			UIUtil.hideSoftInputFromWindow(messageView);
			if (!TextUtils.isEmpty(messageView.getText())) {
				String message = messageView.getText().toString();
				ToastUtils.showMessage(this, message, Toast.LENGTH_SHORT);
				messageView.setText("");
				new AddCommentTask(this, ADD_COMMENT_TASK).execute(String.valueOf(newsId), message);
			}
			break;
		}
	}

	private void setTrashEnabled(boolean enabled) {
		if (imageTrash != null) {
			imageTrash.setEnabled(enabled);
		}
	}
	
	private void onRefresh(List<Comment> data) {
		if (data != null && data.size() > 0) {
			txtSummuy.setText(data.size()+"");
			setTrashEnabled(true);
			txtTips.setVisibility(View.GONE);
			if (commentAdapter == null) {
				commentAdapter = new CommentsAdapter(this, data);
				listView.setAdapter(commentAdapter);
			} else {
				commentAdapter.addAll(data);
				commentAdapter.notifyDataSetChanged();
			}
		} else {
			setTrashEnabled(false);
			txtTips.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onTaskStart(String taskName) {
	}

	@Override
	public void onTaskFinished(String taskName, Result result) {
		notifyErrorMessage(result.getException());
		if (taskName.equals(GET_COMMENT_TASK) && result.getValue()!=null) {
			comments = (Group<Comment>)result.getValue();
			onRefresh(comments);
		} else if (taskName.equals(ADD_COMMENT_TASK)) {
			new GetCommentsTask(this, GET_COMMENT_TASK).execute(String.valueOf(newsId));
		}
	}
}
