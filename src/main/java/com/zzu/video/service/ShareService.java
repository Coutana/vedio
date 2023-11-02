package com.zzu.video.service;

import com.zzu.video.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 注释
 *
 * @author Coutana
 * @since 2.9.0
 */
@Service
public class ShareService {
    private final RedisTemplate redisTemplate;

    @Autowired
    public ShareService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void shareVideo(int videoId) {
        String shareKey = RedisKeyUtil.getShareKey(videoId);
        redisTemplate.opsForValue().increment(shareKey);
    }

    public Integer findVideoShareCount(int videoId) {
        String shareKey = RedisKeyUtil.getShareKey(videoId);
        Integer result = (Integer)redisTemplate.opsForValue().get(shareKey);
        if(result ==null) {
            redisTemplate.opsForValue().set(shareKey,0);
            return 0;
        }
        return result;
    }
}
