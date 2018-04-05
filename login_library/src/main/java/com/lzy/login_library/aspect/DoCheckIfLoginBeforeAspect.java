
package com.lzy.login_library.aspect;

import com.lzy.login_library.LoginUtil;
import com.lzy.login_library.RemoteMethodBean;
import com.lzy.login_library.annotation.CheckIfLoginAndLoginAndBackToContinue;

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
        CheckIfLoginAndLoginAndBackToContinue checkIfLogin = currentMethod.getAnnotation(CheckIfLoginAndLoginAndBackToContinue.class);
        LoginUtil.mRemoteMethodBean = null;
        if (LoginUtil.isLogined()) {
            result = joinPoint.proceed();
        } else {
            LoginUtil.gotoLogin();
            LoginUtil.mRemoteMethodBean = new RemoteMethodBean(target,currentMethod,joinPoint.getArgs());
        }
        return result;
    }

}
