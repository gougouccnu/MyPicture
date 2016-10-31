package com.ggccnu.mypicture.Model;

import cn.bmob.v3.BmobObject;

/**
 * Created by lishaowei on 2016/10/30.
 */
public class picInf extends BmobObject {
    private String itemUrl;
    private String picUrl;
    private MyUser user;
    private MyUser starter;

    public picInf(String itemUrl, String picUrl, MyUser starter, MyUser user) {
        this.itemUrl = itemUrl;
        this.picUrl = picUrl;
        this.starter = starter;
        this.user = user;
    }

    public void setStarter(MyUser starter) {
        this.starter = starter;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public MyUser getStarter() {
        return starter;
    }

    public MyUser getUser() {
        return user;
    }

    public picInf(String tableName, String itemUrl, String picUrl, MyUser starter, MyUser user) {
        super(tableName);
        this.itemUrl = itemUrl;
        this.picUrl = picUrl;
        this.starter = starter;
        this.user = user;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }
}
