package com.lzy.login_library;

import android.support.annotation.MainThread;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by lizhiyun on 2017/4/15.
 *用于记录拦截的startactivity的方法，等登录后自动触发原操作
 */

public class StartActivityRemoteMethodBean {
    private Object target;
    private Method method;
    private Object[] objects;
    private static volatile StartActivityRemoteMethodBean instance;

    private StartActivityRemoteMethodBean() {

    }

    public static StartActivityRemoteMethodBean getInstance() {
        if (instance == null) {
            synchronized (StartActivityRemoteMethodBean.class) {
                if (instance == null) {
                    instance = new StartActivityRemoteMethodBean();
                }
            }
        }
        return instance;
    }


    @MainThread
    public void setMessage(Object target, Method method, Object[] objects) {
        this.target = target;
        this.method = method;
        this.objects = objects;
    }

    @MainThread
    public void setNull() {
        this.target = null;
        this.method = null;
        this.objects = null;
    }

    public void doMethod() {
        if (target != null) {
            try {
                method.setAccessible(true);
                method.invoke(target, objects);
                Log.e("test","StartActivityRemoteMethodBean invoke");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
