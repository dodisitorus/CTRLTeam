package com.dodi.ctrlteam.model;

public class Materi {

    private String mImageUrl;
    private String mInfo;
    private String mSubTitle;
    private String mTitle;

    public Materi(String mImageUrl, String mInfo, String mSubTitle, String mTitle) {
        this.mImageUrl = mImageUrl;
        this.mInfo = mInfo;
        this.mSubTitle = mSubTitle;
        this.mTitle = mTitle;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
    public String getInfo() {
        return mInfo;
    }
    public void setInfo(String info) {
        mInfo = info;
    }
    public String getSubTitle() {
        return mSubTitle;
    }
    public void setSubTitle(String subTitle) {
        mSubTitle = subTitle;
    }
    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        mTitle = title;
    }

}
