package com.pps.news.common;

import java.io.ByteArrayOutputStream;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;

/**
 * @file WXShare.java
 * @create 2013-8-12 下午03:40:10
 * @author lilong 
 * @description TODO 封装分享到微信的常用操作
 * 
 * 		微信开放平台使用要点：
 * 			1. 确保应用的APPID，MD5码，包名和开放平台注册的保持一致，否则不会跳转至微信界面；
 * 			2. MD5码可以采用keytool工具生成 ： keytool -list -alias 别名 -keystore 签名文件 -storepass 密钥 -keypass 密钥 > out.txt
 * 			        从out.txt文件中提取MD5码，注意填写到开放平台时候需要去掉中间的分隔符冒号
 */
public class WXShare {

	// 微信开发者APP_ID
	private static final String APP_ID = "wxd5e8675b8e8d0006";
	// 微信朋友圈支持的版本>4.2
	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
	
	private static final int THUMB_SIZE = 150;
	
	private WXShare() {
	}

	/**
	 * 将该app注册到微信 
	 * @param context
	 * @return IWXAPI
	 */
	public static IWXAPI registerAppId(Context context) {
		IWXAPI api = WXAPIFactory.createWXAPI(context, APP_ID, false);
		api.registerApp(APP_ID);
		return api;
	}
	
	/**
	 * 检测当前微信的版本，大于4.2版本才支持分享到朋友圈 
	 * @param api
	 * @return true 大于4.2版本，false 小于4.2版本
	 */
	public static boolean checkWXSupportAPI(IWXAPI api) {
		int wxSdkVersion = api.getWXAppSupportAPI(); 
		return wxSdkVersion >= TIMELINE_SUPPORTED_VERSION;
	}
	
	/**
	 * 分享文本到微信或朋友圈 
	 * @param api	IWXAPI
	 * @param text	分享的文字
	 * @param isTimelineCb 是否发送到朋友圈，(true 发送朋友圈，false 发送给指定的好友)
	 * @return	true 发送成功，false 发送失败
	 */
	public static boolean sendTextToWX(IWXAPI api, String text, boolean isTimelineCb) {
		WXTextObject textObj = new WXTextObject();
		textObj.text = text;

		// 用WXTextObject对象初始化一个WXMediaMessage对象
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		// 发送文本类型的消息时，title字段不起作用
		// msg.title = "Will be ignored";
		msg.description = textObj.text;

		// 构造一个Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis()); // transaction字段用于唯一标识一个请求
		req.message = msg;
		// 是否发送到朋友圈
		req.scene = isTimelineCb ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
		// 调用api接口发送数据到微信
		return api.sendReq(req);
	}
	
	/**
	 * 分享图片到微信或朋友圈 
	 * @param api	IWXAPI
	 * @param bmp	分享的图片
	 * @param isTimelineCb 是否发送到朋友圈，(true 发送朋友圈，false 发送给指定的好友)
	 * @return	true 发送成功，false 发送失败
	 */
	public static boolean sendImageToWX(IWXAPI api, Bitmap bmp, boolean isTimelineCb) {
		WXImageObject imgObj = new WXImageObject(bmp);
		
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;
		
		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
		bmp.recycle();
		msg.thumbData = bmpToByteArray(thumbBmp, true);  // 设置缩略图

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = isTimelineCb ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
		return api.sendReq(req);
	}
	
	private static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
