package com.lzy.login_library;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.MainThread;
import android.support.v4.util.ArrayMap;
import android.util.LruCache;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by lizhiyun on 2018/4/5.
 */

public class LoginUtil {

    public static Application sApplication;
    static Class sLoginActivityClass;
    static SharedPreferences sSharedPreferences = null;
    private static final Object sObjectSp = new Object();
    public static Object sObjectLogin = RemoteMethodBean.class;


    private static InternalHandler sHandler;

    public static void login(Boolean login) {

        synchronized (sObjectSp) {
            if (login) {
                sSharedPreferences.edit().putBoolean("login", true).commit();
                Message msg = getMainHandler().obtainMessage();
                msg.what = What_DoRemoteMethod;
                getMainHandler().removeMessages(What_DoRemoteMethod);
                getMainHandler().sendMessageDelayed(msg, 350);
            } else {
                sSharedPreferences.edit().putBoolean("login", false).commit();
            }
        }
    }

    private static Handler getMainHandler() {
        synchronized (AsyncTask.class) {
            if (sHandler == null) {
                sHandler = new InternalHandler(Looper.getMainLooper());
            }
            return sHandler;
        }
    }

    private static class InternalHandler extends Handler {
        public InternalHandler(Looper looper) {
            super(looper);
        }

        @SuppressWarnings({"unchecked", "RawUseOfParameterizedType"})
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case What_DoRemoteMethod:
                    synchronized (sObjectLogin) {
                        RemoteMethodBean.getInstance().doMethod();
                        StartActivityRemoteMethodBean.getInstance().doMethod();
                    }
                    break;
                case What_Login:

                    Activity activity = getActivity();
                    if (activity != null) {
                        activity.startActivity(new Intent(activity, sLoginActivityClass));
                    }
                    break;
            }
        }
    }

    public static synchronized Boolean isLogined() {
        synchronized (sObjectSp) {
            return sSharedPreferences.getBoolean("login", false);
        }
    }

    private static WeakReference<Activity> mWeakReferenceActivity;

    public static Activity getActivity() {
        if (mWeakReferenceActivity != null) {
            return mWeakReferenceActivity.get();
        }
        return null;
    }

    @MainThread
    public static void init(Application application, Class loginActivityClass) {
        sApplication = application;
        sLoginActivityClass = loginActivityClass;
        sSharedPreferences = application.getSharedPreferences("login",
                Context.MODE_PRIVATE);
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

                mWeakReferenceActivity = new WeakReference<Activity>(activity);
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (mActivityOnDestroyListenerArrayMap.get(activity) != null) {
                    mActivityOnDestroyListenerArrayMap.get(activity).onDestroy();
                }
            }
        });
        HookAmsUtil.hookStartActivity();
        HookAmsUtil.hookSystemHandler();
    }

    static ArrayMap<Activity, OnDestroyListener> mActivityOnDestroyListenerArrayMap = new ArrayMap<>();

    public static void putDestoryListener(Activity activity, OnDestroyListener onDestroyListener) {
        if (activity != null && onDestroyListener != null) {
            mActivityOnDestroyListenerArrayMap.put(activity, onDestroyListener);
        }
    }

    private static final int What_Login = 0;
    private static final int What_DoRemoteMethod = 1;

    public static void gotoLogin() {
        Message msg = getMainHandler().obtainMessage();
        msg.what = What_Login;
        getMainHandler().removeMessages(What_Login);
        getMainHandler().sendMessageDelayed(msg, 350);
    }

    public static Class getProxyActivityClass() {
        try {
            ActivityInfo[] activities = LoginUtil.sApplication.getPackageManager().getPackageInfo(LoginUtil.sApplication.getPackageName(), PackageManager.GET_ACTIVITIES).activities;
            if (activities != null && activities.length > 0) {
                return Thread.currentThread().getContextClassLoader().loadClass(activities[0].name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
