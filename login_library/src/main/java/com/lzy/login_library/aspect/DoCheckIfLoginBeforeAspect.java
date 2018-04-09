
package com.lzy.login_library.aspect;

import android.os.Looper;
import android.util.Log;
import android.util.LruCache;

import com.lzy.login_library.LoginUtil;
import com.lzy.login_library.RemoteMethodBean;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;


@Aspect
public class DoCheckIfLoginBeforeAspect {

    @Pointcut("execution(@com.lzy.login_library.annotation.CheckIfLoginAndLoginAndBackToContinue * *(..))")
    public void executeDoCheckIfLoginBeforeAspect() {}

    @Around("executeDoCheckIfLoginBeforeAspect()")
    public Object beforeJoinPoint(final ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        LoginUtil.setmRemoteMethodBean(null);
        Object target = joinPoint.getTarget();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final Method currentMethod = methodSignature.getMethod();
        if (LoginUtil.isLogined()) {
            result = joinPoint.proceed();
        } else {
            LoginUtil.gotoLogin();
            LoginUtil.setmRemoteMethodBean(new RemoteMethodBean(target,currentMethod,joinPoint.getArgs()));
            if (Looper.myLooper() != Looper.getMainLooper()) {
                try{
                    synchronized (LoginUtil.getObjectForThread(Thread.currentThread().hashCode())){
                        LoginUtil.getObjectForThread(Thread.currentThread().hashCode()).wait();
                        joinPoint.proceed();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return result;
    }



}
