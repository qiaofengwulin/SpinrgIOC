package com.demo.spring.Proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @ClassName ProxyInvocationHandler
 * @Description TODO
 * @Author qiaozhonghuai
 * @Date 2019/9/3 0003 上午 9:14
 * @Version 1.0
 **/
public class ProxyInvocationHandler implements InvocationHandler {
    private Object target;

    private static Method m1;
    private static Object o1;
    private static Method m2;
    private static Object o2;

    public ProxyInvocationHandler(Object target) {
        this.target=target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (m1 != null) {
            m1.invoke(o1);
        }

        Object invoke = method.invoke(target, args);
        if (m2 != null) {
            m2.invoke(o2);
        }

        return invoke;
    }

    public static void before(Method method,Object o){
        m1 = method;
        o1 = o;
    }

    public static void after(Method method,Object o){
        m2 = method;
        o2 = o;
    }
}
