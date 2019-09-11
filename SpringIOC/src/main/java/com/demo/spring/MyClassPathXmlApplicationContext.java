package com.demo.spring;

import com.demo.spring.Proxy.JDKProxy;
import com.demo.spring.Proxy.ProxyInvocationHandler;
import com.demo.spring.annotation.*;
import com.demo.spring.utils.ClassUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ProjectName SpringIOC
 * @Author qiaozhonghuai
 * @Date 2019/8/28
 * @Version 1.0
 */
public class MyClassPathXmlApplicationContext {
    //初始bean容器
    private static ConcurrentHashMap<String, Object> beans = new ConcurrentHashMap<String, Object>();
    //切面类容器
    private static ConcurrentHashMap<String, Object> aspectBeans = new ConcurrentHashMap<String, Object>();
    //代理容器
    private static ConcurrentHashMap<String, Object> proxyBeans = new ConcurrentHashMap<String, Object>();

    public MyClassPathXmlApplicationContext(String packageName) throws Exception {
        if (packageName == null) {
            throw new Exception("包名不能为空 ");
        }
        //扫包获取该包下所有类
        List<Class<?>> classesList = ClassUtil.getClasses(packageName);
        //初始化bean容器
        initBeans(classesList);
        //初始化initProxyBeans容器
        initProxyBeans();
        //替换掉beans里中需要代理的实例
        replaceProxyBean();

    }

    /**
     * 初始化bean容器
     *
     * @param classesList
     */
    private void initBeans(List<Class<?>> classesList) throws Exception {
        //遍历扫包的类
        for (Class<?> classes : classesList) {
            //获取该类上的MyService注解
            MyService myService = classes.getAnnotation(MyService.class);
            //判断是否包含注解MyService，如果包含注解将该类放入bean容器里
            if (myService != null) {
                //get类名
                String name = classes.getSimpleName();
                //类名小写做为beanID
                String beanID = toLowerCaseFirstOne(name);
                Object o = classes.newInstance();
                //key类名小写做为beanID ，value是该类对象
                beans.put(beanID, o);
                //判断是否为切面，如果是切面就注入到切面容器中
                MyAspect extAspect = classes.getAnnotation(MyAspect.class);
                if (extAspect != null) {
                    //初始化切面容器
                    aspectBeans.put(beanID, o);
                }
            }
        }
    }

    /**
     * 初始化代理容器
     * @throws Exception
     */
    private void initProxyBeans() throws Exception {
        if (aspectBeans == null) {
            return;
        }
        //遍历出所有的切面
        for (Map.Entry<String, Object> stringObjectEntry : aspectBeans.entrySet()) {
            Object value = stringObjectEntry.getValue();
            //获取切面上的所有方法
            Method[] methods = value.getClass().getMethods();
            for (Method method : methods) {
                MyBefore extBefore = method.getAnnotation(MyBefore.class);
                //判断切面类方法是否存在MyBefore
                if (extBefore != null) {
                    //初始化代理对象
                    ProxyInvocationHandler.before(method, value);
                    //获取MyBefore的packageName
                    String packageName = extBefore.packageName();
                    //扫包
                    List<Class<?>> classesList = ClassUtil.getClasses(packageName);
                    for (Class<?> aClass : classesList) {
                        String name = aClass.getSimpleName();
                        String s = toLowerCaseFirstOne(name);
                        //实例化代理目标对象
                        Object o = aClass.newInstance();
                        //初始化代理容器
                        proxyBeans.put(s, JDKProxy.newProxy(o));
                    }
                }

                MyAfter extAfter = method.getAnnotation(MyAfter.class);
                if (extAfter != null) {
                    ProxyInvocationHandler.after(method, value);
                    String packageName = extAfter.packageName();
                    //扫包
                    List<Class<?>> classesList = ClassUtil.getClasses(packageName);
                    for (Class<?> aClass : classesList) {
                        String name = aClass.getSimpleName();
                        String s = toLowerCaseFirstOne(name);
                        //o为目标对象
                        Object o = aClass.newInstance();
                        //创建代理对象
                        proxyBeans.put(s, JDKProxy.newProxy(o));
                    }
                }
            }
        }
    }
    /**
     * 初始化替换bean容器
     */
    private void replaceProxyBean() {
        for (Map.Entry<String, Object> stringObjectEntry : beans.entrySet()) {
            String key = stringObjectEntry.getKey();
            Object o = proxyBeans.get(key);
            if (o != null) {
                beans.put(key,o);
            }
        }
    }

    /**
     * 通过beanId活动该对象
     *
     * @param beanId
     * @return
     * @throws Exception
     */
    public Object getBean(String beanId) throws Exception {
        if (beanId == null) {
            throw new Exception("beanId不能为空 ");
        }
        /**
         * 创建beanId的实例对象
         *
         * 此时创建的对象已经是代理对象了。因为replaceProxyBean()方法已经将原beans里面的对象替换了。
         * 这个beanId目标对象不替换成代理对象。就无法实现aop切面。
         * 替换成代理对象造成无法依赖注入的bug待解决
         *
         */
        Object o = beans.get(beanId);
        //该对象的属性分布
        attrDistribution(o);
        return o;
    }

    /**
     * //对象的属性分布
     * @param o
     */
    private void attrDistribution(Object o) throws Exception{
        //获取该对象中所有的属性
        Field[] fields = o.getClass().getDeclaredFields();
        //遍历这些属性
        for (Field field : fields) {
            //获取对象属性上的MyAutowired注解
            MyAutowired myAutowired = field.getAnnotation(MyAutowired.class);
            //判断是否有MyAutowired注解，有就进行实例化该属性
            if (myAutowired != null) {
                //获得该属性的变量名
                String name = field.getName();
                //根据该变量名获得bean容器的beanId实例化对象
                Object o1 = beans.get(name);
                o1.toString();
//                field = o1.getClass().getDeclaredField(name);
                //对象不为空说明beans里面有该beanI
                if (o1 != null) {
                    //bean对象属性private修饰的，当你用反射去访问的时候需要setAccessible(true);
                    field.setAccessible(true);
                    //将属性和实例化的对象替换到该属性中
                    field.set(o, o1);
                }
            }
        }
    }

    //首字母转小写
    public String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }
}
