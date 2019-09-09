package com.demo;

import com.demo.spring.MyClassPathXmlApplicationContext;
import com.demo.spring.service.UserService;

/**
 * @ProjectName SpringIOC
 * @Author qiaozhonghuai
 * @Date 2019/8/28
 * @Version 1.0
 */
public class TestMain {

    public static void main(String[] args) throws Exception {
        MyClassPathXmlApplicationContext myClassPathXmlApplicationContext =
                new MyClassPathXmlApplicationContext("com.demo.spring");
//        OrderService orderService = (OrderService) myClassPathXmlApplicationContext.getBean("orderServiceImpl");
//        orderService.addOrder();

        UserService userService = (UserService) myClassPathXmlApplicationContext.getBean("userServiceImpl");
        userService.add();
    }

}
