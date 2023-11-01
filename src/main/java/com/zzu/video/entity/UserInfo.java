package com.zzu.video.entity;

import lombok.Data;

/**
 * 注释
 *
 * @author Coutana
 * @since 2.9.0
 */

@Data
public class UserInfo {
    private int uid;
    private String nickname;
    private String avatar;
    private int gender;
    private int age;
    private String desc;
}
