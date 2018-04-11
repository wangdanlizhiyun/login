
package com.lzy.login_library.aspect;

import android.os.Looper;
import android.util.Log;
import android.util.LruCache;

import com.lzy.login_library.LoginUtil;
import com.lzy.login_library.OnDestroyListener;
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
    public void executeDoCheckIfLoginBeforeAspect() {
    }

    @Around("executeDoCheckIfLoginBeforeAspect()")
    public Object beforeJoinPoint(final ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        Object target = joinPoint.getTarget();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final Method currentMethod = methodSignature.getMethod();

        if (Looper.myLooper() != Looper.getMainLooper()) {
            if (LoginUtil.isLogined()) {
                result = joinPoint.proceed();
            } else {
                synchronized (RemoteMethodBean.class) {
                    LoginUtil.gotoLogin();
                    LoginUtil.sObjectLogin.wait();
                    joinPoint.proceed();

                }
            }
        } else {
            //ui
            if (LoginUtil.isLogined()) {
                result = joinPoint.proceed();
            } else {
                RemoteMethodBean.getInstance().setMessage(target, currentMethod, joinPoint.getArgs());
                LoginUtil.putDestoryListener(LoginUtil.getActivity(),RemoteMethodBean.getInstance().getOnDestroyListener());
                LoginUtil.gotoLogin();
            }
        }

        return result;
    }


}
