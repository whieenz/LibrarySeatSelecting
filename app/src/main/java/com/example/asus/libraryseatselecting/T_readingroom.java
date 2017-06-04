package com.example.asus.libraryseatselecting;

import cn.bmob.v3.BmobObject;

/**
 * Created by asus on 2017/3/14.
 */

public class T_readingroom extends BmobObject {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
