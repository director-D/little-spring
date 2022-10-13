package org.springframework.test.mapper;

import org.springframework.annotation.mybatis.*;
import org.springframework.test.entity.User;

/**
 * @Author: vincent
 * @License: (C) Copyright 2005-2200, vincent Corporation Limited.
 * @Contact: lookvincent@163.com
 * @Date: 2022/8/23 16:12
 * @Version: 1.0
 * @Description:
 */
@Mapper
public interface UserMapper {

    @Select("select * from user where id = #{id}")
    User select(@Param("id") int id);

    @Insert("insert into user values(default, #{username}, #{password})")
    int insert(@Param("username") String username, @Param("password") String password);

    @Update("update user set password = #{password} where id = #{id}")
    int update(@Param("id") int id, @Param("password") String password);

    @Delete("delete from user where id = #{id}")
    int delete(@Param("id") int id);


}
