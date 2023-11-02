package com.zzu.video.service;

import com.mysql.cj.util.StringUtils;
import com.zzu.video.constants.UserConstant;
import com.zzu.video.dao.UserMapper;
import com.zzu.video.entity.RefreshTokenDetail;
import com.zzu.video.entity.User;
import com.zzu.video.entity.UserInfo;
import com.zzu.video.utils.MD5Util;
import com.zzu.video.utils.RSAUtil;
import com.zzu.video.utils.TokenUtil;
import com.zzu.video.vo.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 注释
 *
 * @author Coutana
 * @since 2.9.0
 */
@Service
public class UserService implements UserConstant {
    private final UserMapper userMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public String login(User user) throws Exception{
        String username = user.getUsername();
        String password = user.getPassword();
        if(StringUtils.isNullOrEmpty(username)||StringUtils.isNullOrEmpty(password)) {
            throw new BizException("参数异常！");
        }
        User dbUser = userMapper.selectByName(username);
        if(dbUser==null) {
            throw new BizException("用户不存在！");
        }

        if(!dbUser.getPassword().equals(user.getPassword())) {
            throw  new BizException("密码错误！");
        }
        return TokenUtil.generateToken(dbUser.getUid());
    }

    public void addUser(User user) throws Exception{
        String username = user.getUsername();
        String password = user.getPassword();
        if(StringUtils.isNullOrEmpty(username)) {
            throw new BizException("用户名不能为空!");
        }
        if(StringUtils.isNullOrEmpty(password)) {
            throw new BizException("密码不能为空!");
        }
        User dbUser =  userMapper.selectByName(username);
        if(dbUser!=null) {
            throw new BizException("该用户已注册！");
        }
        Date now = new Date();
        String salt = String.valueOf(now.getTime());
        String rawPassword;
        try{
            rawPassword = RSAUtil.decrypt(password);
        }catch (Exception e){
            throw new BizException("密码解密失败！");
        }
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        user.setSalt(salt);
        user.setPassword(md5Password);
        user.setCreateTime(now);
        UserInfo userInfo = new UserInfo();
        userInfo.setUid(user.getUid());
        userInfo.setNickname(NICK_NAME);
        userInfo.setGender(GENDER_UNKNOW);
        userInfo.setAge(DEFAULT_AGE);
        userInfo.setDesc(DESC);
        user.setUserInfo(userInfo);
        userMapper.insertUser(user);
    }

    public UserInfo findUserInfoById(int userId) {
        User user = userMapper.selectById(userId);
        if(user==null) {
            throw new BizException("用户信息不存在！");
        }
        return user.getUserInfo();
    }

    public Map<String, Object> loginForDts(User user) throws Exception{
        String username = user.getUsername()==null?"":user.getUsername();
        String password = user.getPassword()==null?"":user.getPassword();
        if(StringUtils.isNullOrEmpty(username) || StringUtils.isNullOrEmpty(password)){
            throw new BizException("参数异常！");
        }
        User dbUser = userMapper.selectByName(username);
        if(dbUser == null){
            throw new BizException("当前用户不存在！");
        }
        String rawPassword;
        try{
            rawPassword = RSAUtil.decrypt(password);
        }catch (Exception e){
            throw new BizException("密码解密失败！");
        }
        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        if(!md5Password.equals(dbUser.getPassword())){
            throw new BizException("密码错误！");
        }
        int userId = dbUser.getUid();
        String accessToken = TokenUtil.generateToken(userId);
        String refreshToken = TokenUtil.generateRefreshToken(userId);
        //保存refresh token到数据库
        userMapper.deleteRefreshTokenByUserId(userId);
        userMapper.addRefreshToken(refreshToken, userId, new Date());
        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        return result;
    }

    public void logout(String refreshToken,int userId) {
        userMapper.deleteRefreshToken(refreshToken,userId);
    }

    public String refreshAccessToken(String refreshToken) throws Exception {
        RefreshTokenDetail refreshTokenDetail = userMapper.getRefreshTokenDetail(refreshToken);
        if(refreshTokenDetail == null){
            throw new BizException("555"," refreshtoken 已过期！");
        }
        int userId = refreshTokenDetail.getUid();
        return TokenUtil.generateToken(userId);
    }

    public List<UserInfo> getUserInfoByIds(Set<Integer> set) {
        List<User> list = userMapper.getUserByIds(set);
        List<UserInfo> result = new ArrayList<>();
        for(User user:list) {
            result.add(user.getUserInfo());
        }
        return result;
    }
}
