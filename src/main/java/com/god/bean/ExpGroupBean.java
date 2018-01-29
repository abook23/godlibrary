package com.god.bean;

import java.util.List;

/**
 * Created by abook23 on 2016/9/14.
 */
public class ExpGroupBean {
    private Object imageUrl;//
    private String title;
    private String describe;
    private String time;
    private int count;
    private List<ExpChildBean> childs;

    public Object getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Object imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<ExpChildBean> getChilds() {
        return childs;
    }

    public void setChilds(List<ExpChildBean> childs) {
        this.childs = childs;
    }
}
