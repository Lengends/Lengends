package com.wyf.lengends.util.shortcut.imp;

import android.content.Context;
import android.content.Intent;

import com.wyf.lengends.util.shortcut.ShortcutBadgeException;
import com.wyf.lengends.util.shortcut.ShortcutBadger;

/**
 */
public class AdwHomeBadger extends ShortcutBadger {

    public static final String INTENT_UPDATE_COUNTER = "org.adw.launcher.counter.SEND";
    public static final String PACKAGENAME = "PNAME";
    public static final String COUNT = "COUNT";

    public AdwHomeBadger(Context context) {
        super(context);
    }

    @Override
    protected void executeBadge(int badgeCount) throws ShortcutBadgeException {

        Intent intent = new Intent(INTENT_UPDATE_COUNTER);
        intent.putExtra(PACKAGENAME, getContextPackageName());
        intent.putExtra(COUNT, badgeCount);
        mContext.sendBroadcast(intent);
    }
}
