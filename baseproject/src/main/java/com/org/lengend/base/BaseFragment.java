package com.org.lengend.base;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wangyanfei on 2016/4/15.
 */
public abstract class BaseFragment extends Fragment {
    protected View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutResourcesId(), container, false);
        return rootView;
    }

    protected View findViewById(int resId){
        return rootView.findViewById(resId);
    }

    /**
     * 获取当前页面布局资源ID
     * @return
     */
    protected abstract int getLayoutResourcesId();

    /**
     * 初始化控件
     */
    protected abstract void initView();
}
