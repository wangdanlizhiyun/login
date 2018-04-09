package com.lzy.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.lzy.login_library.LoginUtil;

/**
 * Created by lizhiyun on 2018/4/5.
 */

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //模拟主线程触发
//                LoginUtil.login(true);

                //模拟子线程触发
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LoginUtil.login(true);
                    }
                }).start();
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
