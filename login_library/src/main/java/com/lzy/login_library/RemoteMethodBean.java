package com.lzy.login_library;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by lizhiyun on 2017/4/15.
 */

public class RemoteMethodBean {
    private Object target;
    private Method method;
    private Object[] objects;
    private int currentThreadCode;

    public RemoteMethodBean(Object target, Method method, Object[] objects) {
        this.target = target;
        this.method = method;
        this.objects = objects;
        this.currentThreadCode = Thread.currentThread().hashCode();
    }
    public void doMethod(){
        if (this.currentThreadCode == Thread.currentThread().hashCode()){
            method.setAccessible(true);
            try {
                method.invoke(target,objects);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }else {
            synchronized (LoginUtil.getObjectForThread(currentThreadCode)){
                LoginUtil.getObjectForThread(currentThreadCode).notify();
            }
        }

    }
}
