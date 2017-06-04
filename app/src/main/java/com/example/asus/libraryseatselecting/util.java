package com.example.asus.libraryseatselecting;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.view.Gravity;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by asus on 2017/4/4.
 */

public class util{

    /**
     * 显示提示信息
     * @param str
     */
    public void showToast(String str){
        Toast toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);//显示时间较短
        toast.setGravity(Gravity.CENTER, 0, 0);// 居中显示
        toast.show();
    }

    /**
     * 点击注销按钮事件
     * @param relationObjectId
     */

    public void exit(String relationObjectId){
        //首先删除关系表中的记录
        if(relationObjectId!=null&&!relationObjectId.equals("")) {
            T_relation relation = new T_relation();
            relation.setObjectId(relationObjectId);
            relation.delete(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                    } else {
                        showToast("删除失败：" + e.getMessage());
                    }
                }
            });
        }
    }

    //先获取LocationManager 然后根据可用的位置提供器获取Location，代码如下

    private LocationManager locationManager;

    private static Location location;
    private String locationProvider;
    /** 定位，获取经纬度 */
    public String getLocation(Context context){

        String result="";

        //获取地理位置管理器
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);

        if(providers.contains(LocationManager.GPS_PROVIDER)){

            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;

        }else if(providers.contains(LocationManager.NETWORK_PROVIDER)){
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        }else{
            //没有可用
            showToast("请先打开网络或者GPS哦~");
            return "";
        }

        location=getBestLocation(locationManager,locationProvider);
        if(location!=null){
            result=location.getLongitude()+","+location.getLatitude();
            System.out.println(result);
        }else{
            showToast("获取当前地理位置失败了~");
        }

//   //监视地理位置变化 --不需要用到
//        try{
//            locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
//        }catch(SecurityException e){
//            //地理位置授权被拒绝
//            showToast(e.getMessage());
//        }

        return result;

    }
    /**
     * 获取location对象，优先以GPS_PROVIDER获取location对象，当以GPS_PROVIDER获取到的locaiton为null时
     * ，则以NETWORK_PROVIDER获取location对象，这样可保证在室内开启网络连接的状态下获取到的location对象不为空
     *
     * @param locationManager
     * @return
     */
    private Location getBestLocation(LocationManager locationManager,String provider) {
        Location result = null;
        try{
            if (locationManager != null) {
                result = locationManager.getLastKnownLocation(provider);
                if (result != null) {
                    return result;
                } else {
                    result = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    return result;
                }
            }
        }catch(SecurityException e){
            //地理位置授权被拒绝
            showToast(e.getMessage());
        }
        return result;
    }





    //地球半径
    private final double EARTH_RADIUS = 6378.137;

    //宿舍经纬度 114.385544,30.476409
    //文波楼经纬度  114.388094,30.47869
    //图书馆经纬度  114.385001,30.473913
    private final double LIBRARY_LAT = 30.476409;
    private final double LIBRARY_LNG = 114.385544;
    private final double AREA=200;

    /**
     * 获取两个地点之间的距离
     * @param lat1 第一点纬度
     * @param lng1 第一点经度
     * @return 两点之间的距离
     */
    public boolean getDistance(double lat1, double lng1){
        double radLat1 = rad(lat1);
        double radLat2 = rad(LIBRARY_LAT);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(LIBRARY_LNG);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        //千米转换成米
        s=s*1000;
        if(s<=AREA){
            return true;
        }else{
            return false;
        }
    }
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    /**
     * 在地图上显示用户的当前位置
     * @param location
     */
    private final String userMapKey="3a173262c3eeaedf0d61905c1bd50071";
    public void showMap(Context context,String location){
        String url="http://m.amap.com/navi/?dest="+location+"&destName=我的位置&key="+userMapKey;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
        context.startActivity(intent);
    }

    public String roomid2room(String roomid){
        String room="";
        char id=roomid.charAt(7);
        switch(id){
            case '1':
                room="综合阅览室一";
                break;
            case '2':
                room="综合阅览室二";
                break;
            case '3':
                room="综合阅览室三";
                break;
            case '4':
                room="综合阅览室四";
                break;
            case '5':
                room="综合阅览室五";
                break;
            case '6':
                room="综合阅览室六";
                break;
        }
        return room;
    }

}
