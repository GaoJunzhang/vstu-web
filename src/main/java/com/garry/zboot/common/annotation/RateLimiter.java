package com.garry.zboot.common.annotation;

import java.lang.annotation.*;

/**
* class_name: RateLimiter
* package: com.garry.zboot.common.annotation
* describe: 限流注解
* creat_user: ZhangGaoJun@zhanggj@seeyoo.cn
* creat_date: 2019/7/9
* creat_time: 13:26
**/
@Target(ElementType.METHOD)//作用于方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {

    int limit() default 5;
    int timeout() default 1000;
}
