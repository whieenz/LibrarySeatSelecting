package com.example.asus.libraryseatselecting;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class cancelActivity extends AppCompatActivity {

    private String name="";
    private String id="";
    private String roomid="";
    private String seatid="";
    private String relationObjectId="";
    private TextView welcome;
    private TextView nameText,roomText,lineText,rowText,hour,min,second;
    int timeSec=0;
    int timeMin=0;
    int timeHour=0;
    private util myUtil=new util();

    private Handler handler=new Handler(){
        public void handleMessage(android.os.Message msg){
            //数据库数据修改成功后，更新UI
            afterUpdate(name,id,roomid);
        }
    };

    /**得到关系表数据后*/
    //点击注销按钮时
    private Handler handler3=new Handler(){
        public void handleMessage(android.os.Message msg){
            T_relation myrelation=(T_relation) msg.obj;
            String myrelationObjectId=myrelation.getObjectId();
            exit(myrelationObjectId);
        }
    };
    //点击取消选座按钮时
    private Handler handler4=new Handler(){
        public void handleMessage(android.os.Message msg){
            T_relation myrelation=(T_relation) msg.obj;
            String myrelationObjectId=myrelation.getObjectId();
            String myseatid=myrelation.getSeatid();
            doCancel(myseatid,myrelationObjectId);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);
        //获取传递参数
        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        id=intent.getStringExtra("id");
        roomid=intent.getStringExtra("roomid");
        seatid=intent.getStringExtra("seatid");
        relationObjectId=intent.getStringExtra("relationObjectId");
        //进入页面开始计时
        handler2.postDelayed(runnable, 1000); //每隔1s执行

        //进入页面就开始监控地理位置
        locationChangedListener(cancelActivity.this);


        //初始化页面
        initPage();

    }

    /**
     * 计时器的实现
     */
    Handler handler2 = new Handler();
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                handler.postDelayed(this, 1000);
                timeSec++;
                if(timeSec%60==0){
                    timeMin++;
                    min.setText(trans2time(timeMin));
                    if(timeMin%60==0){
                        timeHour++;
                        hour.setText(trans2time(timeHour));
                    }
                }
                second.setText(trans2time(timeSec%60));
                System.out.println("do...");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("exception...");
            }
        }
    };

    /**
     * 将数值转换为时间格式
     */
    private String trans2time(int time){
        time=time%60;
        if(time<10)
            return "0"+time;
        else return ""+time;
    }

    /**
     * 初始化页面
     */
    private void initPage(){
        welcome=(TextView)findViewById(R.id.welcomeText);
        nameText=(TextView)findViewById(R.id.nameText);
        roomText=(TextView)findViewById(R.id.roomText);
        lineText=(TextView)findViewById(R.id.lineText);
        rowText=(TextView)findViewById(R.id.rowText);
        hour=(TextView)findViewById(R.id.hour);
        min=(TextView)findViewById(R.id.min);
        second=(TextView)findViewById(R.id.second);
        welcome.setText(name);
        nameText.setText(name+nameText.getText());
        roomText.setText(myUtil.roomid2room(roomid));
        //获取行列值
        int line=0;
        int row=0;
        int btnid=Integer.parseInt(seatid.substring(6));
        line=(int)(btnid/7)+1;
        row=btnid%7;
        if(row==0){
            row=7;
        }
        String linetext="第"+num2word(line)+"行";
        String rowtext="第"+num2word(row)+"列";
        lineText.setText(linetext);
        rowText.setText(rowtext);

    }

    private String num2word(int num){
        String result="";
        switch(num){
            case 1:
                result="一";
                break;
            case 2:
                result="二";
                break;
            case 3:
                result="三";
                break;
            case 4:
                result="四";
                break;
            case 5:
                result="五";
                break;
            case 6:
                result="六";
                break;
            case 7:
                result="七";
                break;
            case 8:
                result="八";
                break;
            case 9:
                result="九";
                break;
        }
        return result;
    }
