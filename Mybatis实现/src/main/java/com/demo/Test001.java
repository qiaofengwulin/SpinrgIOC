package com.demo;

import com.demo.entity.User;
import com.demo.mapper.ExtUserMapper;
import com.demo.orm.SQLSession;

import java.io.IOException;

/**
 * @ClassName Test001
 * @Description TODO
 * @Author qiaozhonghuai
 * @Date 2019/9/10 0010 下午 16:55
 * @Version 1.0
 **/
public class Test001 {
    public static void main(String[] args) throws IOException {
        ExtUserMapper mapper = SQLSession.getMapper(ExtUserMapper.class);
        int i = mapper.insertUser("李狗蛋", 1);
        System.out.println(i);
        User u = mapper.getUser("王二狗", 2);
        System.out.println(u.toString());
    }
}
