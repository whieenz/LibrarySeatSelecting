package com.example.asus.libraryseatselecting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class seatActivity extends AppCompatActivity {

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button button10;
    private Button button11;
    private Button button12;
    private Button button13;
    private Button button14;
    private Button button15;
    private Button button16;
    private Button button17;
    private Button button18;
    private Button button19;
    private Button button20;
    private Button button21;
    private Button button22;
    private Button button23;
    private Button button24;
    private Button button25;
    private Button button26;
    private Button button27;
    private Button button28;
    private Button button29;
    private Button button30;
    private Button button31;
    private Button button32;
    private Button button33;
    private Button button34;
    private Button button35;
    private Button button36;
    private Button button37;
    private Button button38;
    private Button button39;
    private Button button40;    
    private Button button41;
    private Button button42;
    private Button button43;
    private Button button44;
    private Button button45;
    private Button button46;
    private Button button47;
    private Button button48;
    private Button button49;
    private Button button50;
    private Button button51;
    private Button button52;
    private Button button53;
    private Button button54;
    private Button button55;
    private Button button56;
    private Button button57;
    private Button button58;
    private Button button59;
    private Button button60;
    private Button button61;
    private Button button62;
    private Button button63;

    private String name="";
    private String id="";
    private String roomid="";
    private String relationObjectId="";
    private String userObjectId="";
    private String seatId="";
    private T_relation relation;
    private util myUtil=new util();
    private int btnid=0;

    private TextView welcome;
    private TextView roomText;
    private StringBuffer flag=new StringBuffer();//需两个校验都通过后，才执行选座操作，用于记录次数
    private StringBuffer flag2=new StringBuffer();//需两个校验都通过后，才执行选座操作，用于记录次数


    private Handler handler=new Handler(){
        public void handleMessage(android.os.Message msg){
            System.out.println(msg.obj);
            relation=(T_relation)msg.obj;
            String myseatid="";
            if(relation.getSeatid()!=null){
                myseatid=relation.getSeatid().toString();
            }
            checkUser(myseatid);
        }
    };

//    private Handler handler2=new Handler(){
//        public void handleMessage(android.os.Message msg){
//            T_relation myrelation=(T_relation)msg.obj;
//            String myseatid="";
//            if(myrelation!=null){
//                myseatid=myrelation.getSeatid().toString();
//            }
//            checkSeat(myseatid);
//        }
//    };

    Boolean done1=false;
    private Handler handler3=new Handler(){
        public void handleMessage(android.os.Message msg){
            //两次校验都通过，开始选座
            if(!done1){
                if(flag.indexOf("ab")!=-1||flag.indexOf("ba")!=-1){
                    done1=true;
                    seatSelect(seatId,roomid,relation.getObjectId().toString());
                }
            }
        }
    };

    //更新座位信息成功后操作
    private Handler handler4=new Handler(){
        public void handleMessage(android.os.Message msg){
            afterUpdate(btnid);

        }
    };

    /**得到关系表数据后*/
    //点击姓名或头像时
    private Handler handler5=new Handler(){
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
    private Handler handler6=new Handler(){
        public void handleMessage(android.os.Message msg){
            T_relation relation=(T_relation) msg.obj;
            String myrelationObjectId=relation.getObjectId();
            exit(myrelationObjectId);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);
        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        id=intent.getStringExtra("id");
        roomid=intent.getStringExtra("roomid");

        //初始化页面
        initPage();
    }

    /**
     * 初始化页面
     */
    private void initPage(){
        //显示用户姓名
        welcome=(TextView)findViewById(R.id.welcomeText);
        welcome.setText(name);
        roomText=(TextView)findViewById(R.id.roomText);
        roomText.setText(myUtil.roomid2room(roomid));
        //初始化按钮
        initButton();
        initSeat();

    }

    /**
     * 初始化座位
     */
    private void initSeat(){
        //从关系表中查询座位的选取情况,查出该阅览室所有已被选取的座位
        BmobQuery<T_relation> relationQuery = new BmobQuery<T_relation>();
        relationQuery.addWhereEqualTo("roomid",roomid);
        relationQuery.findObjects(new FindListener<T_relation>(){
            @Override
            public void done(List<T_relation> relations, BmobException e) {
                if(e==null){
                    if(relations.isEmpty()){
                        return;
                    }else{
                        //切换已选座位图标
                        for(T_relation relation:relations){
                            String seatid=relation.getSeatid();
                            seatid=seatid.substring(6);
                            if (seatid.charAt(0)=='0') {
                                seatid=seatid.substring(1);
                            }
                            int btnid=Integer.parseInt(seatid);
                            //改变座位选择状态
                            changeBtn(btnid);
                        }
                    }
                }else{
                    myUtil.showToast("查询关系信息错误"+ e.getMessage());
                }
            }
        });
    }

    /**
     * 根据id来更改按钮背景
     */
    private void changeBtn(int id){
        switch(id){
            case 1:
                button1.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 2:
                button2.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 3:
                button3.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 4:
                button4.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 5:
                button5.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 6:
                button6.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 7:
                button7.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 8:
                button8.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 9:
                button9.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 10:
                button10.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 11:
                button11.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 12:
                button12.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 13:
                button13.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 14:
                button14.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 15:
                button15.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 16:
                button16.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 17:
                button17.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 18:
                button18.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 19:
                button19.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 20:
                button20.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 21:
                button21.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 22:
                button22.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 23:
                button23.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 24:
                button24.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 25:
                button25.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 26:
                button26.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 27:
                button27.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 28:
                button28.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 29:
                button29.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 30:
                button30.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 31:
                button31.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 32:
                button32.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 33:
                button33.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 34:
                button34.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 35:
                button35.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 36:
                button36.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 37:
                button37.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 38:
                button38.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 39:
                button39.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 40:
                button40.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 41:
                button41.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 42:
                button42.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 43:
                button43.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 44:
                button44.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 45:
                button45.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 46:
                button46.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 47:
                button47.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 48:
                button48.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 49:
                button49.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 50:
                button50.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 51:
                button51.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 52:
                button52.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 53:
                button53.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 54:
                button54.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 55:
                button55.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 56:
                button56.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 57:
                button57.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 58:
                button58.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 59:
                button59.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 60:
                button60.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 61:
                button61.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 62:
                button62.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            case 63:
                button63.setBackground(this.getResources().getDrawable(R.drawable.seatred));
                break;
            default : break;
            
        }
    }

    /**
     * 初始化按钮
     */
    private void initButton(){
        button1=(Button)findViewById(R.id.button1);
        button2=(Button)findViewById(R.id.button2);
        button3=(Button)findViewById(R.id.button3);
        button4=(Button)findViewById(R.id.button4);
        button5=(Button)findViewById(R.id.button5);
        button6=(Button)findViewById(R.id.button6);
        button7=(Button)findViewById(R.id.button7);
        button8=(Button)findViewById(R.id.button8);
        button9=(Button)findViewById(R.id.button9);
        button10=(Button)findViewById(R.id.button10);
        button11=(Button)findViewById(R.id.button11);
        button12=(Button)findViewById(R.id.button12);
        button13=(Button)findViewById(R.id.button13);
        button14=(Button)findViewById(R.id.button14);
        button15=(Button)findViewById(R.id.button15);
        button16=(Button)findViewById(R.id.button16);
        button17=(Button)findViewById(R.id.button17);
        button18=(Button)findViewById(R.id.button18);
        button19=(Button)findViewById(R.id.button19);
        button20=(Button)findViewById(R.id.button20);
        button21=(Button)findViewById(R.id.button21);
        button22=(Button)findViewById(R.id.button22);
        button23=(Button)findViewById(R.id.button23);
        button24=(Button)findViewById(R.id.button24);
        button25=(Button)findViewById(R.id.button25);
        button26=(Button)findViewById(R.id.button26);
        button27=(Button)findViewById(R.id.button27);
        button28=(Button)findViewById(R.id.button28);
        button29=(Button)findViewById(R.id.button29);
        button30=(Button)findViewById(R.id.button30);
        button31=(Button)findViewById(R.id.button31);
        button32=(Button)findViewById(R.id.button32);
        button33=(Button)findViewById(R.id.button33);
        button34=(Button)findViewById(R.id.button34);
        button35=(Button)findViewById(R.id.button35);
        button36=(Button)findViewById(R.id.button36);
        button37=(Button)findViewById(R.id.button37);
        button38=(Button)findViewById(R.id.button38);
        button39=(Button)findViewById(R.id.button39);
        button40=(Button)findViewById(R.id.button40);
        button41=(Button)findViewById(R.id.button41);
        button42=(Button)findViewById(R.id.button42);
        button43=(Button)findViewById(R.id.button43);
        button44=(Button)findViewById(R.id.button44);
        button45=(Button)findViewById(R.id.button45);
        button46=(Button)findViewById(R.id.button46);
        button47=(Button)findViewById(R.id.button47);
        button48=(Button)findViewById(R.id.button48);
        button49=(Button)findViewById(R.id.button49);
        button50=(Button)findViewById(R.id.button50);
        button51=(Button)findViewById(R.id.button51);
        button52=(Button)findViewById(R.id.button52);
        button53=(Button)findViewById(R.id.button53);
        button54=(Button)findViewById(R.id.button54);
        button55=(Button)findViewById(R.id.button55);
        button56=(Button)findViewById(R.id.button56);
        button57=(Button)findViewById(R.id.button57);
        button58=(Button)findViewById(R.id.button58);
        button59=(Button)findViewById(R.id.button59);
        button60=(Button)findViewById(R.id.button60);
        button61=(Button)findViewById(R.id.button61);
        button62=(Button)findViewById(R.id.button62);
        button63=(Button)findViewById(R.id.button63);

    }

    public void clickSeat(View view){
        //为每一个按钮绑定点击事件
        btnid=btnid2id(view);
        getUserInfo();
        getSeatInfo(id2seatid(btnid));
    }

    /**
     * 将按钮id转换为id
     * @param view
     * @return
     */
    private int btnid2id(View view){
        switch(view.getId()){
            case R.id.button1:
                return 1;
            case R.id.button2:
                return 2;
            case R.id.button3:
                return 3;
            case R.id.button4:
                return 4;
            case R.id.button5:
                return 5;
            case R.id.button6:
                return 6;
            case R.id.button7:
                return 7;
            case R.id.button8:
                return 8;
            case R.id.button9:
                return 9;
            case R.id.button10:
                return 10;
            case R.id.button11:
                return 11;
            case R.id.button12:
                return 12;
            case R.id.button13:
                return 13;
            case R.id.button14:
                return 14;
            case R.id.button15:
                return 15;
            case R.id.button16:
                return 16;
            case R.id.button17:
                return 17;
            case R.id.button18:
                return 18;
            case R.id.button19:
                return 19;
            case R.id.button20:
                return 20;
            case R.id.button21:
                return 21;
            case R.id.button22:
                return 22;
            case R.id.button23:
                return 23;
            case R.id.button24:
                return 24;
            case R.id.button25:
                return 25;
            case R.id.button26:
                return 26;
            case R.id.button27:
                return 27;
            case R.id.button28:
                return 28;
            case R.id.button29:
                return 29;
            case R.id.button30:
                return 30;
            case R.id.button31:
                return 31;
            case R.id.button32:
                return 32;
            case R.id.button33:
                return 33;
            case R.id.button34:
                return 34;
            case R.id.button35:
                return 35;
            case R.id.button36:
                return 36;
            case R.id.button37:
                return 37;
            case R.id.button38:
                return 38;
            case R.id.button39:
                return 39;
            case R.id.button40:
                return 40;
            case R.id.button41:
                return 41;
            case R.id.button42:
                return 42;
            case R.id.button43:
                return 43;
            case R.id.button44:
                return 44;
            case R.id.button45:
                return 45;
            case R.id.button46:
                return 46;
            case R.id.button47:
                return 47;
            case R.id.button48:
                return 48;
            case R.id.button49:
                return 49;
            case R.id.button50:
                return 50;
            case R.id.button51:
                return 51;
            case R.id.button52:
                return 52;
            case R.id.button53:
                return 53;
            case R.id.button54:
                return 54;
            case R.id.button55:
                return 55;
            case R.id.button56:
                return 56;
            case R.id.button57:
                return 57;
            case R.id.button58:
                return 58;
            case R.id.button59:
                return 59;
            case R.id.button60:
                return 60;
            case R.id.button61:
                return 61;
            case R.id.button62:
                return 62;
            case R.id.button63:
                return 63;
        }
        return 0;
    }

    /**
     * 将id转换为seatid
     * @param btnid
     * @return
     */
    private String id2seatid(int btnid){
        int myroomid=0;
        if(roomid.equals("00000001"))myroomid=0;
        else if(roomid.equals("00000002"))myroomid=1;
        else if(roomid.equals("00000003"))myroomid=2;
        else if(roomid.equals("00000004"))myroomid=3;
        else if(roomid.equals("00000005"))myroomid=4;
        else if(roomid.equals("00000006"))myroomid=5;
        if(btnid<10){
            seatId="00000"+myroomid+"0"+btnid;
        }else{
            seatId="00000"+myroomid+btnid;
        }
        return seatId;
    }

    /**
     * 选择座位前的校验
     * @return 返回是否具有选座资格
     */
    private void getUserInfo(){
        BmobQuery<T_relation> relationQuery = new BmobQuery<T_relation>();
        relationQuery.addWhereEqualTo("userid",id);
        relationQuery.findObjects(new FindListener<T_relation>(){
            @Override
            public void done(List<T_relation> relation, BmobException e) {
                if(e==null){
                    if(!relation.isEmpty()){
                        final T_relation relation0=relation.get(0);
                        new Thread(){
                            public void run(){
                                Message message=handler.obtainMessage();
                                message.obj=relation0;
                                handler.sendMessage(message);
                            }
                        }.start();
                    }
                }else{
                    myUtil.showToast("查询关系信息错误"+ e.getMessage());
                }
            }
        });
    }

    private void checkUser(String myseatid){
        if(!myseatid.equals(""))
        {
            myUtil.showToast("您已选择座位，请取消座位选择后再进行选择~");
            flag.setLength(0);
            return;
        }
        //校验位置是否在图书馆
        if(!checkLocation()){
            myUtil.showToast("请到达图书馆后再选择座位哦~");
            return;
        }
        flag.append("a");
        handler3.sendEmptyMessage(1);
    }

    /**
     * 校验用户位置是否在合法范围内
     * @return
     */
    private Boolean checkLocation(){
        String locate=myUtil.getLocation(seatActivity.this);
        Boolean result=false;
        if(locate!=null&&!locate.equals("")){
            String[] location=locate.split(",");
            double lng=Double.valueOf(location[0]);
            double lat=Double.valueOf(location[1]);
            result=myUtil.getDistance(lat,lng);
            return result;
        }
        return false;
    }

    /**
     * 校验该座位是否被占用
     * @param myseatid 座位id
     */
    private void getSeatInfo(String myseatid){
        BmobQuery<T_relation> relationQuery = new BmobQuery<T_relation>();
        relationQuery.addWhereEqualTo("seatid",myseatid);
        relationQuery.findObjects(new FindListener<T_relation>(){
            @Override
            public void done(List<T_relation> relation, BmobException e) {
                if(e==null){
                    if(relation.isEmpty()){
//                        final T_relation relation0=relation.get(0);
//                        Message message=handler2.obtainMessage();
//                        message.obj=relation0;
//                        handler2.sendMessage(message);
                        flag.append("b");
                        handler3.sendEmptyMessage(1);
                    }else{
                        myUtil.showToast("该座位已被占用，请选择其他座位~");
                        flag.setLength(0);
                    }
                }else{
                    myUtil.showToast("查询关系信息错误"+ e.getMessage());
                }
            }
        });
    }

//    private void checkSeat(String myseatid){
//        if(!myseatid.equals("")){
//            myUtil.showToast("该座位已被占用，请选择其他座位");
//            flag.setLength(0);
//            return;
//        }
//        flag.append("b");
//        handler3.sendEmptyMessage(1);
//    }

    /**
     * 选择座位
     */
    private void seatSelect(String myseatid,String myroomid,String myrelationobjectid){
        //首先更改数据库的信息
        //更改关系表信息
        T_relation myrelation = new T_relation();
        myrelation.setSeatid(myseatid);
        myrelation.setRoomid(myroomid);
        myrelation.update(myrelationobjectid, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    handler4.sendEmptyMessage(1);
                }else{
                    myUtil.showToast("更新失败：" + e.getMessage());
                }
            }

        });
    }

    /**
     * 更新数据库成功后更新UI
     * @param myseatid
     */
    private void afterUpdate(int myseatid){
        changeBtn(myseatid);
        myUtil.showToast("选座成功~");

        Intent intent = new Intent(this,cancelActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("roomid",roomid);
        intent.putExtra("relationObjectId",relation.getObjectId().toString());
        intent.putExtra("seatid",seatId);
        startActivityForResult(intent, 2001);
        finish();
    }

    /**
     * 所有的Activity对象的返回值都是由这个方法来接收
     * requestCode:    表示的是启动一个Activity时传过去的requestCode值
     * resultCode：表示的是启动后的Activity回传值时的resultCode值
     * data：表示的是启动后的Activity回传过来的Intent对象
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2001 && resultCode == 1001)
        {
            initSeat();
        }
    }

//    public void clickExit(View view){
//        myUtil.exit(relationObjectId);
//        startActivity(new Intent(seatActivity.this, login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//        finish();
//    }

    public void locate(View view){
        String location=myUtil.getLocation(seatActivity.this);
        if(!location.equals("")){
            myUtil.showMap(seatActivity.this,location);
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
        startActivity(new Intent(seatActivity.this, login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
                        handler5.sendMessage(message2);
                    }else if(mytype==2){
                        //点击注销按钮时
                        handler6.sendMessage(message2);
                    }

                } else {
                    myUtil.showToast("获取关系表信息失败~"+e.getLocalizedMessage());
                }
            }
        });
    }

}
