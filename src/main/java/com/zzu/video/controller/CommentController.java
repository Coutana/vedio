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

import javax.servlet.http.HttpServletRequest;
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
    public CommentController(CommentService commentService, UserService userService,
                             UserUtil userUtil, LikeService likeService) {
        this.commentService = commentService;
        this.userService = userService;
        this.userUtil = userUtil;
        this.likeService = likeService;
    }

    /**
     * 添加评论
     *
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
        responseData.put("comment", newComment);
        responseData.put("userInfo", userInfo);
        responseData.put("status", false);
        responseData.put("likeNum", 0);
        return new JsonResponse<>(responseData);
    }

    /**
     * 获取视频评论
     *
     * @param videoId
     * @param pageId
     * @return
     */
    @GetMapping("/video/{videoId}/page/{pageId}")
    public JsonResponse<List<JSONObject>> getCommentByVideoId(@PathVariable("videoId") int videoId, @PathVariable("pageId") int pageId, HttpServletRequest request) {

        int userId = request.getHeader("Authoritization") != null ? userUtil.getCurrentUserId() : 0;
        int offset = (pageId - 1) * Page.limit;
        int limit = Page.limit;
        List<Comment> result = commentService.findCommentByVideoId(videoId, offset, limit);
        List<JSONObject> list = new ArrayList<>();
        for (Comment comment : result) {
            UserInfo userInfo = userService.findUserInfoById(comment.getUid());
            boolean likeStatus = userId != 0 && likeService.findCommentLikeStatus(userId, comment.getCid());
            Long likeCount = likeService.findCommentLikeCount(comment.getCid());
            JSONObject jsonObject = new JSONObject()
                    .fluentPut("userInfo", userInfo)
                    .fluentPut("comment", comment)
                    .fluentPut("status", likeStatus)
                    .fluentPut("likeNum", likeCount);
            list.add(jsonObject);
        }
        return new JsonResponse<>(list);
    }
}
