package com.demo.spring.service.impl;

import com.demo.spring.annotation.MyAutowired;
import com.demo.spring.annotation.MyService;
import com.demo.spring.service.OrderService;
import com.demo.spring.service.UserService;

/**
 * @ProjectName Spring
 * @Author qiaozhonghuai
 * @Date 2019/8/28
 * @Version 1.0
 */
@MyService
public class UserServiceImpl implements UserService {
    @MyAutowired
    private OrderService orderServiceImpl = new OrderServiceImpl() ;

    public void add() {
        orderServiceImpl.addOrder();
        System.out.println("UserService执行add方法...........");
    }


}
