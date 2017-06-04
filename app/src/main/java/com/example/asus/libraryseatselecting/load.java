package com.example.asus.libraryseatselecting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import cn.bmob.v3.Bmob;

public class load extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        //初始化bmob
        Bmob.initialize(this, "a3a3558a0c735348baa11aed216519f5");

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent=new Intent(load.this,login.class);
                startActivity(intent);
                finish();
            }
        }, 2000); //延迟2秒跳转
    }
}
