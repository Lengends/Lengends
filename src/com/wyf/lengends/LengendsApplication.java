package com.wyf.lengends;

import android.app.Application;

import com.bugtags.library.Bugtags;

public class LengendsApplication extends Application{

	@Override
    public void onCreate() {        
        super.onCreate();        
        //在这里初始化
        Bugtags.start("6d988d1a62d21b2d4fe02eb3ec88831f", this, Bugtags.BTGInvocationEventBubble);
    }
}
