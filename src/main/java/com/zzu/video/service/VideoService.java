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

import java.util.List;

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

    public int like(int id,int userId) {
        return 1;
    }
    public Long findVideoLikeCount(int id) {
        return redisTemplate.opsForSet().size(RedisKeyUtil.getVideoLikeKey(id));
    }

}
