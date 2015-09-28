package com.wyf.lengends.http;

import org.apache.http.Header;
import org.json.JSONException;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;
/**
 * 网络请求结果处理类
 * @author wyf
 */
public class ResponseHandler extends AsyncHttpResponseHandler{

	private Context context;			//上下文
	private IResultListener listener;	//返回结果回调接口
	private String url;					//当前请求的URL（BaseURL除外）
	private Object tag;         		//用来标示同一个URL请求时的不同状态或其他
	private int type ;					//请求类型（用于请求返回后的判断）
	private BaseJsonParser parse;		//数据解析器
	
	/**
	 * 
	 * @param context	上下文
	 * @param url		当前请求的URL（除BaseURL）
	 * @param tag		请求标示（如果没需求可以传null）
	 * @param type		标记当前（本次）请求
	 * @param parse		数据解析器
	 * @param listener	数据返回监听器
	 */
	public ResponseHandler(Context context, String url,Object tag,int type ,BaseJsonParser parse, IResultListener listener) {
		this.parse = parse;
		this.listener = listener;
		this.context = context;
		this.url = url;
		this.tag = tag;
		this.type = type;
	}
	
	@Override
	public void onProgress(long bytesWritten, long totalSize) {
		super.onProgress(bytesWritten, totalSize);
	}
	/**
	 * 数据返回成功
	 */
	@Override
	public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		if(listener != null){
			HttpBaseEntity entity = null;
			String json = new String(arg2);
			try {
				entity = parse.parseInBackground(json);
			} catch (JSONException e) {
				if(entity == null){
					entity = new HttpBaseEntity();
				}
			}
			entity.type = type;
			entity.tag = tag;
			entity.url = url;
			if(entity.error_code == 0){ //成功
				listener.onSuccessful(entity);
			}else{ //失败
				listener.onFailure(entity);
			}
		}
	}
	
	/**
	 * 数据返回失败
	 */
	@Override
	public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		if(listener != null){
			HttpBaseEntity entity = new HttpBaseEntity();
			entity.type = type;
			entity.tag = tag;
			entity.url = url;
			entity.error_code = arg0;
			listener.onFailure(entity);
		}
	}

	public IResultListener getListener() {
		return listener;
	}

	public int getType() {
		return type;
	}
	
	public Context getContext() {
		return context;
	}
	public String getUrl() {
		return url;
	}

}
