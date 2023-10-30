package com.zzu.video.service;

import com.mysql.cj.util.StringUtils;
import com.zzu.video.dao.UserMapper;
import com.zzu.video.entity.User;
import com.zzu.video.utils.TokenUtil;
import com.zzu.video.vo.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 注释
 *
 * @author Coutana
 * @since 2.9.0
 */
@Service
public class UserService {
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
        return TokenUtil.generateToken(dbUser.getId());
    }

    public int addUser(User user) throws Exception{
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
        user.setCreateTime(new Date());
        return userMapper.insertUser(user);
    }
}
