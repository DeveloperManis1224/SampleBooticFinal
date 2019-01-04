package com.app.adssan.ayucraze.model;

import android.graphics.drawable.Drawable;

public class MenuModel {

    Drawable image;
    private String name;
    private String uId;
    public boolean hasChildren, isGroup;


    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public MenuModel(Drawable image,String name,boolean isGroup, boolean hasChildren, String url) {
        this.image = image;
        this.name = name;
        this.uId = uId;
        this.hasChildren = hasChildren;
        this.isGroup = isGroup;
    }
    public MenuModel(String name,boolean isGroup, boolean hasChildren, String uId) {
        this.name = name;
        this.uId = uId;
        this.hasChildren = hasChildren;
        this.isGroup = isGroup;
    }
}


