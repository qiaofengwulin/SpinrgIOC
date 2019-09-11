package com.demo.spring.aop;

import com.demo.spring.annotation.MyAfter;
import com.demo.spring.annotation.MyAspect;
import com.demo.spring.annotation.MyBefore;
import com.demo.spring.annotation.MyService;

/**
 * @ClassName AopLogExtAspect
 * @Description TODO
 * @Author qiaozhonghuai
 * @Date 2019/9/3 0003 下午 18:19
 * @Version 1.0
 **/
@MyAspect
@MyService
public class AopLogExtAspect {

    // 请求method前打印内容
    @MyBefore(packageName = "com.demo.spring.service.impl")
    public void methodBefore() {
        System.out.println("前置通知");
    }

    @MyAfter(packageName = "com.demo.spring.service.impl")
    public void methodAfterReturing() {
        System.out.println("后置通知");
    }
}
