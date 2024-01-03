package com.gzu.lionsoj.judge.sandbox.impl;

import com.gzu.lionsoj.judge.sandbox.CodeSandbox;
import com.gzu.lionsoj.judge.sandbox.model.ExecuteCodeRequest;
import com.gzu.lionsoj.judge.sandbox.model.ExecuteCodeResponse;

/**
 * @ClassName: ThirdPartyCodeSandbox
 * @Description: 第三方代码沙箱实现
 * @Author: Lions
 * @Datetime: 1/4/2024 3:14 AM
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    /**
     * @param executeCodeRequest 判题请求封装
     * @Description: 执行判题
     * @Return: 判题响应封装
     * @Author: lions
     * @Datetime: 1/4/2024 3:04 AM
     */
    @Override
    public ExecuteCodeResponse execute(ExecuteCodeRequest executeCodeRequest) {
        return null;
    }
}
