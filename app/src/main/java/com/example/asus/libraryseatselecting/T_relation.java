package com.example.asus.libraryseatselecting;

import cn.bmob.v3.BmobObject;

/**
 * Created by asus on 2017/3/14.
 */

public class T_relation extends BmobObject {
    private String userid;
    private String seatid;
    private String login_flag;
    private String roomid;

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSeatid() {
        return seatid;
    }

    public void setSeatid(String seatid) {
        this.seatid = seatid;
    }

    public String getLogin_flag() {
        return login_flag;
    }

    public void setLogin_flag(String login_flag) {
        this.login_flag = login_flag;
    }
}
