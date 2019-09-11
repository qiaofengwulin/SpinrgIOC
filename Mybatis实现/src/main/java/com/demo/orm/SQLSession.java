package com.demo.orm;

import java.lang.reflect.Proxy;

/**
 * @ClassName SQLSession
 * @Description TODO
 * @Author qiaozhonghuai
 * @Date 2019/9/10 0010 下午 18:25
 * @Version 1.0
 **/
public class SQLSession  {
    /**
     * sql代理对象容器
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getMapper(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},new MybatisInvocationHandler(clazz));
    }
}
