package com.demo.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解 从Spring容器获取bean
 *
 * @ProjectName SpringIOC
 * @Author qiaozhonghuai
 * @Date 2019/8/28
 * @Version 1.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAutowired {

}
