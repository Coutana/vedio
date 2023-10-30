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
public class User {
    private int id;
    private String username;
    private String password;
    private String headerUrl;
    private Date createTime;
}
