package com.zzu.video.controller;

import com.alibaba.fastjson.JSONObject;
import com.zzu.video.entity.Comment;
import com.zzu.video.entity.Page;
import com.zzu.video.entity.UserInfo;
import com.zzu.video.service.CommentService;
import com.zzu.video.service.LikeService;
import com.zzu.video.service.UserService;
import com.zzu.video.utils.UserUtil;
import com.zzu.video.vo.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 注释
 *
 * @author Coutana
 * @since 2.9.0
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;
    private final UserUtil userUtil;
    private final LikeService likeService;
    @Autowired
    public CommentController(CommentService commentService,UserService userService,
                             UserUtil userUtil,LikeService likeService) {
        this.commentService = commentService;
        this.userService = userService;
        this.userUtil = userUtil;
        this.likeService = likeService;
    }

    /**
     * 添加评论
     * @param comment
     * @return
     */
    @PostMapping("/add")
    public JsonResponse<JSONObject> addComment(@RequestBody Comment comment) {
        int userId = userUtil.getCurrentUserId();
        comment.setUid(userId);
        Comment newComment = commentService.addComment(comment);
        UserInfo userInfo = userService.findUserInfoById(userId);
        JSONObject responseData = new JSONObject();
        responseData.put("commentInfo",newComment);
        responseData.put("userInfo",userInfo);
        responseData.put("likeStatus",false);
        responseData.put("likeCount",0);
        return new JsonResponse<>(responseData);
    }

    /**
     * 获取视频评论
     * @param videoId
     * @param pageId
     * @return
     */
    @GetMapping("/{videoId}/{pageId}")
    public JsonResponse<JSONObject> getCommentByVideoId(@PathVariable("videoId")int videoId,
                                                              @PathVariable("pageId")int pageId) {
        int userId = userUtil.getCurrentUserId();
        int offset = (pageId-1)* Page.limit;
        int limit = Page.limit;
        List<Comment> result = commentService.findCommentByVideoId(videoId,offset,limit);
        List<JSONObject> list = new ArrayList<>();
        for(Comment comment:result) {
            JSONObject jsonObject = new JSONObject();
            UserInfo userInfo = userService.findUserInfoById(comment.getUid());
            boolean likeStatus = likeService.findCommentLikeStatus(userId,comment.getCid());
            Long likeCount = likeService.findCommentLikeCount(comment.getCid());
            jsonObject.put("userInfo",userInfo);
            jsonObject.put("commentInfo",comment);
            jsonObject.put("likeStatus",likeStatus);
            jsonObject.put("likeCount",likeCount);
            list.add(jsonObject);
        }
        JSONObject responseData = new JSONObject();
        responseData.put("commentList",list);
        return new JsonResponse<>(responseData);
    }
}
