package com.pps.news.wxapi;

import com.pps.news.NewsActivity;
import com.pps.news.R;
import com.pps.news.app.NewsApplication;
import com.pps.news.constant.Constants;
import com.pps.news.util.ToastUtils;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

/**
 * @file WXEntryActivity.java
 * @create 2013-8-15 上午10:43:29
 * @author lilong
 * @description TODO 处理微信的返回消息
 * 			注意事项：
 * 			1. 包名和类名必须为: {package_name}.wxapi.WXEntryActivity，并在manifest文件
 * 			        里面加上exported属性，设置为true；否则不能接收到微信的反馈消息；
 * 			2. 该类可以不被直接调用，发送消息过后微信会自动响应IWXAPIEventHandler事件；
 * 			        详见：http://open.weixin.qq.com/document/gettingstart/android/?lang=zh_CN
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
	
	// IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = NewsApplication.getInstance().getWXAPI();
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			goToGetMsg();		
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			break;
		default:
			break;
		}
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		int result = 0;
		
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = R.string.errcode_success;
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = R.string.errcode_cancel;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = R.string.errcode_deny;
			break;
		default:
			result = R.string.errcode_unknown;
			break;
		}
		Intent intent = new Intent(Constants.NEWS_SHARE_DIALOG_ACTION);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		ToastUtils.showMessage(this, result, Toast.LENGTH_SHORT);
		finish();
	}
	
	private void goToGetMsg() {
		Intent intent = new Intent(this, NewsActivity.class);
		intent.putExtras(getIntent());
		startActivity(intent);
		finish();
	}
}