package com.org.lengend.recycleview.entity;

/**
 * Created by wangyanfei on 2016/6/17.
 */
public class ItemData {
    public static final int ITEM_TYPE_5 = 0;        //title类型
    public static final int ITEM_TYPE_1 = 1;        //轮播类型
    public static final int ITEM_TYPE_2 = 2;        //每行两个  宽 > 高
    public static final int ITEM_TYPE_3 = 3;        //每行两个  宽 < 高
    public static final int ITEM_TYPE_4 = 4;        //每行3个
    public static final int ITEM_TYPE_6 = 6;        //横向滑动ListView

    private int type;
    private Object data;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public <T> T getData(Class<T> tClass) {
        return (T)data;
    }
}
