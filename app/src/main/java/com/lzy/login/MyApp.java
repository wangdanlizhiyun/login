package com.lzy.login;

import android.app.Application;

import com.lzy.login_library.HookAmsUtil;
import com.lzy.login_library.LoginUtil;

/**
 * Created by lizhiyun on 2018/4/5.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LoginUtil.init(this,LoginActivity.class);
    }
}
