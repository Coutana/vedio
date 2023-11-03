package com.zzu.video.service;

import com.zzu.video.entity.UserInfo;
import com.zzu.video.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 注释
 *
 * @author Coutana
 * @since 2.9.0
 */
@Service
public class FollowService {
    private final RedisTemplate redisTemplate;
    private final UserService userService;
    @Autowired
    public FollowService(RedisTemplate redisTemplate,UserService userService) {
        this.redisTemplate = redisTemplate;
        this.userService = userService;
    }
    public void follow(int userId,int targetId) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(targetId);
        String followerKey = RedisKeyUtil.getFollowerKey(userId);
        boolean isFollow = redisTemplate.opsForSet().isMember(followeeKey,userId);
        if(isFollow) {
            redisTemplate.opsForSet().remove(followeeKey,userId);
            redisTemplate.opsForSet().remove(followerKey,targetId);
        }
        else {
            redisTemplate.opsForSet().add(followeeKey,userId);
            redisTemplate.opsForSet().add(followerKey,targetId);
        }
    }

    public int getFollowStatus(int userId,int targetId) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(targetId);
        boolean isFollow = redisTemplate.opsForSet().isMember(followeeKey,userId);
        if(isFollow) {
            return 1;
        }
        else {
            return 0;
        }
    }

    /**
     * 获取用户关注者列表
     * @return
     */
    public List<UserInfo> getFollowerList(int userId) {
        String followerKey = RedisKeyUtil.getFollowerKey(userId);
        Set<Integer> set = redisTemplate.opsForSet().members(followerKey);
        return userService.getUserInfoByIds(set);
    }
    /**
     * 获取用户粉丝列表
     * @return
     */
    public List<UserInfo> getFolloweeList(int userId) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId);
        Set<Integer> set = redisTemplate.opsForSet().members(followeeKey);
        return userService.getUserInfoByIds(set);
    }

}
