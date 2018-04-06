package com.lzy.login_library;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by lizhiyun on 2018/4/5.
 */

public class LoginUtil {

    public static Application mApplication;
    static Class mLoginActivityClass;
    static SharedPreferences mSharedPreferences = null;
    public static RemoteMethodBean mRemoteMethodBean;

    public static void login(Boolean login) {
        if (login) {
            mSharedPreferences.edit().putBoolean("login",true).commit();
            if (mRemoteMethodBean != null){
                mRemoteMethodBean.doMethod();
            }
        } else {
            mSharedPreferences.edit().putBoolean("login",false).commit();
        }
    }
    public static Boolean isLogined(){
        return mSharedPreferences.getBoolean("login",false);
    }
    private static WeakReference<Activity> mWeakReferenceActivity;
    public static Activity getActivity() {
        if (mWeakReferenceActivity != null){
            return mWeakReferenceActivity.get();
        }
        return null;
    }
    public static void init(Application application, Class loginActivityClass){
        mApplication = application;
        mLoginActivityClass = loginActivityClass;
        mSharedPreferences = application.getSharedPreferences("login",
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

            }
        });
        HookAmsUtil.hookStartActivity();
        HookAmsUtil.hookSystemHandler();
    }
    public static void gotoLogin(){
        Activity activity = getActivity();
        if (activity != null){
            activity.startActivity(new Intent(activity,mLoginActivityClass));
        }
    }

    public static Class getProxyActivityClass(){
        try {
            ActivityInfo[] activities = LoginUtil.mApplication.getPackageManager().getPackageInfo(LoginUtil.mApplication.getPackageName(), PackageManager.GET_ACTIVITIES).activities;
            if (activities != null && activities.length > 0){
                return Thread.currentThread().getContextClassLoader().loadClass(activities[0].name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
