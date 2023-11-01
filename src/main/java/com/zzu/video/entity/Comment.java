package com.zzu.video.entity;

import lombok.Data;

import java.util.List;

/**
 * 注释
 *
 * @author Coutana
 * @since 2.9.0
 */
@Data
public class Comment {
    private int cid;
    private int uid;
    private int vid;
    private String content;
    private int isRead;
}
