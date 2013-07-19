package com.pps.news.app;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;
import com.pps.news.util.Log;
import com.pps.news.util.ToastUtils;

/**
 * @file BaseActivity.java
 * @create 2013-6-4 下午03:44:27
 * @author lilong dramxsky@gmail.com
 * @description TODO 自定义Activity基类
 */
public abstract class BaseActivity extends FragmentActivity {

	private String TAG = BaseActivity.class.getSimpleName();

	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.TAG = getClass().getSimpleName();
		_onCreate(savedInstanceState);
		addNavagationAndStatus();
		ViewServer.get(this).addWindow(this);
	}

	protected abstract void _onCreate(Bundle savedInstanceState);

	@Override
	protected void onRestart() {
		Log.d(TAG, "onRestart");
		super.onRestart();
	}

	@Override
	protected void onStart() {
		Log.d(TAG, "onStart");
		super.onStart();
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
		ViewServer.get(this).setFocusedWindow(this);
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
		ViewServer.get(this).removeWindow(this);
	}

	// 添加导航组件
	protected void addNavagationAndStatus() {
	}
	
	
	public final void showFragment(final int pane, final Fragment fragment, final boolean add_to_backstack) {
		final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(pane, fragment);
		if (add_to_backstack) ft.addToBackStack(null);
		ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
	}
	
	/** 处理回调异常信息  */
	public void handlerException(Exception ex) {
		if (ex != null) {
			if (ex instanceof ConnectTimeoutException) {
				ToastUtils.showMessage(this, "网络连接超时", Toast.LENGTH_SHORT);
			} else if (ex instanceof UnknownHostException ||
					ex instanceof HttpHostConnectException) {
				ToastUtils.showMessage(this, "网络连接异常", Toast.LENGTH_SHORT);
			} else if (ex instanceof SocketTimeoutException) {
				ToastUtils.showMessage(this, "读取数据失败", Toast.LENGTH_SHORT);
			} else {
				ToastUtils.showMessage(this, ex.getLocalizedMessage(), Toast.LENGTH_SHORT);
			}
		}
	}
}
