package com.zzu.video.controller;

import com.alibaba.fastjson.JSONObject;
import com.zzu.video.entity.Video;
import com.zzu.video.service.VideoService;
import com.zzu.video.utils.UserUtil;
import com.zzu.video.vo.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 注释
 *
 * @author Coutana
 * @since 2.9.0
 */
@RestController
public class LikeController {
    private final UserUtil userUtil;
    private final VideoService videoService;

    @Autowired
    public LikeController(UserUtil userUtil,VideoService videoService) {
        this.userUtil = userUtil;
        this.videoService = videoService;
    }

    /**
     * 点赞视频
     * @param videoId
     * @return
     */
    @PostMapping("/video-like")
    public JsonResponse<String> addVideoLike(@RequestParam int videoId){
        int userId = userUtil.getCurrentUserId();
        videoService.like(videoId, userId);
        return JsonResponse.success();
    }

    /**
     * 查询视频点赞数量
     * @param id
     * @return
     */
    @GetMapping("/like-count/{id}")
    public JsonResponse<JSONObject> getLikeCount(@PathVariable("id")int id) {
        long count = videoService.findVideoLikeCount(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count",count);
        return new JsonResponse<>(jsonObject);
    }

    /**
     * 查询用户点赞视频列表
     * @return
     */
    @GetMapping("/user-like")
    public JsonResponse<List<Video>> getUserLikeVideoList() {
        int userId = userUtil.getCurrentUserId();
        List<Video> list = videoService.findUserLikeVideo(userId);
        return new JsonResponse<>(list);
    }

    /**
     * 查询用户对视频点赞状态
     * @param id
     * @return
     */
    @GetMapping("/like-status/{id}")
    public JsonResponse<JSONObject> getUserLikeStatus(@PathVariable("id")int id) {
        int userId = userUtil.getCurrentUserId();
        int status = videoService.findVideoLikeStatus(userId,id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status",status);
        return new JsonResponse<>(jsonObject);

    }
}
