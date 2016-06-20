package com.org.lengend.base;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyanfei on 2016/5/12.
 */
public class DataUtils {

    public static ArrayList<DataEntity> getTestData(int count){
        ArrayList<DataEntity> data = new ArrayList<DataEntity>();
        for (int i = 0; i < count; i++){
            DataEntity entity = new DataEntity();
            entity.setTitle("测试数据Title===>" + i);
            entity.setDesc("测试数据Desc===>" + i);
            data.add(entity);
        }
        return data;
    }

    public static List<String> getUrls(int count){
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++){
            list.add("http://b.hiphotos.baidu.com/image/pic/item/023b5bb5c9ea15cec72cb6d6b2003af33b87b22b.jpg");
        }
        return list;
    }

    public static List<String> getUrls(int count,String url){
        if(TextUtils.isEmpty(url)){
            url = "http://pic0.mofang.com/2014/1113/20141113110312116.jpg";
        }
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++){
            list.add(url);
        }
        return list;
    }

}
