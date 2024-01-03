package com.gzu.lionsoj.judge.sandbox.impl;

import com.gzu.lionsoj.judge.sandbox.CodeSandbox;
import com.gzu.lionsoj.judge.sandbox.model.ExecuteCodeRequest;
import com.gzu.lionsoj.judge.sandbox.model.ExecuteCodeResponse;
import com.gzu.lionsoj.model.dto.submission.JudgeInfo;
import com.gzu.lionsoj.model.enums.JudgeInfoMessageEnum;
import com.gzu.lionsoj.model.enums.SubmissionStatusEnum;

import java.util.List;

/**
 * @ClassName: ExampleCodeSandbox
 * @Description: 示例代码沙箱
 * @Author: Lions
 * @Datetime: 1/4/2024 3:09 AM
 */
public class ExampleCodeSandbox implements CodeSandbox {
    /**
     * @param executeCodeRequest 判题请求封装
     * @Description: 执行判题
     * @Return: 判题响应封装
     * @Author: lions
     * @Datetime: 1/4/2024 3:04 AM
     */
    @Override
    public ExecuteCodeResponse execute(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();

        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功了");
        executeCodeResponse.setStatus(SubmissionStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getValue());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
