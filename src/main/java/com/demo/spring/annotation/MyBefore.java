package com.demo.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName MyBefore
 * @Description TODO
 * @Author qiaozhonghuai
 * @Date 2019/9/3 0003 上午 9:42
 * @Version 1.0
 **/
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MyBefore {
        public String packageName();

}
