package com.lzy.login;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import java.util.logging.LogRecord;

/**
 * Created by lizhiyun on 2018/4/10.
 */

public class ToastUtil {
    static Context sContext;
    public static final Handler sHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Toast.makeText(sContext, (String) msg.obj,Toast.LENGTH_SHORT).show();
        }
    };
    public static void toast(Context context,String content){
        sContext = context.getApplicationContext();
        if (Looper.myLooper() == Looper.getMainLooper()){
            Toast.makeText(sContext,content,Toast.LENGTH_SHORT).show();
        }else {
            Message msg = sHandler.obtainMessage();
            msg.obj = content;
            sHandler.sendMessage(msg);
        }
    }
}
