package com.pps.news;

import java.util.Collections;
import java.util.Observable;
import java.util.Observer;
import org.apache.http.HttpStatus;
import tv.pps.vipmodule.vip.AccountVerify;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.pps.news.adapter.CommentsAdapter;
import com.pps.news.app.BaseActivity;
import com.pps.news.app.NewsApplication;
import com.pps.news.bean.Comment;
import com.pps.news.bean.Group;
import com.pps.news.bean.Result;
import com.pps.news.constant.Constants;
import com.pps.news.task.AddCommentTask;
import com.pps.news.task.GetVideoCommentTask;
import com.pps.news.task.GetUserCommentTask;
import com.pps.news.task.NotificationTask;
import com.pps.news.task.TaskListener;
import com.pps.news.task.NotificationTask.TaskData;
import com.pps.news.util.CacheUtil;
import com.pps.news.util.UIUtil;

public class CommentActivity extends BaseActivity implements OnClickListener, 
	TaskListener, OnScrollListener, Observer {
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
	private View footerView;
	private ViewGroup moreView;
	
	private int status;
	private long userId = 0;
	private long newsId = 0; //新闻id
	private int lastItem = 0; //最后一项索引
	private int pageNo = 1; //初始状态页码
	private Group<Comment> comments;
	private CommentsAdapter mAdapter;
	
	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.comment_layout);
		txtTitle = (TextView) findViewById(R.id.title);
		imageView = (ImageView) findViewById(R.id.icon);
		txtSummuy = (TextView) findViewById(R.id.summary);
		txtSummuy.setText("0");
		txtTips = (TextView) findViewById(android.R.id.empty);
		listView = (ListView) findViewById(android.R.id.list);
		listView.setOnScrollListener(this);
		imageView.setImageResource(R.drawable.ic_comment);
		imageView.setOnClickListener(this);
		
		NewsApplication.getInstance().addObserver(this);
		moreView = new FrameLayout(this);
		listView.addFooterView(moreView);
		this.comments = new Group<Comment>();
		footerView = View.inflate(this, R.layout.list_footer_view, null);
		
		status = getIntent().getIntExtra(Constants.NEWS_DETAIL_EXTRAS, Constants.NEWS_DETAIL_SELF_COMMENT);
		if (status == Constants.NEWS_DETAIL_SELF_COMMENT) { //自己评论
			((ViewStub)findViewById(R.id.toolbar)).inflate();
			txtTitle.setText(R.string.comment_title_self_label);
			imageTrash = (ImageView) findViewById(R.id.imageView);
			imageTrash.setOnClickListener(this);
//			userId = Long.valueOf(AccountVerify.getInstance().getmUID());
//			onRefresh(CacheUtil.getCommentCache(userId));
		} else if (status == Constants.NEWS_DETAIL_FRIEND_COMMENT) { //网友评论
			((ViewStub)findViewById(R.id.viewStub)).inflate();
			txtTitle.setText(R.string.comment_title_friend_label);
			messageView=(EditText)findViewById(R.id.message);
			postView = (TextView)findViewById(R.id.postView);
			postView.setOnClickListener(this);
//			newsId = getIntent().getLongExtra(Constants.NEWS_ID_EXTRAS, 0L);
//			onRefresh(CacheUtil.getCommentCache(newsId));
		}

		sendRequest();
	}

	private void sendRequest() {
		if (status == Constants.NEWS_DETAIL_SELF_COMMENT) {
			new GetUserCommentTask(this, GET_COMMENT_TASK).execute(String.valueOf(pageNo), Constants.COMMENTS_LIST_PAGESIZE);
		} else if (status == Constants.NEWS_DETAIL_FRIEND_COMMENT) {
			new GetVideoCommentTask(this, GET_COMMENT_TASK).execute(String.valueOf(newsId),String.valueOf(pageNo),Constants.COMMENTS_LIST_PAGESIZE);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.icon:
			finish();
			break;
		case R.id.imageView:
			Intent intent = new Intent(this, CommentEditActivity.class);
			if (comments != null) {
				intent.putParcelableArrayListExtra(Constants.NEWS_DETAIL_EXTRAS, comments);
			}
			startActivity(intent);
			break;
		case R.id.postView: // 发表评论
			UIUtil.hideSoftInputFromWindow(messageView);
			if (!TextUtils.isEmpty(messageView.getText())) {
				String ipString = UIUtil.getIPAddress(this);
				String message = messageView.getText().toString();
				AddCommentTask addTask = new AddCommentTask(this, ADD_COMMENT_TASK);
				addTask.execute(String.valueOf(newsId), message, ipString);
			}
			break;
		}
	}

	// 禁用删除功能
	private void setTrashEnabled(boolean enabled) {
		if (imageTrash != null) {
			imageTrash.setEnabled(enabled);
		}
	}
	
	// 刷新列表视图
	private void onRefresh(Group<Comment> data) {
		if (mAdapter == null) {
			mAdapter = new CommentsAdapter(this, data);
			listView.setAdapter(mAdapter);
		} else {
			mAdapter.addAll(data);
			mAdapter.notifyDataSetChanged();
		}
		
		if (data==null || data.isEmpty()) {
			setTrashEnabled(false);
			txtTips.setVisibility(View.VISIBLE);
			txtSummuy.setText("0");
		} else {
			listView.setSelection(lastItem);
			setTrashEnabled(true);
			txtTips.setVisibility(View.GONE);
			txtSummuy.setText(data.getTotal()+"");
		}
	}

	@Override
	public void onTaskStart(String taskName) {
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onTaskFinished(String taskName, Result result) {
		handlerException(result.getException());
		if (result.getCode()==HttpStatus.SC_OK) {
			if (taskName.equals(GET_COMMENT_TASK) && result.getValue()!=null) {
				Group<Comment> data = (Group<Comment>)result.getValue();
				addAllData(data);
				onRefresh(comments);
				listView.setSelection(lastItem);
				/*if (status == Constants.NEWS_DETAIL_SELF_COMMENT) {
					CacheUtil.saveCommentCache(userId, comments);
				} else {
					CacheUtil.saveCommentCache(newsId, comments);
				}*/
			} else if (taskName.equals(ADD_COMMENT_TASK)) {
				comments.clear();
				messageView.setText("");
				TaskData data = new TaskData(NotificationTask.NOTIFICATION_ADD_COMMENT_TASK, null);
				NewsApplication.getInstance().notifyObservers(data);
			}
		}
	}

	// 合并加载的数据
	private void addAllData(Group<Comment> data) {
		if (pageNo < data.getTotal_page()) {
			showMoreFooterView(true);
		} else {
			showMoreFooterView(false);
		}
		comments.addAll(data);
		comments.setCur_page(data.getCur_page());
		comments.setTotal(data.getTotal());
		comments.setTotal_page(data.getTotal_page());
		Collections.sort(comments);
	}
	
	// 显示加载更多
	private void showMoreFooterView(boolean shown) {
		if (shown) {
			moreView.removeAllViews();
			moreView.addView(footerView);
		} else {
			moreView.removeAllViews();
		}
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			lastItem = lastItem - 1; // 除去FootView部分视图
			// 当前滑动到最后一项且不是最后一页
			if ((lastItem == mAdapter.getCount()-1) && pageNo < comments.getTotal_page()) {
				pageNo++; // 页码累加
				sendRequest(); // 重新从网络加载数据
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		lastItem = firstVisibleItem + visibleItemCount - 1;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		comments.clear();
		if (mAdapter!=null) {
			mAdapter.deleteObservers();
		}
		NewsApplication.getInstance().removeObserver(this);
	}

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		comments.clear();
		sendRequest();
	}
}
