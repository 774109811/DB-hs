package com.db.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequiresCacheKey {//这个注解自己定义的 有个这个注解点击用户第一次在数据库取,第二次就在缓存中取

}
