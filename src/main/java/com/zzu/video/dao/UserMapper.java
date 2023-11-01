package com.zzu.video.dao;

import com.zzu.video.entity.RefreshTokenDetail;
import com.zzu.video.entity.User;
import com.zzu.video.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Mapper
public interface UserMapper {
    User selectById(int userId);

    User selectByName(String username);

    int insertUser(User user);

    int updateHeader(int id, String headerUrl);

    int updatePassword(int id, String password);

    int deleteRefreshTokenByUserId(int userId);

    int deleteRefreshToken(String refreshToken,int userId);

    int addRefreshToken(String refreshToken, int userId, Date createTime);

    RefreshTokenDetail getRefreshTokenDetail(String refreshToken);

    List<User> getUserByIds(Set<Integer> userIdList);

}
