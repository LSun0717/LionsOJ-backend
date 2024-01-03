package com.gzu.lionsoj.judge.strategy;

import com.gzu.lionsoj.model.dto.question.JudgeCase;
import com.gzu.lionsoj.model.dto.submission.JudgeInfo;
import com.gzu.lionsoj.model.entity.Question;
import com.gzu.lionsoj.model.entity.Submission;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: JudgeContext
 * @Description: 判题上下文
 * @Author: Lions
 * @Datetime: 1/4/2024 3:30 AM
 */
@Data
public class JudgeContext {

    /**
     * 判题输出信息
     */
    private JudgeInfo judgeInfo;

    /**
     * 用例输入
     */
    private List<String> inputList;

    /**
     * 执行输出
     */
    private List<String> outputList;

    /**
     * 题目信息
     */
    private Question question;

    /**
     * 测试用例列表
     */
    private List<JudgeCase> judgeCaseList;

    /**
     * 代码提交信息
     */
    private Submission submission;
}
