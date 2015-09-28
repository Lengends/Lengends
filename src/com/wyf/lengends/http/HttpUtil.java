package com.wyf.lengends.http;


import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RangeFileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 网络请求帮助类
 * @author wyf
 *
 */
public class HttpUtil {
	private static final AsyncHttpClient httpClient = new AsyncHttpClient();
	public static PersistentCookieStore myCookieStore;  
	static {
		// httpClient.setMaxRetriesAndTimeout(1, 30 * 1000);
		httpClient.setTimeout(15000);
		httpClient.setMaxConnections(10);
		httpClient.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
	}

	protected static AsyncHttpClient getHttpClient() {
		return httpClient;
	}
	
	private static void init(Context context) {
		if(myCookieStore == null){
			myCookieStore = new PersistentCookieStore(context);  
			httpClient.setCookieStore(myCookieStore);
		}
	}
	
	public static void clearCookies(){
		if(myCookieStore != null){
			myCookieStore.clear();
			myCookieStore = null;
		}
	}
	
	/**
	 * 设置上传服务器的公共参数
	 * @param context
	 * @param params
	 */
	private static void setParams(Context context, RequestParams params) {
		params.put("userId", "123");
	}
	
	/**
	 * post请求,如果设备网络不可用直接回调ResponseHandler中listener的onFailure方法
	 * @param url
	 * @param params
	 * @param responseHandler 如果网络不可用,回调中的entity.type为传入type,entity.error_code为BaseEntity.ERROR_CODE_NET_DISABLE,
	 */
	public final static void postRequest(RequestParams params, ResponseHandler responseHandler) {
        AsyncHttpClient httpClient = getHttpClient();
        final Context context = responseHandler.getContext();
        init(context);
        String url = responseHandler.getUrl();
        if (httpClient != null ) {
        	if(params == null){
        		params = new RequestParams();
        	}
        	setParams(context, params);
            httpClient.post(url, params, responseHandler);
        }
    }
	
	public final static void getRequest(ResponseHandler responseHandler) {
		// TODO Auto-generated method stub
		String url = responseHandler.getUrl();
		httpClient.get(url, responseHandler);
	}
	
	/**
	 * 文件下载
	 * @param context
	 * @param url
	 * @param responseHandler
	 */
	public static void downloadFile(Context context,String url ,RangeFileAsyncHttpResponseHandler responseHandler) {
		AsyncHttpClient httpClient = getHttpClient();
		init(context);
		if (httpClient != null ) {
			httpClient.get(url, responseHandler);
		}
	}
}
