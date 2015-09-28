package com.wyf.lengends.http;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * json解析类
 * @author wyf
 *
 */
public class JsonParser implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JsonParser() {
	}
	
	/**
	 * 获取String类型的值
	 * @param key
	 * @return
	 */
	public String getStringValue(JSONObject jsonObject, String key) {
		if(jsonObject == null){
			return null;
		}
		String value = "";
		if(jsonObject.has(key)){
			try {
				value = jsonObject.getString(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	
	/**
	 * 获取Int类型的值
	 * @param key
	 * @return
	 */
	public int getIntValue(JSONObject jsonObject, String key) {
		if(jsonObject == null){
			return 0;
		}
		int value = 0;
		if(jsonObject.has(key)){
			try {
				value = jsonObject.getInt(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	/**
	 * 获取double类型的值
	 * @param key
	 * @return
	 */
	public double getDoubleValue(JSONObject jsonObject, String key) {
		if(jsonObject == null){
			return 0;
		}
		double value = 0;
		if(jsonObject.has(key)){
			try {
				value = jsonObject.getDouble(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	
	/**
	 * 获取double类型的值
	 * @param key
	 * @return
	 */
	public long getLongValue(JSONObject jsonObject, String key) {
		if(jsonObject == null){
			return 0;
		}
		long value = 0;
		if(jsonObject.has(key)){
			try {
				value = jsonObject.getLong(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	
	/**
	 * 获取boolean类型的值
	 * @param key
	 * @return
	 */
	public boolean getBooleanValue(JSONObject jsonObject, String key) {
		if(jsonObject == null){
			return false;
		}
		boolean value = false;
		if(jsonObject.has(key)){
			try {
				value = jsonObject.getBoolean(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	
	/**
	 * 获取JSONArray类型的值
	 * @param key
	 * @return
	 */
	public JSONArray getJSONArrayValue(JSONObject jsonObject, String key) {
		if(jsonObject == null){
			return null;
		}
		JSONArray value = null;
		if(jsonObject.has(key)){
			try {
				value = jsonObject.getJSONArray(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	
	/**
	 * 获取JSONObject类型的值
	 * @param key
	 * @return
	 */
	public JSONObject getJSONObjectValue(JSONObject jsonObject, String key) {
		if(jsonObject == null){
			return null;
		}
		JSONObject value = null;
		if(jsonObject.has(key)){
			try {
				value = jsonObject.getJSONObject(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	
	/**
	 * 获取Object类型的值
	 * @param key
	 * @return
	 */
	public Object getObjectValue(JSONObject jsonObject, String key) {
		if(jsonObject == null){
			return null;
		}
		Object value = null;
		if(jsonObject.has(key)){
			try {
				value = jsonObject.get(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	
//	private void getObjectKeys(JSONObject jsonObject) {
//		if(jsonObject == null){
//			return null;
//		}
//	}	
	
	
	
	
	

}
