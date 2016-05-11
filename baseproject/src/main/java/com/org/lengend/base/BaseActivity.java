package com.org.lengend.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by wangyanfei on 2016/5/10.
 */
public abstract class BaseActivity extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
    }

    protected abstract void initView();
}
