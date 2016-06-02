package com.org.lengend.tablayout;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.org.lengend.base.log.Logger;

/**
 * Created by wangyanfei on 2016/6/1.
 */
public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

    private final String tabTitles[] = new String[] { "六一", "儿童节", "七一", "建军节"};
    final int PAGE_COUNT = tabTitles.length;
    private Context context;

    public TabFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Logger.d(TabLayoutActivity.TAG , "==getItem==" + position);
        return HomeFragment.newInstance(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

}
