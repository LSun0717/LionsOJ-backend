package com.gzu.lionsoj.judge.strategy.impl;

import cn.hutool.json.JSONUtil;
import com.gzu.lionsoj.judge.strategy.JudgeContext;
import com.gzu.lionsoj.judge.strategy.JudgeStrategy;
import com.gzu.lionsoj.model.dto.question.JudgeCase;
import com.gzu.lionsoj.model.dto.question.JudgeConfig;
import com.gzu.lionsoj.model.dto.submission.JudgeInfo;
import com.gzu.lionsoj.model.entity.Question;
import com.gzu.lionsoj.model.enums.JudgeInfoMessageEnum;

import java.util.List;

/**
 * @ClassName: DefaultJudgeStrategy
 * @Description: 默认判题策略实现
 * @Author: Lions
 * @Datetime: 1/4/2024 3:34 AM
 */
public class DefaultJudgeStrategy implements JudgeStrategy {
    /**
     * @param judgeContext 判题上下文
     * @Description: 执行判题
     * @Return: 判题响应信息
     * @Author: lions
     * @Datetime: 1/4/2024 3:33 AM
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        Question question = judgeContext.getQuestion();
        List<JudgeCase> judgeCases = judgeContext.getJudgeCaseList();

        long memoryConsume = judgeInfo.getMemoryConsume();
        long timeConsume = judgeInfo.getTimeConsume();

        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setMemoryConsume(memoryConsume);
        judgeInfoResponse.setTimeConsume(timeConsume);

        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        // 判断输出数量是否与测试用例输出相等
        if (outputList.size() != inputList.size()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        // 依次判断答案是否正确
        for (int i = 0; i < judgeCases.size(); i++) {
            String judgeCaseOutput = judgeCases.get(i).getOutput();
            String judgeInfoOutput = outputList.get(i);
            if (!judgeCaseOutput.equals(judgeInfoOutput)) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfoResponse;
            }
        }
        // 判断是否满足题目系统资源消耗限制
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        long memoryLimit = judgeConfig.getMemoryLimit();
        long timeLimit = judgeConfig.getTimeLimit();
        if (memoryConsume > memoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        if (timeConsume > timeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }

        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        return judgeInfoResponse;
    }
}
