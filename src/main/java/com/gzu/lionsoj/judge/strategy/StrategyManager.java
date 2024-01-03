package com.gzu.lionsoj.judge.strategy;

import com.gzu.lionsoj.judge.strategy.impl.DefaultJudgeStrategy;
import com.gzu.lionsoj.judge.strategy.impl.JavaLanguageJudgeStrategy;
import com.gzu.lionsoj.model.dto.submission.JudgeInfo;
import com.gzu.lionsoj.model.entity.Question;
import com.gzu.lionsoj.model.entity.Submission;
import org.springframework.stereotype.Component;

/**
 * @ClassName: StrategyManager
 * @Description: 策略管理器
 * @Author: Lions
 * @Datetime: 1/4/2024 4:10 AM
 */
@Component
public class StrategyManager {

    /**
     * @Description: StrategyManager 决定采用哪个策略
     * @param judgeContext 判题上下文
     * @Return: 判题信息
     * @Author: lions
     * @Datetime: 1/4/2024 4:12 AM
     */
    public JudgeInfo decide(JudgeContext judgeContext) {
        Question question = judgeContext.getQuestion();
        Submission submission = judgeContext.getSubmission();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        String language = submission.getLanguage();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
