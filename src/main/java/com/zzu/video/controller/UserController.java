package com.zzu.video.controller;

import com.alibaba.fastjson.JSONObject;
import com.zzu.video.entity.User;
import com.zzu.video.service.UserService;
import com.zzu.video.vo.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注释
 *
 * @author Coutana
 * @since 2.9.0
 */
@RestController
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/token")
    public JsonResponse<JSONObject> login(@RequestBody User user) throws Exception{
        JSONObject jsonObject = new JSONObject();
        String token = userService.login(user);
        jsonObject.put("token",token);
        return new JsonResponse<>(jsonObject);
    }
    @PostMapping("/add")
    public JsonResponse<String> addUser(@RequestBody User user) throws Exception {
        userService.addUser(user);
        return JsonResponse.success();
    }
}
