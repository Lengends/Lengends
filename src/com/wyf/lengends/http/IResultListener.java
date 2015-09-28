package com.wyf.lengends.http;


/**
 * 网络请求数据返回监听接口
 * @author wyf
 *
 */
public interface IResultListener {
	public void onSuccessful(HttpBaseEntity entity);
	public void onFailure(HttpBaseEntity entity);
}
