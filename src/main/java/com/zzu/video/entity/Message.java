package com.zzu.video.entity;

import lombok.Data;

import java.util.Date;

/**
 * 注释
 *
 * @author Coutana
 * @since 2.9.0
 */
@Data
public class Message {
    private int mid;
    private int fromId;
    private int toId;
    private String content;
    private int isRead;
    private Date createTime;
}
