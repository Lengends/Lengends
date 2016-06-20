package com.org.lengend.tablayout;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.org.lengend.R;
import com.org.lengend.base.BaseActivity;

/**
 * Created by wangyanfei on 2016/6/1.
 */
public class TabLayoutActivity extends BaseActivity{
    public static final String TAG = "TabLayout";

    private ViewPager viewPager;
    private TabFragmentPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tablayout);
    }

    @Override
    protected void initView() {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(),
                TabLayoutActivity.this);
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_launcher);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_launcher);
//        tabLayout.getTabAt(2).setIcon(R.drawable.ic_launcher);
//        tabLayout.getTabAt(3).setIcon(R.drawable.ic_launcher);
        tabLayout.removeAllTabs();
        tabLayout.addTab(getTab(tabLayout, 0));
        tabLayout.addTab(getTab(tabLayout, 1));
        tabLayout.addTab(getTab(tabLayout, 2));
        tabLayout.addTab(getTab(tabLayout, 3));
    }

    private TabLayout.Tab getTab(TabLayout tabLayout, int index){

        TabLayout.Tab tab = tabLayout.newTab();
        CharSequence titleText = adapter.getPageTitle(index);
        View tabView = View.inflate(this,R.layout.tab,null);
        tab.setCustomView(tabView);
        tab.setIcon(R.drawable.ic_launcher);
        tab.setText(titleText);
        return tab;
    }
}
