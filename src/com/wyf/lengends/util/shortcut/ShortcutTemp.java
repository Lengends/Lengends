package com.wyf.lengends.util.shortcut;

import android.content.Context;

/**
 * 在应用图标上添加数字
 * @author wyf
 *
 */
public class ShortcutTemp {
	
	/***
     * 在应用图标的快捷方式上加数字
     * @param num 显示的数字：整型
     * 
     */
	public static void createShortCut(Context activity,int num) {
		try {
			if(num > 99){
				num = 99;
			}else if(num < 0){
				num = 0;
			}
			ShortcutBadger.setBadge(activity.getApplicationContext(), num);
		} catch (ShortcutBadgeException e) {
		}
	}

}
