package com.zzu.video.service;

import com.alibaba.fastjson.JSONObject;
import com.zzu.video.dao.VideoMapper;
import com.zzu.video.entity.UserInfo;
import com.zzu.video.entity.Video;
import com.zzu.video.utils.RedisKeyUtil;
import com.zzu.video.vo.JsonResponse;
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

    private final UserService userService;
    private final LikeService likeService;
    private final ShareService shareService;
    private final CommentService commentService;
    @Autowired
    public VideoService(VideoMapper videoMapper,RedisTemplate redisTemplate,UserService userService,
                        LikeService likeService,ShareService shareService,CommentService commentService) {
        this.videoMapper = videoMapper;
        this.redisTemplate = redisTemplate;
        this.userService = userService;
        this.likeService = likeService;
        this.shareService = shareService;
        this.commentService = commentService;
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

    public UserInfo findUserInfoByVIdeoId(int videoId) {
        int userId = videoMapper.selectUserIdByVideoId(videoId);
        return userService.findUserInfoById(userId);
    }
    public List<Video> findVideoByTag(String tag,int offset,int limit) {
        return videoMapper.selectByTag(tag,offset,limit);
    }

    public List<JSONObject> getVideoResponseData(List<Video> videos,int userId) {
        List<JSONObject> list = new ArrayList<>();
        for(Video video:videos) {
            JSONObject jsonObject = new JSONObject();
            Long likeCount = likeService.findVideoLikeCount(video.getVid());
            int likeStatus = likeService.findVideoLikeStatus(userId,video.getVid());
            jsonObject.put("videoInfo",video);
            jsonObject.put("likeCount",likeCount);
            jsonObject.put("likeStatus",likeStatus);
            list.add(jsonObject);
        }
        return list;
    }
    public JSONObject getVideoDetail(int id) {
        Video videoInfo = findVideoById(id);
        UserInfo userInfo = findUserInfoByVIdeoId(id);
        Long likeCount = likeService.findVideoLikeCount(id);
        Integer shareCount = shareService.findVideoShareCount(id);
        int commentCount = commentService.findCommentCountByVideoId(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("videoInfo",videoInfo);
        jsonObject.put("userInfo",userInfo);
        jsonObject.put("likeCOunt",likeCount);
        jsonObject.put("shareCount",shareCount);
        jsonObject.put("commentCount",commentCount);
        return jsonObject;
    }

}
