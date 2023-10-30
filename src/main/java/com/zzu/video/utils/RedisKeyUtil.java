package com.zzu.video.utils;

import org.springframework.stereotype.Component;

/**
 * 注释
 *
 * @author Coutana
 * @since 2.9.0
 */
public class RedisKeyUtil {
    private static final String SPLIT = ":";
    private static final String PREFIX_VIDEO_LIKE = "like:video";
    private static final String PREFIX_USER_LIKE = "like:user";
    private static final String PREFIX_FOLLOWEE = "followee";
    private static final String PREFIX_FOLLOWER = "follower";
    private static final String PREFIX_TICKET = "ticket";
    private static final String PREFIX_USER = "user";
    private static final String PREFIX_UV = "uv";

    public static String getVideoLikeKey(int id) {
        return PREFIX_VIDEO_LIKE + SPLIT + id;
    }

    public static String getUserLikeKey(int id) {
        return PREFIX_USER + SPLIT + id;
    }
}
