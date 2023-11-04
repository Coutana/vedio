package com.zzu.video.controller;

import com.alibaba.fastjson.JSONObject;
import com.zzu.video.entity.Video;
import com.zzu.video.service.LikeService;
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
    private final LikeService likeService;

    @Autowired
    public LikeController(UserUtil userUtil,LikeService likeService) {
        this.userUtil = userUtil;
        this.likeService = likeService;
    }

    /**
     * 点赞视频
     * @param videoId
     * @return
     */
    @PostMapping("/like-video")
    public JsonResponse<Boolean> addVideoLike(@RequestParam int videoId){
        int userId = userUtil.getCurrentUserId();
        likeService.likeVideo(videoId, userId);
        boolean likeStatus = likeService.findVideoLikeStatus(userId,videoId);
        return new JsonResponse<>(likeStatus);
    }

    /**
     * 查询视频点赞数量
     * @param id
     * @return
     */
    @GetMapping("/video/like-count/{id}")
    public JsonResponse<JSONObject> getVideoLikeCount(@PathVariable("id")int id) {
        long count = likeService.findVideoLikeCount(id);
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
        List<Video> list = likeService.findUserLikeVideo(userId);
        return new JsonResponse<>(list);
    }

    /**
     * 查询用户对视频点赞状态
     * @param id
     * @return
     */
    @GetMapping("/video/like-status/{id}")
    public JsonResponse<JSONObject> getVideoLikeStatus(@PathVariable("id")int id) {
        int userId = userUtil.getCurrentUserId();
        boolean status = likeService.findVideoLikeStatus(userId,id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status",status);
        return new JsonResponse<>(jsonObject);

    }

    /**
     * 点赞评论
     * @param videoId
     * @return
     */
    @PostMapping("/like-comment")
    public JsonResponse<Boolean> addCommentLike(@RequestParam int videoId){
        int userId = userUtil.getCurrentUserId();
        likeService.likeComment(videoId, userId);
        boolean likeStatus = likeService.findCommentLikeStatus(userId,videoId);
        return new JsonResponse<>(likeStatus);
    }

    /**
     * 查询评论点赞数量
     * @param id
     * @return
     */
    @GetMapping("/comment/like-count/{id}")
    public JsonResponse<JSONObject> getCommentLikeCount(@PathVariable("id")int id) {
        long count = likeService.findVideoLikeCount(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count",count);
        return new JsonResponse<>(jsonObject);
    }

    /**
     * 查询用户对评论点赞状态
     * @param
     * @return
     */
    @GetMapping("/commenmt/like-status/{commentId}")
    public JsonResponse<JSONObject> getCommentLikeStatus(@PathVariable("commentId")int commentId) {
        int userId = userUtil.getCurrentUserId();
        boolean status = likeService.findVideoLikeStatus(userId,commentId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status",status);
        return new JsonResponse<>(jsonObject);

    }

}
