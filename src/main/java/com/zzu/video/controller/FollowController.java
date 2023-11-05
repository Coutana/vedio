package com.zzu.video.controller;

import com.alibaba.fastjson.JSONObject;
import com.zzu.video.entity.UserInfo;
import com.zzu.video.service.FollowService;
import com.zzu.video.utils.UserUtil;
import com.zzu.video.vo.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 注释
 *
 * @author Coutana
 * @since 2.9.0
 */
@RestController
public class FollowController {
    /**
     * 关注
     * @param targetId
     * @return
     */
    private final FollowService followService;
    private final UserUtil userUtil;
    @Autowired
    public FollowController(FollowService followService,UserUtil userUtil) {
        this.followService = followService;
        this.userUtil = userUtil;
    }

    /**
     * 关注某人
     * @param targetId
     * @return
     */
    @PostMapping("/follow")
    public JsonResponse<String> follow(@RequestParam("targetId")int targetId) {
        int userId = userUtil.getCurrentUserId();
        followService.follow(userId,targetId);
        return JsonResponse.success();
    }

    /**
     * 查询关注状态
     * @param targetId
     * @return
     */
    @GetMapping("/follow-status/{targetId}")
    public JsonResponse<JSONObject> getFollowStatus(@PathVariable("targetId")int targetId) {
        int userId = userUtil.getCurrentUserId();
        int isFollow = followService.getFollowStatus(userId,targetId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status",isFollow);
        return new JsonResponse<>(jsonObject);
    }

    /**
     * 当前用户关注列表
     * @return
     */
    @GetMapping("/follower-list")
    public JsonResponse<List<UserInfo>> getFollowerList() {
        int userId = userUtil.getCurrentUserId();
        List<UserInfo> result = followService.getFollowerList(userId);
        return new JsonResponse<>(result);
    }

    /**
     * 当前用户粉丝列表
     * @return
     */
    @GetMapping("/followee-list")
    public JsonResponse<List<UserInfo>> getFolloweeList() {
        int userId = userUtil.getCurrentUserId();
        List<UserInfo> result = followService.getFolloweeList(userId);
        return new JsonResponse<>(result);
    }
}
