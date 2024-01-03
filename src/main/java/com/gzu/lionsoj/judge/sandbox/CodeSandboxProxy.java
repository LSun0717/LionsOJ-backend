package com.gzu.lionsoj.judge.sandbox;

import com.gzu.lionsoj.judge.sandbox.model.ExecuteCodeRequest;
import com.gzu.lionsoj.judge.sandbox.model.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: CodeSandboxProxy
 * @Description: 代码沙箱功能增强（静态代理）
 * @Author: Lions
 * @Datetime: 1/4/2024 3:06 AM
 */
@Slf4j
public class CodeSandboxProxy implements CodeSandbox {

    private final CodeSandbox codeSandbox;

    public CodeSandboxProxy(CodeSandbox codeSandbox) {
        this.codeSandbox = codeSandbox;
    }

    /**
     * @Description: 代理增强方法
     * @param executeCodeRequest 代码执行请求
     * @Return: 代码执行响应
     * @Author: lions
     * @Datetime: 1/4/2024 3:07 AM
     */
    @Override
    public ExecuteCodeResponse execute(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息:{}", executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandbox.execute(executeCodeRequest);
        log.info("代码沙箱响应信息:{}", executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
