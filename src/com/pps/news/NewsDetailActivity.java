package com.pps.news;

import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.pps.news.app.BaseActivity;
import com.pps.news.bean.Comment;
import com.pps.news.bean.Group;
import com.pps.news.bean.News;
import com.pps.news.bean.Result;
import com.pps.news.constant.Constants;
import com.pps.news.task.GetCommentsTask;
import com.pps.news.task.TaskListener;
import com.pps.news.util.CacheUtil;
import com.pps.news.util.ToastUtils;
import com.pps.news.widget.CommentPanel;
import com.pps.news.widget.SharePopupWindow;

public class NewsDetailActivity extends BaseActivity implements TaskListener, OnClickListener {

	private TextView txtTitle;
	private TextView txtDate;
	private TextView txtCommentNum;
	private TextView txtSource;
	private TextView txtDesc;
	private TextView txtSummuy;
	private ImageView iconDown;
	private CommentPanel commentPanel;
	private ImageView iconBack;
	private ImageView iconShare;
	private ImageView iconPost;
	
	private long newsId;
	private int lineCount;
	private int maxLine = 5;
	private boolean isReady;
	private boolean isExpand;
	
	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.news_detail);
		News news = getIntent().getParcelableExtra(Constants.NEWS_DETAIL_EXTRAS);
		newsId = news.getInfo_id();
		txtTitle = (TextView)findViewById(R.id.news_detail_title);
		txtDate = (TextView)findViewById(R.id.news_detail_date);
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
		
		ensureUi(news);
		new GetCommentsTask(this, null).execute(String.valueOf(newsId));
		txtDesc.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
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
		});
	}

	private void ensureUi(News news) {
		if (news != null) {
			txtTitle.setText(news.getMain_title());
			txtDate.setText(news.getShow_date());
			txtCommentNum.setText(String.valueOf(news.getStart_count()));
			txtSource.setText(getString(R.string.news_detail_source,news.getNews_from()));
			txtDesc.setText("\u3000\u3000"+news.getDesc_title());
			onRefresh(CacheUtil.getCommentCache(newsId));
		}
	}

	private void onRefresh(List<Comment> comments) {
		txtSummuy.setText(comments.size()+"");
		commentPanel.setItems(comments);
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
			SharePopupWindow popup = new SharePopupWindow(this);
			popup.showPopupWindow(findViewById(R.id.divide));
			popup.setOnClickListener(this);
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
	public void onTaskStart(String taskName) {
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onTaskFinished(String taskName, Result result) {
		notifyErrorMessage(result.getException());
		if (result.getValue() != null) {
			Group<Comment> comments = (Group<Comment>) result.getValue();
			onRefresh(comments);
			CacheUtil.saveCommentCache(newsId, comments);
		}
	}
	
	@Override
	public void onDestroy() {
		commentPanel.deleteObservers();
		super.onDestroy();
	}
}
