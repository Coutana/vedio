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
    private static final String PREFIX_COMMENT_LIKE = "like:comment";
    private static final String PREFIX_FOLLOWEE = "followee";//被关注者
    private static final String PREFIX_FOLLOWER = "follower";//关注者
    private static final String PREFIX_SHARE = "share";


    public static String getVideoLikeKey(int id) {
        return PREFIX_VIDEO_LIKE + SPLIT + id;
    }

    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    public static String getFolloweeKey(int userId) {
        return PREFIX_FOLLOWEE + SPLIT + userId;
    }
    public static String getFollowerKey(int userId) {
        return PREFIX_FOLLOWER + SPLIT + userId;
    }
    public static String getShareKey(int videoId) {
        return PREFIX_SHARE + SPLIT + videoId;
    }
    public static String getCommentLikeKey(int commentId) {
        return PREFIX_COMMENT_LIKE + SPLIT + commentId;
    }
}
