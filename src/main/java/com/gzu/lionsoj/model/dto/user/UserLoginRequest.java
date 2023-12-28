package com.gzu.lionsoj.model.dto.user;

import java.io.Serializable;
import lombok.Data;

/**
 * @Classname: UserLoginRequest
 * @Description: 用户登录请求封装
 * @Author: lions
 * @Datetime: 12/28/2023 10:49 PM
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    private String userAccount;

    private String userPassword;
}
