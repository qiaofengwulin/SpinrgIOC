package com.demo.mapper;

import com.demo.entity.User;
import com.demo.orm.annotation.ExtInsert;
import com.demo.orm.annotation.ExtParam;
import com.demo.orm.annotation.ExtSelect;

public interface ExtUserMapper {

    @ExtSelect("select * from user where name=#{name} and sex=#{sex}")
    public User getUser(@ExtParam("name") String name, @ExtParam("sex") int sex);

    @ExtInsert("insert into user(name,sex) values(#{name},#{sex})")
    public int insertUser(@ExtParam("name") String name, @ExtParam("sex") int sex);
}
