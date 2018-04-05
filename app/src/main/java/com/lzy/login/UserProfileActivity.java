package com.lzy.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.lzy.login_library.annotation.CheckIfLoginAndLoginAndBackToContinue;

/**
 * Created by lizhiyun on 2018/4/5.
 */

@CheckIfLoginAndLoginAndBackToContinue
public class UserProfileActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("这里是个人中心");
        setContentView(tv);
    }
}
