package com.demo.spring;

import com.demo.spring.annotation.MyAutowired;
import com.demo.spring.annotation.MyService;
import com.demo.spring.utils.ClassUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ProjectName SpringIOC
 * @Author qiaozhonghuai
 * @Date 2019/8/28
 * @Version 1.0
 */
public class MyClassPathXmlApplicationContext {
    //bean容器
    private static ConcurrentHashMap<String,Class<?>> beans = new ConcurrentHashMap<String,Class<?>>();
    
    public MyClassPathXmlApplicationContext(String packageName) throws Exception {
        if (packageName == null) {
            throw new Exception("包名不能为空 ");
        }
        //扫包获取该包下所有类
        List<Class<?>> classesList = ClassUtil.getClasses(packageName);
        //初始化bean容器
        initBeans(classesList);
    }

    /**
     * 通过beanId活动该对象
     * @param beanId
     * @return
     * @throws Exception
     */
    public Object getBean(String beanId) throws Exception {
        if (beanId == null) {
            throw new Exception("beanId不能为空 ");
        }
        Class<?> aClass = beans.get(beanId);
        if (aClass == null) {
            return null;
        }
        //创建beanId的实例对象
        Object o = aClass.newInstance();
        //该对象属性分布
        attrDistribution(o);
        return o;
    }

    //首字母转小写
    public  String toLowerCaseFirstOne(String s){
        if(Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    private void attrDistribution(Object o) throws IllegalAccessException, InstantiationException {
        //获取该对象中所有的属性
        Field[] fields = o.getClass().getDeclaredFields();
        //遍历这些属性
        for (Field field : fields) {
            //获取对象属性上的MyAutowired注解
            MyAutowired myAutowired = field.getAnnotation(MyAutowired.class);
            //判断是否有注解，有就进行实例化该属性
            if (myAutowired != null) {
                //获得该属性的变量名
                String name = field.getName();
                //根据该变量名获得bean容器的beanId
                Class<?> aClass = beans.get(name);
                //实例化该bean的属性对象
                Object o1 = aClass.newInstance();
                //对象不为空说明beans里面有该beanI
                if (aClass != null) {
                    //bean对象属性private修饰的，当你用反射去访问的时候需要setAccessible(true);
                    field.setAccessible(true);
                    //将属性和实例化的对象替换到该属性中
                    field.set(o,o1);
                }
            }
        }
    }

    /**
     * 初始化bean容器
     * @param classesList
     */
    private void initBeans(List<Class<?>> classesList){
        //遍历扫包的类
        for (Class<?> classes: classesList ) {
            //获取该类上的MyService注解
            MyService myService = classes.getAnnotation(MyService.class);
            //判断是否包含注解MyService，如果包含注解将该类放入bean容器里
            if (myService != null) {
                //get类名
                String name = classes.getSimpleName();
                //类名小写做为beanID
                String beanID = toLowerCaseFirstOne(name);
                //key类名小写做为beanID ，value是该类
                beans.put(beanID,classes);
            }
        }
    }
}
