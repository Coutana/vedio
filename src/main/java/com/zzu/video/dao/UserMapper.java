package com.zzu.video.dao;

import com.zzu.video.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User selectById(int id);

    User selectByName(String name);

    int insertUser(User user);

    int updateHeader(int id, String headerUrl);

    int updatePassword(int id, String password);
}
