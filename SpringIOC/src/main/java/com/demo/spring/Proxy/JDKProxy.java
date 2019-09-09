package com.demo.spring.Proxy;

import com.demo.spring.service.OrderService;
import com.demo.spring.service.impl.OrderServiceImpl;

import java.lang.reflect.Proxy;

/**
 * @ClassName JDKProxy
 * @Description TODO
 * @Author qiaozhonghuai
 * @Date 2019/9/2 0002 下午 17:55
 * @Version 1.0
 **/
public class JDKProxy {



    public static Object newProxy(Object targetObject) {
        //将目标对象传入进行代理
        if (targetObject!=null) {
            return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(),
                    targetObject.getClass().getInterfaces(), new ProxyInvocationHandler(targetObject));//返回代理对象
        }
        return null;
    }


    public static void main(String[] args) {
        OrderService o = (OrderService)newProxy(new OrderServiceImpl());
        o.addOrder();
    }


}