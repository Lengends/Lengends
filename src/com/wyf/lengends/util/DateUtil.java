package com.wyf.lengends.util;

import java.util.Calendar;

public class DateUtil {
	
	/**
	 * 获取给定的时间是礼拜几
	 * @param pTime
	 * @return
	 */
	public static String getWeek(long pTime) {
		String Week = "星期";
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(pTime);
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			Week += "日";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 2) {
			Week += "一";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 3) {
			Week += "二";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 4) {
			Week += "三";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 5) {
			Week += "四";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 6) {
			Week += "五";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 7) {
			Week += "六";
		}
		return Week;
	}

}
