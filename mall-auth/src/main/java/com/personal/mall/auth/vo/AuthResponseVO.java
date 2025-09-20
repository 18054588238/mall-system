package com.personal.mall.auth.vo;

import lombok.Data;

/**
 * @ClassName UserResponseVO
 * @Author liupanpan
 * @Date 2025/9/20
 * @Description
 */
@Data
public class AuthResponseVO {
    private String accessToken;
    private long expiresIn;
    private long remindIn;
    private String uid;
}
