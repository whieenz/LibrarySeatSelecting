package com.example.asus.libraryseatselecting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.os.Handler;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ReadingRoomActivity extends AppCompatActivity {

    private String name="";
    private String id="";
    private String relationObjectId="";
    private String roomid="";
    private String seatId="";
    private TextView welcome;
    private util myUtil=new util();

    /**得到关系表数据后*/
    //点击姓名或头像时
    private Handler handler=new Handler(){
        public void handleMessage(android.os.Message msg){
            T_relation relation=(T_relation) msg.obj;
            relationObjectId=relation.getObjectId();
            seatId=relation.getSeatid();
            roomid=relation.getRoomid();
            //页面跳转
            jump();
        }
    };

    //点击注销按钮时
    private Handler handler2=new Handler(){
        public void handleMessage(android.os.Message msg){
            T_relation relation=(T_relation) msg.obj;
            String myrelationObjectId=relation.getObjectId();
            exit(myrelationObjectId);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_room);

        //get message from login activity
        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        id=intent.getStringExtra("id");
//        relationObjectId=intent.getStringExtra("relationObjectId");
//        userObjectId=intent.getStringExtra("userObjectId");
        welcome=(TextView)findViewById(R.id.welcomeText);
        welcome.setText(name);
    }

    public void clickRoom(View view){
        Intent intent = new Intent(this,seatActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("id",id);
        switch(view.getId()){
            case R.id.button1:
                roomid="00000001";
                break;
            case R.id.button2:
                roomid="00000002";
                break;
            case R.id.button3:
                roomid="00000003";
                break;
            case R.id.button4:
                roomid="00000004";
                break;
            case R.id.button5:
                roomid="00000005";
                break;
            case R.id.button6:
                roomid="00000006";
                break;
        }
        intent.putExtra("roomid",roomid);
        startActivity(intent);
//        /**
//         * 如果希望启动另一个Activity，并且希望有返回值，则需要使用startActivityForResult这个方法，
//         * 第一个参数是Intent对象，第二个参数是一个requestCode值，如果有多个按钮都要启动Activity，则requestCode标志着每个按钮所启动的Activity
//         */
//        startActivityForResult(intent, 1000);
    }
//
//    /**
//     * 所有的Activity对象的返回值都是由这个方法来接收
//     * requestCode:    表示的是启动一个Activity时传过去的requestCode值
//     * resultCode：表示的是启动后的Activity回传值时的resultCode值
//     * data：表示的是启动后的Activity回传过来的Intent对象
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//        //从选座页面取消页面返回
//        if(requestCode == 1000 && resultCode == 1001)
//        {
//            seatObjectId = data.getStringExtra("seatObjectId");
//            btnid = data.getStringExtra("btnid");
//            roomName = data.getStringExtra("room");
//        }
//    }

    /**
     * 点击注销啊按钮
     * @param view
     */
    public void clickExit(View view){
        getRelationObjectId(id,2);

   }

   public void exit(String myrelationobjectid){
       myUtil.exit(myrelationobjectid);
       startActivity(new Intent(ReadingRoomActivity.this, login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
       finish();
   }

    /**
     * 点击姓名或头像时跳转到已选座信息页面
     * @param view
     */
    public void clickName(View view){
        getRelationObjectId(id,1);
    }

    public void jump(){
        Intent intent = new Intent(this,cancelActivity.class);
        if(seatId!=null&&!seatId.equals("")){
            intent.putExtra("seatid",seatId);
        }else{
            myUtil.showToast("您还未选择座位~");
            return;
        }
        intent.putExtra("relationObjectId",relationObjectId);
        intent.putExtra("name",name);
        intent.putExtra("roomid",roomid);
        intent.putExtra("id",id);

        startActivity(intent);
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
                        //点击姓名或头像时
                        handler.sendMessage(message2);
                    }else if(mytype==2){
                        //点击注销按钮时
                        handler2.sendMessage(message2);
                    }

                } else {
                    myUtil.showToast("获取关系表信息失败~"+e.getLocalizedMessage());
                }
            }
        });
    }

    /**
     * 点击定位图标
     * @param view
     */
    public void locate(View view){
        String location=myUtil.getLocation(ReadingRoomActivity.this);
        if(!location.equals("")){
            myUtil.showMap(ReadingRoomActivity.this,location);
        }
    }
}
