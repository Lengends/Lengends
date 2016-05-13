package com.org.lengend.base;

import java.util.ArrayList;

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

}
