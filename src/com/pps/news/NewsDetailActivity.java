package com.pps.news;

import java.util.Observable;
import java.util.Observer;
import org.apache.http.HttpStatus;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.pps.news.app.BaseActivity;
import com.pps.news.app.NewsApplication;
import com.pps.news.bean.Comment;
import com.pps.news.bean.Group;
import com.pps.news.bean.News;
import com.pps.news.bean.Result;
import com.pps.news.constant.Constants;
import com.pps.news.task.GetVideoCommentTask;
import com.pps.news.task.NotificationTask;
import com.pps.news.task.NotificationTask.TaskData;
import com.pps.news.task.TaskListener;
import com.pps.news.util.CacheUtil;
import com.pps.news.util.ToastUtils;
import com.pps.news.widget.CommentPanel;
import com.pps.news.widget.ShareDialog;

public class NewsDetailActivity extends BaseActivity implements TaskListener, 
	OnClickListener, OnGlobalLayoutListener, Observer {

	private TextView txtTitle;
	private TextView txtDate;
	private TextView emptyView;
	private TextView txtCommentNum;
	private TextView txtSource;
	private TextView txtDesc;
	private TextView txtSummuy;
	private ImageView iconDown;
	private ImageView iconBack;
	private ImageView iconShare;
	private ImageView iconPost;
	private CommentPanel commentPanel;
	
	private long newsId;
	private int pageNo = 1;
	private int lineCount;
	private int maxLine = 5;
	private boolean isReady;
	private boolean isExpand;
	private News itemNews;
	
	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.news_detail);
		NewsApplication.getInstance().addObserver(this);
		itemNews = getIntent().getParcelableExtra(Constants.NEWS_DETAIL_EXTRAS);
		newsId = itemNews.getInfo_id();
		txtTitle = (TextView)findViewById(R.id.news_detail_title);
		txtDate = (TextView)findViewById(R.id.news_detail_date);
		emptyView = (TextView)findViewById(android.R.id.empty);
		txtCommentNum = (TextView)findViewById(R.id.news_detail_video_num);
		txtSource = (TextView)findViewById(R.id.news_detail_src);
		txtDesc = (TextView)findViewById(R.id.news_detail_desc);
		txtSummuy = (TextView)findViewById(R.id.summary);
		iconDown = (ImageView)findViewById(R.id.ic_down);
		iconBack = (ImageView)findViewById(R.id.icon_back);
		iconBack.setOnClickListener(this);
		iconShare = (ImageView)findViewById(R.id.icon_share);
		iconShare.setOnClickListener(this);
		iconPost = (ImageView)findViewById(R.id.icon_comment);
		iconPost.setOnClickListener(this);
		commentPanel = (CommentPanel) findViewById(R.id.news_detail_comment);
		((TextView)findViewById(R.id.title)).setText(R.string.news_detail_comment_title);
		((ImageView)findViewById(R.id.icon)).setImageResource(R.drawable.ic_post);

		ensureUi(itemNews);
		sendRequest();
		txtDesc.getViewTreeObserver().addOnGlobalLayoutListener(this);
	}

	private void sendRequest() {
		new GetVideoCommentTask(this, null).execute(String.valueOf(newsId), 
				String.valueOf(pageNo), 
				Constants.NEWS_DETAIL_PAGESIZE);
	}
	
	private void ensureUi(News news) {
		if (news != null) {
			txtTitle.setText(news.getMain_title());
			txtDate.setText(news.getShow_date());
			txtCommentNum.setText(String.valueOf(news.getStart_count()));
			txtSource.setText(getString(R.string.news_detail_source,news.getNews_from()));
			txtDesc.setText("\u3000\u3000"+news.getDesc_title());
//			Group<Comment> data = CacheUtil.getCommentCache(newsId);
//			txtSummuy.setText(data.size()+"");
//			commentPanel.setItems(CacheUtil.getCommentCache(newsId));
			onRefresh(CacheUtil.getCommentCache(newsId));
		}
	}

	private void onRefresh(Group<Comment> data) {
		if (data!=null && data.size() > 0) {
			emptyView.setVisibility(View.GONE);
			txtSummuy.setText(data.getTotal()+"");
			commentPanel.setItems(data);
		} else {
			emptyView.setVisibility(View.VISIBLE);
		}
	}
	
	private void showAllText() {
		if (!isExpand) {
			isExpand = true;
			txtDesc.setMaxLines(lineCount);
			iconDown.setImageResource(R.drawable.ic_expand);
		} /*else {
			txtDesc.setMaxLines(maxLine);
			isExpand = false;
			iconDown.setImageResource(R.drawable.ic_down);
		}*/
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ic_down:
			showAllText();
			break;
		case R.id.icon_back:
			finish();
			break;
		case R.id.icon_share:
			/*SharePopupWindow popup = new SharePopupWindow(this);
			popup.showPopupWindow(findViewById(R.id.divide));
			popup.setOnClickListener(this);*/
			ShareDialog dialog = new ShareDialog(this, itemNews.getDesc_title());
			dialog.show();
			break;
		case R.id.icon_comment:
			Intent intent = new Intent(this, CommentActivity.class);
			intent.putExtra(Constants.NEWS_ID_EXTRAS, newsId);
			intent.putExtra(Constants.NEWS_DETAIL_EXTRAS, Constants.NEWS_DETAIL_FRIEND_COMMENT);
			startActivity(intent);
			break;
		case R.id.ic_sina:
		case R.id.ic_tencent:
		case R.id.ic_qzone:
			ToastUtils.showMessage(this, ((TextView)v).getText(), Toast.LENGTH_SHORT);
			break;
		}
	}
	
	@Override
	public void onGlobalLayout() {
		if (!isReady) {
			isReady = true;
			lineCount = txtDesc.getLineCount();
			txtDesc.setMaxLines(maxLine);
			if (lineCount <= maxLine) {
				iconDown.setVisibility(View.GONE);
			}
		}
	}
	
	@Override
	public void onTaskStart(String taskName) {
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onTaskFinished(String taskName, Result result) {
		handlerException(result.getException());
		if (result.getCode()==HttpStatus.SC_OK && result.getValue()!=null) {
			Group<Comment> comments = (Group<Comment>) result.getValue();
			onRefresh(comments);
			CacheUtil.saveCommentCache(newsId, comments);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		commentPanel.deleteObservers();
		NewsApplication.getInstance().removeObserver(this);
	}

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		if (data != null) {
			TaskData taskData = (TaskData) data; // 重新加载数据
			if (taskData.getKey().equals(NotificationTask.NOTIFICATION_ADD_COMMENT_TASK)) {
				sendRequest();
			}
		}
	}
}
