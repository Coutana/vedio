package com.zzu.video.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zzu.video.dao.UserMapper;
import com.zzu.video.entity.User;
import com.zzu.video.entity.UserInfo;
import com.zzu.video.service.UserService;
import com.zzu.video.utils.RSAUtil;
import com.zzu.video.utils.UserUtil;
import com.zzu.video.vo.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 注释
 *
 * @author Coutana
 * @since 2.9.0
 */
@RestController
public class UserController {
    private final UserService userService;
    private final UserUtil userUtil;
    @Autowired
    public UserController(UserService userService, UserUtil userUtil) {
        this.userService = userService;
        this.userUtil = userUtil;
    }

    /**
     * 添加用户
     * @param user
     * @return
     * @throws Exception
     */
    @PostMapping("/user")
    public JsonResponse<String> addUser(@RequestBody User user) throws Exception {
        userService.addUser(user);
        return JsonResponse.success();
    }

    /**
     * 获取accessToken
     * @param user
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    public JsonResponse<JSONObject> login(@RequestBody User user) throws Exception{
        JSONObject jsonObject = new JSONObject();
        String token = userService.login(user);
        jsonObject.put("token",token);
        return new JsonResponse<>(jsonObject);
    }

    /**
     * 获取公钥
     * @return
     */
    @GetMapping("/rsa-pks")
    public JsonResponse<String> getRsaPulicKey(){
        String pk = RSAUtil.getPublicKeyStr();
        return new JsonResponse<>(pk);
    }

    /**
     * 获取用户信息
     * @param userId
     * @return
     * @throws Exception
     */
    @GetMapping("/info/{userId}")
    public JsonResponse<UserInfo> getUserInfo(@PathVariable("userId")int userId) throws Exception {
        UserInfo userInfo = userService.findUserInfoById(userId);
        return new JsonResponse<>(userInfo);
    }

    /**
     * 获取双token
     * @param user
     * @return
     * @throws Exception
     */
    @PostMapping("/user-dts")
    public JsonResponse<Map<String, Object>> loginForDts(@RequestBody User user) throws Exception {
        Map<String, Object> map = userService.loginForDts(user);
        return new JsonResponse<>(map);
    }

    /**
     * 登出并删除当前token
     * @param request
     * @return
     */
    @DeleteMapping("/logout")
    public JsonResponse<String> logout(HttpServletRequest request){
        String refreshToken = request.getHeader("refreshToken");
        int userId = userUtil.getCurrentUserId();
        userService.logout(refreshToken, userId);
        return JsonResponse.success();
    }

    /**
     * 刷新accessToken
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/access-tokens")
    public JsonResponse<String> refreshAccessToken(HttpServletRequest request) throws Exception {
        String refreshToken = request.getHeader("refreshToken");
        String accessToken = userService.refreshAccessToken(refreshToken);
        return new JsonResponse<>(accessToken);
    }

}
