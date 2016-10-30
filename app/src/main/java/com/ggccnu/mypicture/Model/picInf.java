package com.ggccnu.mypicture.Model;

import cn.bmob.v3.BmobObject;

/**
 * Created by lishaowei on 2016/10/30.
 */
public class picInf extends BmobObject {
    private String itemUrl;
    private String picUrl;

    public picInf(String itemUrl, String picUrl) {
        this.itemUrl = itemUrl;
        this.picUrl = picUrl;
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
