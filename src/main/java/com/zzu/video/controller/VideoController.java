package com.zzu.video.controller;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.util.Auth;
import com.zzu.video.entity.Page;
import com.zzu.video.entity.UserInfo;
import com.zzu.video.entity.Video;
import com.zzu.video.service.CommentService;
import com.zzu.video.service.LikeService;
import com.zzu.video.service.ShareService;
import com.zzu.video.service.VideoService;
import com.zzu.video.utils.UserUtil;
import com.zzu.video.vo.JsonResponse;
import com.zzu.video.vo.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 注释
 *
 * @author Coutana
 * @since 2.9.0
 */
@RestController
public class VideoController {
    String accessKey = "kuL2ovVRmvHDPEtsFe62bVk1EZ1Vv8v23DsXBEuD";
    String secretKey = "Gk3WQ-xe1iLVekerGVin8pPDr8Za-r7uPYypacZD";
    String bucket = "666java";
    String domainOfBucket =  "s3bs97u6m.hb-bkt.clouddn.com";

    private final UserUtil userUtil;
    private final VideoService videoService;
    private final LikeService likeService;

    @Autowired
    public VideoController(UserUtil userUtil,VideoService videoService,LikeService likeService) {
        this.userUtil = userUtil;
        this.videoService = videoService;
        this.likeService = likeService;
    }

    /**
     * 获取文件地址
     * @return
     * @throws Exception
     */
    @PostMapping("/download")
    public JsonResponse<String> download() throws Exception {
        String fileName = "1.jpg";
        String publicUrl = String.format("%s/%s", domainOfBucket, fileName);
        return new JsonResponse<>(publicUrl);
    }

    /**
     * 获取上传视频token
     * @param video
     * @return
     * @throws BizException
     */
    @PostMapping("/uploadToken")
    public JsonResponse<String> add(@RequestBody Video video) throws BizException {
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        int userId = userUtil.getCurrentUserId();
        video.setUid(userId);
        video.setCreateTime(new Date());
        videoService.addVideo(video);
        return new JsonResponse<>(upToken);
    }

    /**
     * 查询某个用户发布的视频列表，并展示当前用户点赞状态
     * @author Coutana
     * @since 2.9.0
     */
    @GetMapping("/user-publish/{userId}")
    public JsonResponse<List<JSONObject>> getUserVideoList(@PathVariable("userId")int userId) throws BizException{
        int uid = userUtil.getCurrentUserId();//当前用户Id
        List<Video> result = videoService.findVideoByUserId(userId);
        List<JSONObject> list = videoService.getVideoResponseData(result,uid);
        return new JsonResponse<>(list);
    }

    /**
     * 查询某个用户点赞视频列表,，并展示当前用户点赞状态
     * @param userId
     * @return
     * @throws BizException
     */
    @GetMapping("/user-like/{userId}")
    public JsonResponse<List<JSONObject>> getUserLikeList(@PathVariable("userId")int userId) throws BizException{
        int uid = userUtil.getCurrentUserId();//当前用户Id
        List<Video> result = likeService.findUserLikeVideo(userId);
        List<JSONObject> list = videoService.getVideoResponseData(result,uid);
        return new JsonResponse<>(list);
    }

    /**
     * 获取详细视频信息
     * @param id
     * @return
     * @throws BizException
     */
    @GetMapping("/detail/{id}")
    public JsonResponse<JSONObject> getDetail(@PathVariable("id") int id) throws BizException{
        int userId = userUtil.getCurrentUserId();
        JSONObject jsonObject = videoService.getVideoDetail(id,userId);
        return new JsonResponse<>(jsonObject);
    }

    /**
     * 标签查找视频
     * @param tag
     * @param pageId
     * @return
     */
    @GetMapping("/video-list/{tag}/{pageId}")
    public JsonResponse<JSONObject> getVideoByTag(@PathVariable("tag") String tag,
                                                   @PathVariable("pageId")int pageId) {
        int userId = userUtil.getCurrentUserId();
        int offset = (pageId-1)* Page.limit;
        int limit = Page.limit;
        List<Video> result = videoService.findVideoByTag(tag,offset,limit);
        List<JSONObject> list = videoService.getVideoResponseData(result,userId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("videoList",list);
        return new JsonResponse<>(jsonObject);
    }
}
