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
public class RefreshTokenDetail {
    private int id;

    private String refreshToken;

    private int uid;

    private Date createTime;
}
