package com.org.lengend.tablayout;

import android.os.Bundle;
import android.widget.TextView;

import com.org.lengend.R;
import com.org.lengend.base.BaseFragment;

/**
 * Created by wangyanfei on 2016/6/1.
 */
public class HomeFragment extends BaseFragment{

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static HomeFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    protected int getLayoutResourcesId() {
        return R.layout.tab_fragment;
    }

    @Override
    protected void initView() {

        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText("Fragment # " + mPage);
    }
}
