package com.lzy.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lzy.login_library.LoginUtil;
import com.lzy.login_library.annotation.CheckIfLoginAndLoginAndBackToContinue;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void logout(View view) {
        LoginUtil.login(false);

    }

    public void zan(View view) {
        //模拟主线程的拦截
//        doZan();
        //模拟子线程的拦截
        new Thread(new Runnable() {
            @Override
            public void run() {
                doZan();

            }
        }).start();

    }
    @CheckIfLoginAndLoginAndBackToContinue
    private void doZan() {
        Log.e("test","点赞 in "+Thread.currentThread().getName());
        ToastUtil.toast(MyApp.myApp,"点赞 in "+Thread.currentThread().getName());
    }

    //点击默认头像前往个人中心（自动拦截登录）
    public void userProfilePhoto(View view) {
        Intent intent = new Intent(this,UserProfileActivity.class);
        intent.putExtra("a","a");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    //点击默认昵称前往个人中心（自动拦截登录）
    public void userProfileName(View view) {
        Intent intent = new Intent(this,UserProfileActivity.class);
        intent.putExtra("b","b");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}
