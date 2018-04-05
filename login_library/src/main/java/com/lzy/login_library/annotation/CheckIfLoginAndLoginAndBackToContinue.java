
package com.lzy.login_library.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 添加一个检测是否登陆的注解，如果没登陆就跳转到登陆界面登陆成功返回后自动执行后续代码（跳转，评论等）
 * ,无需在onActivityResult等地方添加额外代码，维护了顺序的代码流，增强可读性
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD , ElementType.TYPE })
public @interface CheckIfLoginAndLoginAndBackToContinue {}
