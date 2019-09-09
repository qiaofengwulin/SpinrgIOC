package com.demo.spring.service.impl;

import com.demo.spring.annotation.MyService;
import com.demo.spring.service.OrderService;

/**
 * @ProjectName SpringIOC
 * @Author qiaozhonghuai
 * @Date 2019/8/28
 * @Version 1.0
 */
@MyService
public class OrderServiceImpl implements OrderService {

    public void addOrder() {
        System.out.println("OrderService执行addOrder方法");
    }
    @Override
    public String toString() {
        return "UserServiceImpl{" +
                "orderServiceImpl=" + 1 +
                '}';
    }
}
