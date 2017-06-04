package com.example.asus.libraryseatselecting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class login extends AppCompatActivity {

    private util myUtil=new util();
    private String password;
    private String id="";
    //获取从数据库取到的结果对象
    private T_user user;
    private T_relation relation;

    private StringBuffer flag=new StringBuffer();

    //初始化控件
    private EditText mid,mpassword;

    /**得到用户名密码数据后*/
    private Handler handler=new Handler(){
        public void handleMessage(android.os.Message msg){
            System.out.println(msg.obj);
            user=(T_user) msg.obj;
            if(user==null){
                myUtil.showToast("账号不存在，请确认后再次输入~");
                flag.setLength(0);
            }else{
                flag.append("a");
                handler3.sendEmptyMessage(1);
            }
        }
    };

    /**得到关系表登录数据后*/
    private Handler handler2=new Handler(){
        public void handleMessage(android.os.Message msg){
            relation=(T_relation) msg.obj;
            flag.append("b");
            handler3.sendEmptyMessage(1);
        }
    };


    /** handler与handler2都处理完后*/
    Boolean done=false;
    private Handler handler3=new Handler(){
        public void handleMessage(android.os.Message msg){
            if(flag.indexOf("ab")!=-1||flag.indexOf("ba")!=-1){
                if(!done){
                    done=true;
                    createLoginInfo(id);
                }
            }
        }
    };

    /**handler3处理完后*/
    private Handler handler4=new Handler(){
        public void handleMessage(android.os.Message msg){
            Boolean result=check(password);
            //校验通过则登录成功
            if(result){
                myUtil.showToast("登录成功");
                //跳转到阅览室界面，将用户名传递到第二个页面显示
                Intent intent=new Intent(login.this,ReadingRoomActivity.class);
                intent.putExtra("name",user.getName());
                intent.putExtra("id",user.getID());
//                intent.putExtra("relationObjectId",relation.getObjectId());
//                intent.putExtra("userObjectId",user.getObjectId());
                startActivity(intent);
                finish();
            }
        }

    };


    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mid=(EditText) findViewById(R.id.email);
        mpassword=(EditText) findViewById(R.id.password);

    }

    public void login(View view){
        id=mid.getText().toString();
        password=mpassword.getText().toString();

        //校验学号密码是否为空
        if(id.isEmpty()||password.isEmpty()){
            myUtil.showToast("请输入学号及密码~");
            return;
        }
        getLoginInfo(id);

    }

    /**
     * 在关系表中创建登录信息
     */
    public void createLoginInfo(String id){
        final String mid=id;
        //若无登录记录则插入登录记录，若有登录记录则获取objectId
        if(relation==null){
            //插入该条登录记录
            final T_relation myrelation = new T_relation();
            myrelation.setUserid(mid);
            myrelation.setLogin_flag("1");
            myrelation.save(new SaveListener<String>() {
                @Override
                public void done(String objectId,BmobException e) {
                    if(e==null){
                        myrelation.setObjectId(objectId);
                        relation=myrelation;
                        handler4.sendEmptyMessage(1);
                    } else {
                        myUtil.showToast("创建登录信息失败~"+e.getLocalizedMessage());
                    }
                }
            });
        }else{
            handler4.sendEmptyMessage(1);
        }
    }

    /**
     * 登陆前的校验
     * @param password
     * @return
     */
    public Boolean check(String password){
        if(user==null){
            myUtil.showToast("账号不存在，请确认后再次输入~");
            return false;
        }else if(!user.getPassword().equals(password)){
            myUtil.showToast("密码不正确，请注意大小写~");
            return false;
        }
        return true;
    }

    /**
     * 获取登录的相关信息
     * @param id 用户id
     */
    public void getLoginInfo(String id) {
        final String mid=id;
        //获取id及密码，姓名信息
        final Message message = handler.obtainMessage();
        BmobQuery<T_user> userQuery = new BmobQuery<T_user>();
        userQuery.addWhereEqualTo("ID", mid);
        userQuery.findObjects(new FindListener<T_user>() {
            @Override
            public void done(List<T_user> users, BmobException e) {
                if (e == null) {
                    final T_user user0;
                    if (!users.isEmpty()) {
                        user0 = users.get(0);
                    } else {
                        user0 = null;
                    }
                    message.obj = user0;
                    handler.sendMessage(message);
                } else {
                    myUtil.showToast("获取用户表信息失败~"+e.getLocalizedMessage());
                }
            }
        });

        //获取关系表中信息
        final Message message2 = handler.obtainMessage();
        BmobQuery<T_relation> ralationQuery = new BmobQuery<T_relation>();
        ralationQuery.addWhereEqualTo("userid", mid);
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
                    handler2.sendMessage(message2);
                } else {
                    myUtil.showToast("获取关系表信息失败~"+e.getLocalizedMessage());
                }
            }
        });
    }

}
