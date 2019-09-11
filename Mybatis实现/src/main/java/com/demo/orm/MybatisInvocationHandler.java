package com.demo.orm;

import com.demo.orm.annotation.ExtInsert;
import com.demo.orm.annotation.ExtParam;
import com.demo.orm.annotation.ExtSelect;
import com.demo.utils.JDBCUtils;
import com.demo.utils.SQLUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName MybatisInvocationHandler
 * @Description TODO
 * @Author qiaozhonghuai
 * @Date 2019/9/10 0010 下午 18:27
 * @Version 1.0
 **/
public class MybatisInvocationHandler implements InvocationHandler {
    private Object o;
    public MybatisInvocationHandler(Object o) {
        this.o = o;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //判断方法上是否有注解
        ExtInsert extInsert = method.getAnnotation(ExtInsert.class);
        if (extInsert != null) {
            return insert(extInsert,method,args);
        }
        ExtSelect extSelect = method.getAnnotation(ExtSelect.class);
        if (extSelect != null) {
            return select(extSelect,method,args);
        }
        return 1;
    }

    private Object select(ExtSelect extSelect,Method method, Object[] args) throws SQLException, IllegalAccessException, InstantiationException {
        //获取到注解上面的sql
        String sql = extSelect.value();
        //获取参数列表
        ConcurrentHashMap<String, Object> paramMap = getParamMap(method, args);
        //获得sql的参数名
        List<String> strings = SQLUtils.sqlSelectParameter(sql);
        ArrayList<Object> list = new ArrayList<Object>();
        //遍历sql参数数组
        for (String string : strings) {
            //用sql参数名获取参数值
            Object o = paramMap.get(string);
            //将参数存入list
            list.add(o);
        }
        String newSql = SQLUtils.parameQuestion(sql, strings);
        System.out.println(newSql);
        System.out.println(list);
        //执行sql
        ResultSet rs = JDBCUtils.query(newSql, list);
        //获取返回值类型
        Class<?> returnType = method.getReturnType();
        //实例化返回值
        Object o = returnType.newInstance();
        //获得返回值对象的属性
        Field[] declaredFields = returnType.getDeclaredFields();
        //读取sql结果集
        while(rs.next()){
            for (Field declaredField : declaredFields) {
                //获得属性名字
                String name = declaredField.getName();
                //sql结果
                Object object = rs.getObject(name);
                declaredField.setAccessible(true);
                //属性赋值
                declaredField.set(o,object);
            }
        }
        return o;
    }
    private Object insert(ExtInsert extInsert,Method method, Object[] args){
        //获取到注解上面的sql
        String sql = extInsert.value();
        ConcurrentHashMap<String, Object> paramMap = getParamMap(method, args);
        String[] strings = SQLUtils.sqlInsertParameter(sql);
        ArrayList<Object> list = new ArrayList<Object>();
        for (String string : strings) {
            Object o = paramMap.get(string);
            list.add(o);
        }
        String newSql = SQLUtils.parameQuestion(sql, strings);
        System.out.println(newSql);
        System.out.println(list);
        //执行sql
        int i = JDBCUtils.insert(newSql, false, list);
        return i;
    }

    private ConcurrentHashMap<String,Object> getParamMap(Method method, Object[] args){
        Parameter[] parameters = method.getParameters();
        ConcurrentHashMap<String,Object> concurrentHashMap = new ConcurrentHashMap<String,Object>();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            //参数绑定
            ExtParam extParam = parameter.getAnnotation(ExtParam.class);
            if (extParam != null) {
                String key = extParam.value();
                Object value = args[i];
                concurrentHashMap.put(key,value);
            }
        }
        return concurrentHashMap;
    }

}
