package com.zzu.video.controller;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.util.Auth;
import com.zzu.video.entity.Video;
import com.zzu.video.service.VideoService;
import com.zzu.video.utils.UserUtil;
import com.zzu.video.vo.JsonResponse;
import com.zzu.video.vo.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    public VideoController(UserUtil userUtil,VideoService videoService) {
        this.userUtil = userUtil;
        this.videoService = videoService;
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
     * 查询用户发布的视频列表
     *
     * @author Coutana
     * @since 2.9.0
     */
    @PostMapping("/video/{userId}")
    public JsonResponse<List<Video>> getUserVideoList(@PathVariable("userId")int userId) throws BizException{
        List<Video> result = videoService.findVideoByUserId(userId);
        return new JsonResponse<>(result);
    }

    /**
     * 查询用户点赞视频列表
     * @param userId
     * @return
     * @throws BizException
     */
    @GetMapping("/user-like/{userId}")
    public JsonResponse<List<Video>> getUserLikeList(@PathVariable("userId")int userId) throws BizException{
        List<Video> result = videoService.findUserLikeVideo(userId);
        return new JsonResponse<>(result);

    }

    /**
     * 获取视频信息
     * @param id
     * @return
     * @throws BizException
     */
    @GetMapping("/detail/{id}")
    public JsonResponse<Video> detail(@PathVariable("id") int id) throws BizException{
        Video result = videoService.findVideoById(id);
        return new JsonResponse<>(result);
    }

    /**
     * 点赞视频
     * @param id
     * @param userId
     * @return
     * @throws BizException
     */
    @PostMapping("/like")
    public JsonResponse<String> like(@RequestBody int id,@RequestBody int userId) throws BizException{
        videoService.like(id,userId);
        return JsonResponse.success();
    }

}
