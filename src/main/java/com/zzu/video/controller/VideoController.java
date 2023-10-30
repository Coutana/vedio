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

/**
 * 注释
 *
 * @author Coutana
 * @since 2.9.0
 */
@RestController
@RequestMapping("/video")
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
    @PostMapping("/uploadToken")
    public JsonResponse<String> upload() throws Exception{
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        return new JsonResponse<>(upToken);

    }

    @PostMapping("/download")
    public JsonResponse<String> download() throws Exception {
        String fileName = "1.jpg";
        String publicUrl = String.format("%s/%s", domainOfBucket, fileName);
        return new JsonResponse<>(publicUrl);
    }
    @PostMapping("/add")
    public JsonResponse<String> add(@RequestBody Video video) throws BizException {
        int userId = userUtil.getCurrentUserId();
        video.setUserId(userId);
        video.setCreateTime(new Date());
        if(videoService.addVideo(video)==1) {
            return JsonResponse.success();
        }
        return JsonResponse.fail();
    }
    /**
     * 查询自己发布的帖子
     *
     * @author Coutana
     * @since 2.9.0
     */
    @PostMapping("/self")
    public JsonResponse<JSONObject> self() throws BizException{
        int userId = userUtil.getCurrentUserId();
        List<Video> result = videoService.findVideoByUserId(userId);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(result);
        return new JsonResponse<>(jsonObject);
    }
    @GetMapping("/detail/{id}")
    public JsonResponse<JSONObject> detail(@PathVariable("id") int id) throws BizException{
        int userId = userUtil.getCurrentUserId();
        Video result = videoService.findVideoById(id);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(result);
        return new JsonResponse<>(jsonObject);
    }

    @PostMapping("/like")
    public JsonResponse<String> like(int id) {
        int userId = userUtil.getCurrentUserId();
        return JsonResponse.success();
    }
}
