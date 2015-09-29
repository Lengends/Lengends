package com.wyf.lengends.util;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Build;

public class Tools {
	
	public static boolean hasFroyo() {
		// Can use static final constants like FROYO, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed
		// behavior.
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	public static boolean hasIcecreamSandwich() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	}

	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= 17;
	}

	public static boolean hasKitKat() {
		return Build.VERSION.SDK_INT >= 19;
	}
	
	
	public static int dip2pix(Context context, int dips) {
		int densityDpi = context.getResources().getDisplayMetrics().densityDpi;
		return (dips * densityDpi) / 160;
	}

	public static int pix2dip(Context context, int pixs) {
		int densityDpi = context.getResources().getDisplayMetrics().densityDpi;
		return (pixs * 160) / densityDpi;
	}
	/**
	 * 提交Editor， 针对版本不同，提交方式不一样。 从api9开始，建议使用apply，是异步的。提速
	 * @param editor
	 */
	public static void commitEditor(Editor editor) {
		if(editor == null) 
			return;
		if(hasGingerbread()) {
			editor.apply();
		} else {
			editor.commit();
		}
	}
	
	
}