//
//    public void clickExit(View view){
//        myUtil.exit(relationObjectId);
//        startActivity(new Intent(cancelActivity.this, login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//        finish();
//    }
    /**
     * 取消选座
     */
    public void cancel(View view){
        getRelationObjectId(id,1);
    }
    public void doCancel(String myseatid,String myrelationobjectid){
        if(myseatid==null||myseatid.equals("")){
            myUtil.showToast("还未选择座位，无需取消~");
            return;
        }
        //首先更改数据库的信息
        //更改关系表信息
        T_relation myrelation = new T_relation();
        myrelation.setSeatid("");
        myrelation.setRoomid("");
        myrelation.update(myrelationobjectid, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    handler.sendEmptyMessage(1);
                }else{
                    myUtil.showToast("网络状态不佳或者还未选择座位哦~");
                }
            }

        });
    }

    /**
     * 数据库数据修改成功后，更新UI
     */
    private void afterUpdate(String myname,String myid,String myroomid){
        myUtil.showToast("成功取消选座~");
        returnData(seatid);

        //返回选座页面
        Intent intent=new Intent(cancelActivity.this,seatActivity.class);
        intent.putExtra("name",myname);
        intent.putExtra("id",myid);
        intent.putExtra("roomid",myroomid);
        startActivity(intent);
        finish();
    }

    /**
     * 回传信息
     */
    private void returnData(String myseatid){
        Intent intent=new Intent();
        intent.putExtra("seatid",myseatid);
        setResult(1001, intent);
    }

    public void locate(View view){
        String location=myUtil.getLocation(cancelActivity.this);
        if(!location.equals("")){
            myUtil.showMap(cancelActivity.this,location);
        }
    }

    /**
     * 点击注销按钮
     * @param view
     */
    public void clickExit(View view){
        getRelationObjectId(id,2);

    }
    public void exit(String myrelationobjectid){
        myUtil.exit(myrelationobjectid);
        startActivity(new Intent(cancelActivity.this, login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    /**
     * 获取关系表中的信息
     * @param userid
     */
    private void getRelationObjectId(String userid,int type){
        //获取关系表信息
        final int mytype=type;
        final Message message2 = handler.obtainMessage();
        BmobQuery<T_relation> ralationQuery = new BmobQuery<T_relation>();
        ralationQuery.addWhereEqualTo("userid", userid);
        ralationQuery.findObjects(new FindListener<T_relation>() {
            @Override
            public void done(List<T_relation> relationList, BmobException e) {
                if (e == null) {
                    final T_relation myrelation;
                    if (!relationList.isEmpty()) {
                        myrelation = relationList.get(0);
                    } else {
                        myrelation = null;
                    }
                    message2.obj = myrelation;
                    if(mytype==1){
                        //点击取消座位按钮时
                        handler4.sendMessage(message2);
                    }else if(mytype==2){
                        //点击注销按钮时
                        handler3.sendMessage(message2);
                    }

                } else {
                    myUtil.showToast("获取关系表信息失败~"+e.getLocalizedMessage());
                }
            }
        });
    }

    /*********************************************************处理地理位置变化事件开始***************************************************************/
    //定义允许离开的最长时间，以秒记
    private final int LEAVE_TIME=1800;
    //定义离开的时间，以秒记
    private int currentLeaveTime=0;
    //定义隔多久监测一次，以秒记
    private final int CHECK_TIME=10;

    private Handler handler5=new Handler(){
        public void handleMessage(android.os.Message msg){
            int type=msg.arg1;
            switch(type){
                case 1://移动之后超过允许范围
                case 3://未获取到地理位置数据
                    currentLeaveTime+=CHECK_TIME;
                    break;
                case 2://移动之后回到允许范围
                    currentLeaveTime=0;
                    break;
            }
            //若离开时间超过允许时间,则取消选座
            if(currentLeaveTime>LEAVE_TIME){
                getRelationObjectId(id,1);
            }
        }
    };

    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */
    LocationListener locationListener =  new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }
        @Override
        public void onProviderEnabled(String provider) {

        }
        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            //如果位置发生变化,重新计算距离
            final Message message = handler.obtainMessage();
            String address=myUtil.getLocation(cancelActivity.this);
            if(address!=null&&!address.equals("")){
                String[] add=address.split(",");
                double lng=Double.valueOf(add[0]);
                double lat=Double.valueOf(add[1]);
                //若移动之后超过合理范围
                if(myUtil.getDistance(lat,lng)){
                    message.arg1=1;
                }else{
                    message.arg1=2;
                }
            }else{
                message.arg1=3;
            }
            handler5.sendMessage(message);
        }
    };

    private LocationManager locationManager;
    private String locationProvider;

    public void locationChangedListener(Context context){

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
            myUtil.showToast("请先打开网络或者GPS哦~");
            return;
        }

        //监视地理位置变化
        try{
            locationManager.requestLocationUpdates(locationProvider, CHECK_TIME*1000, 1, locationListener);
        }catch(SecurityException e){
            //地理位置授权被拒绝
            myUtil.showToast(e.getMessage());
        }

    }

    /*********************************************************处理地理位置变化事件结束***************************************************************/

}
