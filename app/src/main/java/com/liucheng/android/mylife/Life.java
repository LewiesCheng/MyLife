package com.liucheng.android.mylife;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

/**
 * Created by liucheng on 2017/5/17.
 */

public class Life {
    private UUID mId;
    private String mTitle;
    private String mFeel;
    private Date mDate;
    private String mPicturePath;

    public Life(){
        this(UUID.randomUUID());
    }

    public Life(UUID id){
        this.mId = id;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getFeel() {
        return mFeel;
    }

    public void setFeel(String feel) {
        mFeel = feel;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getPicturePath(){
        return mPicturePath;
    }

    public void setPicturePath(String picturePath){
        mPicturePath = picturePath;
    }

    public void addPicturePath(String newPath){
        if (mPicturePath == null){
            mPicturePath = newPath;
        } else {
            mPicturePath = mPicturePath + "&" + newPath;
        }
    }
}
