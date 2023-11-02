package com.zzu.video.controller;

import com.alibaba.fastjson.JSONObject;
import com.zzu.video.service.ShareService;
import com.zzu.video.vo.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 注释
 *
 * @author Coutana
 * @since 2.9.0
 */
@RestController
public class ShareController {
    private final ShareService shareService;
    @Autowired
    public ShareController(ShareService shareService) {
        this.shareService = shareService;
    }

    /**
     * 分享视频
     * @param id
     * @return
     */
    @GetMapping("/video-share/{id}")
    public JsonResponse<String> share(@PathVariable("id") int id) {
        shareService.shareVideo(id);
        return JsonResponse.success();
    }

    @GetMapping("/share-count/{id}")
    public JsonResponse<JSONObject> getShareCount(@PathVariable("id")int id) {
        int count = shareService.findVideoShareCount(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("shareCount",count);
        return new JsonResponse<>(jsonObject);
    }

}
