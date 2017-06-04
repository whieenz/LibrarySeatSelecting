package com.example.asus.libraryseatselecting;

import cn.bmob.v3.BmobObject;

/**
 * Created by asus on 2017/3/14.
 */

public class T_seat extends BmobObject {
    private String id;
    private String roomid;
    private String flag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
