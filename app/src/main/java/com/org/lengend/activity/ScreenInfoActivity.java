package com.org.lengend.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.widget.TextView;

import com.org.lengend.R;
import com.org.lengend.base.BaseActivity;
import com.org.lengend.utils.SystemPhoneUtil;

/**
 * Created by wangyanfei on 2016/6/2.
 */
public class ScreenInfoActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_info_activity);
        TextView screenInfoText = (TextView) findViewById(R.id.screenInfoText);

        Rect screenW_H = SystemPhoneUtil.getScreenWidthAndHight(this);
        StringBuilder sb = new StringBuilder();
        sb.append("屏幕宽：").append(screenW_H.right).append("\n");
        sb.append("屏幕高：").append(screenW_H.bottom).append("\n");
        sb.append("density：").append(SystemPhoneUtil.getPhoneDensity(this));

        screenInfoText.setText(sb.toString());

    }

    @Override
    protected void initView() {

    }
}
