package com.org.lengend.recycleview.entity;

/**
 * 轮播Layout
 * Created by wangyanfei on 2016/6/17.
 */
public class ItemLayout1 {
    private String title;
    private String imageUrl;
    private String linkUrl;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    @Override
    public String toString() {
        return "title : " + title + "\nimageUrl : " + imageUrl;
    }
}
