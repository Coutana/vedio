package com.zzu.video.controller;

import com.alibaba.fastjson.JSONObject;
import com.zzu.video.entity.Message;
import com.zzu.video.entity.Page;
import com.zzu.video.entity.User;
import com.zzu.video.entity.UserInfo;
import com.zzu.video.service.MessageService;
import com.zzu.video.service.UserService;
import com.zzu.video.utils.UserUtil;
import com.zzu.video.vo.JsonResponse;
import com.zzu.video.vo.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 注释
 *
 * @author Coutana
 * @since 2.9.0
 */
@RestController
public class MessageController {
    private final MessageService messageService;
    private final UserUtil userUtil;
    private final UserService userService;
    @Autowired
    public MessageController(MessageService messageService,UserUtil userUtil,UserService userService) {
        this.messageService = messageService;
        this.userUtil = userUtil;
        this.userService = userService;
    }

    /**
     * 返回每个对话的最新一条私信
     * @return
     */
    @GetMapping("/message/list/{pageId}")
    public JsonResponse<JSONObject> getLetterList(@PathVariable("pageId")int pageId) {
        int userId = userUtil.getCurrentUserId();
        int offset = (pageId-1)* Page.limit;
        int limit = Page.limit;
        JSONObject result = new JSONObject();
        List<Message> conversations = messageService.findConversations(userId,offset,limit);
        List<JSONObject> list = new ArrayList<>();
        if(conversations!=null) {
            for(Message message:conversations) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("messageInfo",message);
                jsonObject.put("messageCount",messageService.findLetterCount(message.getConversationId()));
                jsonObject.put("unReadCount",messageService.findLetterUnreadCount(userId,message.getConversationId()));
                int targetId = userId == message.getFromId() ? message.getToId() : message.getFromId();
                jsonObject.put("targetInfo",userService.findUserInfoById(targetId));
                list.add(jsonObject);
            }
        }
        result.put("list",list);

        return new JsonResponse<>(result);
    }

    @GetMapping("/message/unRead")
    public JsonResponse<JSONObject> getAllUnReadMessageCount() {
        int userId = userUtil.getCurrentUserId();
        int count = messageService.findLetterUnreadCount(userId,null);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("unReadCount",count);
        return new JsonResponse<>(jsonObject);
    }

    @GetMapping("/letter/detail/{conversationId}/{pageId}")
    public JsonResponse<JSONObject> getLetterDetail(@PathVariable("conversationId")String conversationId,
                                                    @PathVariable("pageId")int pageId) {
        int offset = (pageId-1)*Page.limit;
        int limit = Page.limit;
        List<Message> messages = messageService.findLetters(conversationId,offset,limit);
        List<JSONObject> letters = new ArrayList<>();
        for(Message message:messages) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message",message);
            jsonObject.put("fromUser",userService.findUserInfoById(message.getFromId()));
            letters.add(jsonObject);
        }
        JSONObject result = new JSONObject();
        result.put("letters",letters);
        result.put("targetInfo",getLetterTarget(conversationId));

        //设置已读
        List<Integer> ids = getLetterIds(messages);
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }

        return new JsonResponse<>(result);
    }

    private UserInfo getLetterTarget(String conversationId) {
        int userId = userUtil.getCurrentUserId();
        String[] ids = conversationId.split("_");
        int id0 = Integer.parseInt(ids[0]);
        int id1 = Integer.parseInt(ids[1]);

        if (userId == id0) {
            return userService.findUserInfoById(id1);
        } else {
            return userService.findUserInfoById(id0);
        }
    }

    private List<Integer> getLetterIds(List<Message> letterList) {
        int userId = userUtil.getCurrentUserId();
        List<Integer> ids = new ArrayList<>();
        if (letterList != null) {
            for (Message message : letterList) {
                if (userId == message.getToId() && message.getIsRead() == 0) {
                    ids.add(message.getMid());
                }
            }
        }
        return ids;
    }

    @PostMapping("/letter/send")
    public JsonResponse<String> sendLetter(@RequestParam String toname,@RequestParam String content) throws BizException{
        User target = userService.findUserByName(toname);
        if(target==null) {
            throw new BizException("用户不存在！发送私信失败！");
        }
        int fromId = userUtil.getCurrentUserId();
        Message message = new Message();
        message.setFromId(fromId);
        message.setToId(target.getUid());
        message.setContent(content);
        message.setCreateTime(new Date());
        message.setIsRead(0);
        if(fromId<target.getUid()) {
            message.setConversationId(fromId+"_"+target.getUid());
        }
        else {
            message.setConversationId(target.getUid()+"_"+ fromId);
        }
        messageService.addMessage(message);
        return JsonResponse.success();
    }
}
