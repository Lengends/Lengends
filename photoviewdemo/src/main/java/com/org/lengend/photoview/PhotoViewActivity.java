package com.org.lengend.photoview;

import android.os.Bundle;

import com.org.lengend.base.BaseActivity;
import com.org.lengend.photoview.library.PhotoView;

/**
 * Created by wangyanfei on 2016/5/30.
 */
public class PhotoViewActivity extends BaseActivity{

    private PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_view_activity);
    }

    @Override
    protected void initView() {
        photoView = (PhotoView) findViewById(R.id.photoView);
        photoView.setImageResource(R.drawable.photo);
//        final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);
//        attacher.update();
    }
}
