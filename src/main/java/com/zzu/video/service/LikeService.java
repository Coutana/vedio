package com.zzu.video.service;

import com.zzu.video.controller.LikeController;
import com.zzu.video.dao.VideoMapper;
import com.zzu.video.entity.UserInfo;
import com.zzu.video.entity.Video;
import com.zzu.video.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 注释
 *
 * @author Coutana
 * @since 2.9.0
 */
@Service
public class LikeService {

    private final VideoMapper videoMapper;

    private final RedisTemplate redisTemplate;

    @Autowired
    public LikeService(RedisTemplate redisTemplate,
                       VideoMapper videoMapper) {
        this.redisTemplate = redisTemplate;
        this.videoMapper = videoMapper;
    }

    public void likeVideo(int id,int userId) {
        String videoLikeKey = RedisKeyUtil.getVideoLikeKey(id);
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        boolean isMember = redisTemplate.opsForSet().isMember(videoLikeKey,userId);
        if(isMember) {
            redisTemplate.opsForSet().remove(videoLikeKey,userId);
            redisTemplate.opsForSet().remove(userLikeKey,id);
        }
        else {
            redisTemplate.opsForSet().add(videoLikeKey,userId);
            redisTemplate.opsForSet().add(userLikeKey,id);
        }
    }

    public Long findVideoLikeCount(int videoId) {
        return redisTemplate.opsForSet().size(RedisKeyUtil.getVideoLikeKey(videoId));
    }

    public Set<Integer> findUserLikeVideoId(int userId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        return redisTemplate.opsForSet().members(userLikeKey);
    }

    public List<Video> findUserLikeVideo(int userId) {
        Set<Integer> set = findUserLikeVideoId(userId);
        List<Video> result = new ArrayList<Video>();
        for(int s:set) {
            result.add(videoMapper.selectById(s));
        }
        return result;
    }

    public boolean findVideoLikeStatus(int userId,int videoId) {
        String videoLikeKey = RedisKeyUtil.getVideoLikeKey(videoId);
        boolean isMember = redisTemplate.opsForSet().isMember(videoLikeKey,userId);
        if(isMember) {
            return true;
        }
        else {
            return false;
        }
    }

    public void likeComment(int commentId,int userId) {
        String videoLikeKey = RedisKeyUtil.getCommentLikeKey(commentId);
        boolean isMember = redisTemplate.opsForSet().isMember(videoLikeKey,userId);
        if(isMember) {
            redisTemplate.opsForSet().remove(videoLikeKey,userId);
        }
        else {
            redisTemplate.opsForSet().add(videoLikeKey,userId);
        }
    }

    public Long findCommentLikeCount(int id) {
        return redisTemplate.opsForSet().size(RedisKeyUtil.getCommentLikeKey(id));
    }

    public int findCommentLikeStatus(int userId,int commentId) {
        String videoLikeKey = RedisKeyUtil.getCommentLikeKey(commentId);
        boolean isMember = redisTemplate.opsForSet().isMember(videoLikeKey,userId);
        if(isMember) {
            return 1;
        }
        else {
            return 0;
        }
    }

}
