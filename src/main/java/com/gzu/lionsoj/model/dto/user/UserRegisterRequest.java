package com.gzu.lionsoj.model.dto.user;

import java.io.Serializable;
import lombok.Data;

/**
 * @Classname: UserRegisterRequest
 * @Description: 用户注册请求封装
 * @Author: lions
 * @Datetime: 12/28/2023 10:50 PM
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
