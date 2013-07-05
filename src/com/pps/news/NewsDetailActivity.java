package com.pps.news;

import java.util.List;
import org.apache.http.message.BasicNameValuePair;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.pps.news.app.BaseActivity;
import com.pps.news.bean.Comment;
import com.pps.news.bean.Group;
import com.pps.news.bean.News;
import com.pps.news.bean.Result;
import com.pps.news.constant.Constants;
import com.pps.news.network.BetterHttp;
import com.pps.news.parser.CommentParser;
import com.pps.news.task.GenericTask;
import com.pps.news.task.TaskListener;
import com.pps.news.util.CacheUtil;
import com.pps.news.util.ToastUtils;
import com.pps.news.util.UIUtil;
import com.pps.news.util.Utility;
import com.pps.news.widget.CommentPanel;
import com.pps.news.widget.SharePopupWindow;

public class NewsDetailActivity extends BaseActivity implements TaskListener, OnClickListener {

	private static final String GET_COMMENT_LIST_TASK = "GET_COMMENT_LIST_TASK";
	private static final String GET_ADD_COMMENT_TASK = "GET_ADD_COMMENT_TASK";
	
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
		News news = getIntent().getParcelableExtra("news");
		newsId = news.getInfo_id();
		txtTitle = (TextView)findViewById(R.id.news_detail_title);
		txtDate = (TextView)findViewById(R.id.news_detail_date);
		txtCommentNum = (TextView)findViewById(R.id.news_detail_video_num);
		txtSource = (TextView)findViewById(R.id.news_detail_src);
		txtDesc = (TextView)findViewById(R.id.news_detail_desc);
		txtSummuy = (TextView)findViewById(R.id.subTitle);
		iconDown = (ImageView)findViewById(R.id.ic_down);
		iconBack = (ImageView)findViewById(R.id.icon_back);
		iconBack.setOnClickListener(this);
		iconShare = (ImageView)findViewById(R.id.icon_share);
		iconShare.setOnClickListener(this);
		iconPost = (ImageView)findViewById(R.id.icon_comment);
		iconPost.setOnClickListener(this);
		commentPanel = (CommentPanel) findViewById(R.id.news_detail_comment);
		((TextView)findViewById(R.id.title)).setText(R.string.news_detail_comment_title);
		((ImageView)findViewById(R.id.ic_back)).setImageResource(R.drawable.ic_post);
		
		ensureUi(news);
		new GetNewsCommentsTask(this, GET_COMMENT_LIST_TASK).execute();
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
		if (taskName.equals(GET_COMMENT_LIST_TASK)) {
			if (result.getValue() != null) {
				Group<Comment> comments = (Group<Comment>) result.getValue();
				onRefresh(comments);
				CacheUtil.saveCommentCache(newsId, comments);
			}
		}
	}
	
	@Override
	public void onDestroy() {
		commentPanel.deleteObservers();
		super.onDestroy();
	}
	
	
	class GetNewsCommentsTask extends GenericTask {

		public GetNewsCommentsTask(TaskListener taskListener, String taskName) {
			super(taskListener, taskName);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected Result doInBackground(String... params) {
			// TODO Auto-generated method stub
			BetterHttp httpClient = BetterHttp.getInstance();
			String domain = "ppsios";
			Result result = httpClient.doHttpPost(Constants.getCommentByVideoId(), 
					new BasicNameValuePair("returntype", "json"),
					new BasicNameValuePair("encode", "utf8"),
					new BasicNameValuePair("pageID", "1"),					
					new BasicNameValuePair("domain", domain),
					new BasicNameValuePair("sign", UIUtil.md5ForString(domain+"vcommet")),
					new BasicNameValuePair("upload_id", String.valueOf(newsId)),
					new BasicNameValuePair("source", "mobile"),
					new BasicNameValuePair("user_agent", "PPSNews1.0"),
					new BasicNameValuePair("platform", "android"));
			// 测试数据
			Group<Comment> comments = new CommentParser().parse(Utility.readComment(NewsDetailActivity.this));
			result.addResult(comments);
			return result;
		}
	}
}
