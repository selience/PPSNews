package com.pps.news;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.http.HttpStatus;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.pps.news.adapter.CommentsListAdapter;
import com.pps.news.app.BaseActivity;
import com.pps.news.app.NewsApplication;
import com.pps.news.bean.Comment;
import com.pps.news.bean.Group;
import com.pps.news.bean.Result;
import com.pps.news.constant.Constants;
import com.pps.news.task.DeleteCommentTask;
import com.pps.news.task.GetUserCommentTask;
import com.pps.news.task.NotificationTask;
import com.pps.news.task.TaskListener;
import com.pps.news.task.NotificationTask.TaskData;
import com.pps.news.util.UIUtil;

public class CommentEditActivity extends BaseActivity implements OnClickListener, TaskListener, OnItemClickListener, OnScrollListener {
	private static final String GET_COMMENT_TASK = "GET_COMMENT_TASK";
	private static final String DELETE_COMMENT_TASK = "DELETE_COMMENT_TASK";
	
	private View editView;
	private TextView txtSelect;
	private ListView listView;
	private TextView txtTips;
	private View footerView;
	private ViewGroup moreView;
	
	private int pageNo = 1; //初始状态页码
	private int lastItem = 0; //最后一项索引
	private Comment delComment; // 当前删除的评论对象
	private String ipString = null;
	private CommentsListAdapter mAdapter;
	private DeleteCommentTask deleteTask;
	private Group<Comment> comments = null;
	// 保存选中的Comment对象
	private List<Comment> dataList = new ArrayList<Comment>();
	
	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.comment_edit_layout);
		findViewById(R.id.cancel).setOnClickListener(this);
		editView = findViewById(R.id.edit);
		editView.setOnClickListener(this);
		txtTips=(TextView)findViewById(android.R.id.empty);
		listView=(ListView)findViewById(android.R.id.list);
		listView.setOnScrollListener(this);
		txtSelect =(TextView)findViewById(R.id.txtSelect);
		listView.setOnItemClickListener(this);

		setSummary(0);
		ipString = UIUtil.getIPAddress(this);
		moreView = new FrameLayout(this);
		listView.addFooterView(moreView);
		this.comments = new Group<Comment>();
		footerView = View.inflate(this, R.layout.list_footer_view, null);
		sendRequest();
	}

	// 发送网络请求
	private void sendRequest() {
		new GetUserCommentTask(this, GET_COMMENT_TASK).execute(String.valueOf(pageNo), Constants.COMMENTS_LIST_PAGESIZE);
	}
	
	// 刷新视图列表
	private void onRefresh(List<Comment> data) {
		if (mAdapter == null) {
			mAdapter = new CommentsListAdapter(this, data);
			listView.setAdapter(mAdapter);
		} else {
			mAdapter.addAll(data);
			mAdapter.notifyDataSetChanged();
		}

		if (data==null || data.isEmpty()) {
			setSummary(0);
			editView.setEnabled(false);
			txtTips.setVisibility(View.VISIBLE);
		} else {
			editView.setEnabled(true);
			txtTips.setVisibility(View.GONE);
		}
	}
	
	// 设置选中项数目
	private void setSummary(int size) {
		txtSelect.setText(getString(R.string.comment_edit_bar_label, size));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			finish();
			break;
		case R.id.edit:
			if (dataList.size() > 0) {
				delCommentTask();
			}
			break;
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
			} else if (taskName.equals(DELETE_COMMENT_TASK)) {
				if (delComment!=null && comments.contains(delComment)) {
					dataList.remove(delComment);
					comments.remove(delComment);
				} 
				delCommentTask();
			}
		}
	}

	// 循环删除，确保删除完成后刷新
	private void delCommentTask() {
		if (dataList.size() > 0) {
			delComment = dataList.get(0);
			deleteTask = new DeleteCommentTask(this, DELETE_COMMENT_TASK, delComment);
			deleteTask.execute(ipString);
		} else {
			dataList.clear();
			mAdapter.clearAll(); 
			setSummary(0);
			onRefresh(comments);
			// 更新评论列表
			TaskData taskData = new TaskData(NotificationTask.NOTIFICATION_DELETE_COMMENT_TASK, null);
			NewsApplication.getInstance().notifyObservers(taskData);
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
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Comment comment = (Comment) parent.getItemAtPosition(position);
		CheckBox ck = (CheckBox) view.findViewById(R.id.ckSelect);
		ck.setChecked(!ck.isChecked());
		if (ck.isChecked()) {
			dataList.add(comment);
		} else {
			if (dataList.contains(comment)) {
				dataList.remove(comment);
			}
		}
		setSummary(dataList.size());
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			lastItem = lastItem - 1; // 除去FootView部分视图
			// 当前滑动到最后一项且不是最后一页
			if ((lastItem == mAdapter.getCount()-1) && pageNo < comments.getTotal_page()) {
				pageNo++; // 页码累加
				sendRequest();
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		lastItem = firstVisibleItem + visibleItemCount - 1;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mAdapter!=null) {
			mAdapter.deleteObservers();
		}
		deleteTask = null;
		delComment = null;
		comments.clear();
		dataList.clear();
	}
}
