package com.lzy.login_library;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lzy.login_library.annotation.CheckIfLoginAndLoginAndBackToContinue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by lizhiyun on 2018/4/5.
 */

public class HookAmsUtil {

    public static void hookStartActivity() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){//8.0及以上
                Class<?> activityManagerClass = Class.forName("android.app.ActivityManager");
                Class<?> singletonClass = Class.forName("android.util.Singleton");
                Class<?> IActivityManagerClass = Class.forName("android.app.IActivityManager");
                Object IActivityManagerSingleton = FieldUtil.getDeclaredFieldObject(activityManagerClass, "IActivityManagerSingleton", null);

                Field mInstanceField = singletonClass.getDeclaredField("mInstance");
                mInstanceField.setAccessible(true);
                Object mInstance = mInstanceField.get(IActivityManagerSingleton);
                Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class[]{IActivityManagerClass}, new ActivityManagerInvocationHandler(mInstance));
                mInstanceField.set(IActivityManagerSingleton, proxy);
            }else {//5 6 7
                Class<?> activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
                Class<?> singletonClass = Class.forName("android.util.Singleton");
                Class<?> IActivityManagerClass = Class.forName("android.app.IActivityManager");
                Object gDefault = FieldUtil.getDeclaredFieldObject(activityManagerNativeClass, "gDefault", null);

                Field mInstanceField = singletonClass.getDeclaredField("mInstance");
                mInstanceField.setAccessible(true);
                Object mInstance = mInstanceField.get(gDefault);
                Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class[]{IActivityManagerClass}, new ActivityManagerInvocationHandler(mInstance));
                mInstanceField.set(gDefault, proxy);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static class ActivityManagerInvocationHandler implements InvocationHandler {
        Object iActivityManagerObj;

        public ActivityManagerInvocationHandler(Object iActivityManagerObj) {
            this.iActivityManagerObj = iActivityManagerObj;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            Log.w("test","invoke "+method.getName());
            if ("startActivity".equals(method.getName())) {
                Intent intent = null;
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    if (arg instanceof Intent) {
                        intent = (Intent) arg;
                    }
                }
                if (intent != null) {
                    if (hasLoginAnnotation(intent.getComponent().getClassName())){
                        LoginUtil.mRemoteMethodBean = new RemoteMethodBean(iActivityManagerObj,method,args);
                    }else {
                        LoginUtil.mRemoteMethodBean = null;
                    }
                    intent.putExtra("component", intent.getComponent());
                    intent.setComponent(new ComponentName(LoginUtil.mApplication, ProxyActivity.class));
                }
            }
            return method.invoke(iActivityManagerObj, args);
        }
    }

    public static void hookSystemHandler() {
        try {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThreadValue = FieldUtil.getDeclaredFieldObject(activityThreadClass, "sCurrentActivityThread", null);
            Handler handler = (Handler) FieldUtil.getDeclaredFieldObject(activityThreadClass, "mH", activityThreadValue);
            FieldUtil.setDeclaredFieldObject(Handler.class,"mCallback",handler,new ProxyCallback(handler));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ProxyCallback implements Handler.Callback {
        private Handler mHandler;

        public ProxyCallback(Handler handler) {
            this.mHandler = handler;
        }

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 100) {
                handlerLaunchActivity(msg);
            }
            if (mHandler != null) {
                mHandler.handleMessage(msg);
            }
            return true;
        }

        private void handlerLaunchActivity(Message msg) {
            Object object = msg.obj;
            try {
                Field field = object.getClass().getDeclaredField("intent");

                field.setAccessible(true);
                Intent intent = (Intent) field.get(object);
                if (intent != null) {
                    ComponentName componentName = intent.getParcelableExtra("component");
                    if (componentName == null) {
                        return;
                    }
                    Log.w("test","handlerLaunchActivity "+componentName.getClassName());
                    Boolean isNeedGotoLogin = false;

                    isNeedGotoLogin = !LoginUtil.isLogined() && hasLoginAnnotation(componentName.getClassName());
                    if (isNeedGotoLogin) {
                        intent.setComponent(new ComponentName(LoginUtil.mApplication, LoginUtil.mLoginActivityClass));
                    } else {
                        intent.setComponent(componentName);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private static Boolean hasLoginAnnotation(String className){
        Class<?> clazz = null;
        try {
            clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
            CheckIfLoginAndLoginAndBackToContinue checkIfLoginAndLoginAndBackToContinue = clazz.getAnnotation(CheckIfLoginAndLoginAndBackToContinue.class);
            return checkIfLoginAndLoginAndBackToContinue != null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
