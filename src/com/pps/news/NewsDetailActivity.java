package com.pps.news;

import org.apache.http.message.BasicNameValuePair;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.pps.news.util.UIUtil;
import com.pps.news.util.Utility;
import com.pps.news.widget.CommentPanel;

public class NewsDetailActivity extends BaseActivity implements TaskListener, OnClickListener {

	private static final String GET_COMMENT_LIST_TASK = "GET_COMMENT_LIST_TASK";
	private static final String GET_ADD_COMMENT_TASK = "GET_ADD_COMMENT_TASK";
	
	private TextView txtTitle;
	private TextView txtDate;
	private TextView txtCommentNum;
	private TextView txtSource;
	private TextView txtDesc;
	private ImageView iconBack;
	private ImageView iconShare;
	private ImageView iconDown;
	private TextView repostBtn;
	private CommentPanel commentPanel;
	private View repostBar;
	private EditText repostMsg;
	private TextView sendBtn;
	
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

		txtTitle = (TextView) findViewById(R.id.news_detail_title);
		txtDate = (TextView) findViewById(R.id.news_detail_date);
		txtCommentNum = (TextView) findViewById(R.id.news_detail_video_num);
		txtSource = (TextView) findViewById(R.id.news_detail_src);
		txtDesc = (TextView) findViewById(R.id.news_detail_desc);
		iconBack = (ImageView) findViewById(R.id.back);
		iconBack.setOnClickListener(this);
		iconShare = (ImageView) findViewById(R.id.share);
		iconShare.setOnClickListener(this);
		iconDown = (ImageView) findViewById(R.id.ic_down);
		iconDown.setOnClickListener(this);
		repostBtn = (TextView) findViewById(R.id.repost_btn);
		repostBtn.setOnClickListener(this);
		commentPanel = (CommentPanel) findViewById(R.id.news_detail_comment);
		repostBar = findViewById(R.id.news_repost_bar);
		repostMsg = (EditText) findViewById(R.id.edt_comment_conent);
		sendBtn = (TextView) findViewById(R.id.btn_comment_send);
		sendBtn.setOnClickListener(this);
		
		onRefeshUi(news);
		new GetNewsCommentsTask(this, GET_COMMENT_LIST_TASK).execute();
		
		txtDesc.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (!isReady) {
					isReady = true;
					lineCount = txtDesc.getLineCount();
					txtDesc.setMaxLines(maxLine);
					if (lineCount <= maxLine) {
						iconDown.setVisibility(View.INVISIBLE);
					}
				}
			}
		});
	}
	
	private void onRefeshUi(News news) {
		if (news != null) {
			txtTitle.setText(news.getMain_title());
			txtDate.setText(news.getShow_date());
			txtCommentNum.setText(String.valueOf(news.getStart_count()));
			txtSource.setText(getString(R.string.news_detail_source,news.getNews_from()));
			txtDesc.setText("\u3000\u3000"+news.getDesc_title());
			commentPanel.setItems(CacheUtil.getCommentCache(newsId));
		}
	}
	
	private void showAllText() {
		if (!isExpand) {
			isExpand = true;
			txtDesc.setMaxLines(lineCount);
			iconDown.setImageResource(R.drawable.ic_up);
		} else {
			txtDesc.setMaxLines(maxLine);
			isExpand = false;
			iconDown.setImageResource(R.drawable.ic_down);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.ic_down:
			showAllText();
			break;
		case R.id.repost_btn:
			repostBar.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_comment_send:
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
				commentPanel.setItems(comments);
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
