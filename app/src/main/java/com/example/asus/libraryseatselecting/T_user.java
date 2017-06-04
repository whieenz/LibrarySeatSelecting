package com.example.asus.libraryseatselecting;

import cn.bmob.v3.BmobObject;

/**
 * Created by asus on 2017/3/14.
 */

public class T_user extends BmobObject {
    private String name;
    private String ID;
    private String password;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
