package com.lzy.login;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.login_library.LoginUtil;
import com.lzy.login_library.annotation.CheckIfLoginAndLoginAndBackToContinue;

public class MainActivity extends AppCompatActivity {

    TextView message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("test","onCreate");
        setContentView(R.layout.activity_main);
        message = findViewById(R.id.message);
    }


    public void logout(View view) {
        LoginUtil.login(false);
        toast("退出登录");

    }
    public void ui线程拦截点赞(View view) {
        //模拟主线程的拦截
        doZan();
    }

    public void 多子线程拦截点赞(View view) {
        //模拟子线程的拦截
        for (int i = 0; i < 2; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    doZan();

                }
            }).start();
        }
    }
    public void ui线程和多子线程混合拦截点赞(View view) {
        //模拟主线程的拦截
        doZan();
        //模拟子线程的拦截
        for (int i = 0; i < 2; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    doZan();

                }
            }).start();
        }
    }
    //执行点赞的具体代码
    @CheckIfLoginAndLoginAndBackToContinue
    private void doZan() {
        Log.e("test","点赞 in "+Thread.currentThread().getName());
        toast("点赞 in "+Thread.currentThread().getName());
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

    public final Handler sHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            message.append("\n"+(String) msg.obj);
        }
    };
    public void toast(String content){
            Message msg = sHandler.obtainMessage();
            msg.obj = content;
            sHandler.sendMessage(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("test","onDestroy");
        sHandler.removeCallbacksAndMessages(null);
    }
}
