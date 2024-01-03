package com.gzu.lionsoj.judge;

import com.gzu.lionsoj.model.entity.Submission;

/**
 * @ClassName: JudgeService
 * @Description: 判题服务
 * @Author: Lions
 * @Datetime: 1/4/2024 3:53 AM
 */
public interface JudgeService {

    /**
     * @Description: 执行判题
     * @param submissionId 代码提交id
     * @Return: 代码提交信息
     * @Author: lions
     * @Datetime: 1/4/2024 3:53 AM
     */
    Submission doJudge(long submissionId);
}
