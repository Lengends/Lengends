package com.wyf.lengends.http;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
/**
 * 这是Json的基本框架格式，所有的实体数据放在data中
 * 		{
 *		  error_code: 0,
 *		  error_msg: "",
 *		  data: {
 *		    //实体数据
 *		  }
 *		}
 *@author wyf
 */
public abstract class BaseJsonParser {
	
	protected JsonParser parser;
	
	protected abstract Object parseInfo(String json)throws JSONException;
	
	public HttpBaseEntity parseInBackground(String json) throws JSONException {
		HttpBaseEntity entity = new HttpBaseEntity();
		if(TextUtils.isEmpty(json)){
			return entity;
		}
		parser = new JsonParser();
		JSONObject obj = new JSONObject(json);
		entity.error_code = parser.getIntValue(obj, "error_code");
		entity.error_msg = parser.getStringValue(obj, "error_msg");
		entity.isSuccess = "success".equals(parser.getStringValue(obj, "result"));
		try {
			entity.data = parseInfo(parser.getStringValue(obj, "data"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}
}
