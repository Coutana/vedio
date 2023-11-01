package com.zzu.video.service;

import com.zzu.video.dao.VideoMapper;
import com.zzu.video.entity.Video;
import com.zzu.video.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * 注释
 *
 * @author Coutana
 * @since 2.9.0
 */
@Service
public class VideoService {

    private final VideoMapper videoMapper;

    private final RedisTemplate redisTemplate;
    @Autowired
    public VideoService(VideoMapper videoMapper,RedisTemplate redisTemplate) {
        this.videoMapper = videoMapper;
        this.redisTemplate = redisTemplate;
    }

    public int addVideo(Video video) {
        return videoMapper.insertVideo(video);
    }

    public Video findVideoById(int id) {
        return videoMapper.selectById(id);
    }

    public List<Video> findVideoByUserId(int userId) {
        return videoMapper.selectByUserId(userId);
    }

    public void like(int id,int userId) {
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

    public Long findVideoLikeCount(int id) {
        return redisTemplate.opsForSet().size(RedisKeyUtil.getVideoLikeKey(id));
    }

    public Set<Integer> findUserLikeVideoId(int userId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        return redisTemplate.opsForSet().members(userLikeKey);
    }

    public List<Video> findUserLikeVideo(int userId) {
        Set<Integer> set = findUserLikeVideoId(userId);
        List<Video> result = new ArrayList<Video>();
        for(int s:set) {
            result.add(findVideoById(s));
        }
        return result;
    }

    public int findVideoLikeStatus(int userId,int id) {
        String videoLikeKey = RedisKeyUtil.getVideoLikeKey(id);
        boolean isMember = redisTemplate.opsForSet().isMember(videoLikeKey,userId);
        if(isMember) {
            return 1;
        }
        else {
            return 0;
        }
    }

}
