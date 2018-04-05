package com.lzy.login_library;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by lizhiyun on 2017/4/15.
 */

public class RemoteMethodBean {
    private Object target;
    private Method method;
    private Object[] objects;

    public RemoteMethodBean(Object target, Method method, Object[] objects) {
        this.target = target;
        this.method = method;
        this.objects = objects;
    }
    public void doMethod(){
        method.setAccessible(true);
        try {
            method.invoke(target,objects);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
