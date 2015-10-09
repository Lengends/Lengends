package com.wyf.lengends.util;

public class StringUtil {
	
	/**
     * 全角字符转半角
     * @param input
     * @return
     */
    public static String toDBC(String input) {
		if (null != input) {
			char c[] = input.toCharArray();
			for (int i = 0; i < c.length; i++) {
				if ('\u3000' == c[i]) {
					c[i] = ' ';
				} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
					c[i] = (char) (c[i] - 65248);
				}
			}
			String dbc = new String(c);
			return dbc;
		} else {
			return null;
		}
	}

}
