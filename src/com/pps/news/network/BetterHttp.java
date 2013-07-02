package com.pps.news.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import com.pps.news.bean.Result;
import com.pps.news.constant.Constants;
import android.util.Log;

/**
 * @file BetterHttp.java
 * @create 2013-6-4 下午03:58:04
 * @author lilong dramxsky@gmail.com
 * @description TODO 自定义参数的HttpClient，提供httpGet，httpPost两种传送消息的方式
 */
public class BetterHttp {
	private static final boolean DEBUG = Constants.DEVELOP_MODE;
	 //日志输出
    private static final String TAG = "BetterHttp";
    
    // 默认参数设置 
    public static final int CONNECTION_TIMEOUT = 5 * 1000;
    public static final int CON_TIME_OUT_MS= 5 * 1000;
    public static final int SO_TIME_OUT_MS= 5 * 1000;
    public static final int MAX_CONNECTIONS_PER_HOST = 2;
    public static final int MAX_TOTAL_CONNECTIONS = 5;
    
    private HttpClient httpClient;
    
    private static BetterHttp instance;
    
    /**
     * 单例模式创建BetterHttp实例
     * @return BetterHttp对象
     */
    public synchronized static BetterHttp getInstance() {
    	if (instance == null) {
    		instance = new BetterHttp();
    	}
    	return instance;
    }
    
    
    private BetterHttp(){
        this(MAX_CONNECTIONS_PER_HOST,MAX_TOTAL_CONNECTIONS,CON_TIME_OUT_MS,SO_TIME_OUT_MS);
    }
    
    
    private BetterHttp(int maxConnectionPerHost, int maxTotalConnections, int conTimeOutMs, int soTimeOutMs){
        SchemeRegistry supportedSchemes = new SchemeRegistry();         
        supportedSchemes.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        
        try {
            SSLSocketFactory sslSocketFactory=SSLSocketFactory.getSocketFactory();
            sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            supportedSchemes.register(new Scheme("https",new EasySSLSocketFactory(), 443));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Prepare parameters.
        HttpParams httpParams = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(httpParams, maxTotalConnections);
        ConnPerRouteBean connPerRoute = new ConnPerRouteBean(maxConnectionPerHost); 
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, connPerRoute); 
        
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
        HttpProtocolParams.setUseExpectContinue(httpParams, false);
        
        HttpConnectionParams.setConnectionTimeout(httpParams, conTimeOutMs);
        HttpConnectionParams.setSoTimeout(httpParams, soTimeOutMs);
        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
        
        ClientConnectionManager connectionManager = new ThreadSafeClientConnManager(httpParams,supportedSchemes);
        HttpClientParams.setCookiePolicy(httpParams, CookiePolicy.BROWSER_COMPATIBILITY);
        httpClient=new DefaultHttpClient(connectionManager, httpParams);
    }

    private Result executeHttpRequest(HttpRequestBase httpRequest)  {
        if (DEBUG) Log.i(TAG, "doHttpRequest [1] url = " + httpRequest.getURI()); 
        
        try {
        	httpRequest.addHeader("Accept-Encoding", "gzip");
        	httpClient.getConnectionManager().closeExpiredConnections();
        	
        	double start=System.currentTimeMillis();
	        HttpResponse response = httpClient.execute(httpRequest);
	        double end=System.currentTimeMillis();
	        if (DEBUG) Log.i(TAG, "doHttpRequest [2] time = " + ((end - start) / 1000) + "s");
	
	        int statusCode = response.getStatusLine().getStatusCode();
	        if (DEBUG) Log.i(TAG, "doHttpRequest [3] statusLine = " + statusCode);
	        Result result=new Result();
	        result.setCode(statusCode);

	        switch (statusCode) {
		        case 200:
		        	InputStream is = getUngzippedContent(response.getEntity());
	                BufferedReader reader=new BufferedReader(new InputStreamReader(is));
	    			StringBuilder builder = new StringBuilder();
	    			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
	    				builder.append(s);
	    			}
	    			is.close();
	    			reader.close();
	    			
	    			if (DEBUG) Log.i(TAG, "doHttpRequest [4] responseData = " + builder.toString());
	    			result.setMessage(builder.toString());
	    			return result;
		        default:
	        	  String message="Default case for status code reached: " + response.getStatusLine().toString();
	        	  HttpEntity entity = response.getEntity();
	        	  if (entity != null) {
	        		  entity.consumeContent();	
	        	  }
	        	  result.setException(new Exception(message));
	        	  return result;
	        }
        } catch (IOException ex) {
        	ex.printStackTrace();
        	Result result=new Result();
			result.setException(ex);
			return result;
        } finally {
        	httpRequest.abort();
        }
    }
    
    /**
     * GET方式请求数据
     * @param url	请求链接URL
     * @param nameValuePairs 请求参数列表
     * @return 响应字符串
     */
    public Result doHttpGet(String url, NameValuePair... nameValuePairs) {
    	if (nameValuePairs.length > 0) {
    		url = url + "?" + URLEncodedUtils.format(Arrays.asList(nameValuePairs), HTTP.UTF_8);
    	}
    	HttpGet httpGet = new HttpGet(url);
    	return executeHttpRequest(httpGet);
    }
    
    /**
     * POST方式请求数据
     * @param url	请求链接URL
     * @param nameValuePairs 请求参数列表
     * @return  响应字符串
     */
    public Result doHttpPost(String url, NameValuePair... nameValuePairs) {
    	 HttpPost httpPost = new HttpPost(url);
         try {
             httpPost.setEntity(new UrlEncodedFormEntity(Arrays.asList(nameValuePairs), HTTP.UTF_8));
         } catch (UnsupportedEncodingException ex) {
             throw new IllegalArgumentException("Unable to encode http parameters.");
         }
         return executeHttpRequest(httpPost);
    }
    
    private InputStream getUngzippedContent(HttpEntity entity) throws IOException {
        InputStream responseStream = entity.getContent();
        if (responseStream == null) {
            return responseStream;
        }
        Header header = entity.getContentEncoding();
        if (header == null) {
            return responseStream;
        }
        String contentEncoding = header.getValue();
        if (contentEncoding == null) {
            return responseStream;
        }
        if (contentEncoding.contains("gzip")) {
            responseStream = new GZIPInputStream(responseStream);
        }
        return responseStream;
    }
}
