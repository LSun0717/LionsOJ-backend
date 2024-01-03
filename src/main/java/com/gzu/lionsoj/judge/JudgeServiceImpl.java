package com.gzu.lionsoj.judge;

import cn.hutool.json.JSONUtil;
import com.gzu.lionsoj.common.ErrorCode;
import com.gzu.lionsoj.exception.BusinessException;
import com.gzu.lionsoj.judge.sandbox.CodeSandbox;
import com.gzu.lionsoj.judge.sandbox.CodeSandboxFactory;
import com.gzu.lionsoj.judge.sandbox.CodeSandboxProxy;
import com.gzu.lionsoj.judge.sandbox.model.ExecuteCodeRequest;
import com.gzu.lionsoj.judge.sandbox.model.ExecuteCodeResponse;
import com.gzu.lionsoj.judge.strategy.JudgeContext;
import com.gzu.lionsoj.judge.strategy.StrategyManager;
import com.gzu.lionsoj.model.dto.question.JudgeCase;
import com.gzu.lionsoj.model.dto.submission.JudgeInfo;
import com.gzu.lionsoj.model.entity.Question;
import com.gzu.lionsoj.model.entity.Submission;
import com.gzu.lionsoj.model.enums.SubmissionStatusEnum;
import com.gzu.lionsoj.service.QuestionService;
import com.gzu.lionsoj.service.SubmissionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: JudgeServiceImpl
 * @Description: 判题服务实现
 * @Author: Lions
 * @Datetime: 1/4/2024 3:54 AM
 */
@Service
public class JudgeServiceImpl implements JudgeService{

    @Resource
    private QuestionService questionService;

    @Resource
    @Lazy
    private SubmissionService submissionService;

    @Resource
    private StrategyManager strategyManager;

    @Value("${sandbox.type:example}")
    private String type;

    /**
     * @param submissionId 代码提交id
     * @Description: 执行判题
     * @Return: 代码提交信息
     * @Author: lions
     * @Datetime: 1/4/2024 3:53 AM
     */
    @Override
    public Submission doJudge(long submissionId) {
        // 传入题目的提交，找到对应的题目，提交信息
        Submission submission = submissionService.getById(submissionId);
        if (submission == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = submission.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        // 如果不为等待状态，则继续判题
        if (!submission.getStatus().equals(SubmissionStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }
        // 修改题目为判题中的状态
        Submission updateSubmission = new Submission();
        updateSubmission.setId(submissionId);
        updateSubmission.setStatus(SubmissionStatusEnum.WAITING.getValue());
        boolean updateSuccess = submissionService.updateById(updateSubmission);
        if (!updateSuccess) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        // 调用代码沙箱
        CodeSandbox codeSandBox = CodeSandboxFactory.newInstance(type);
        String language = submission.getLanguage();
        String code = submission.getCode();
        // 获取测试用例
        String judgeCaseJson = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseJson, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());

        // 调用沙箱执行
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        CodeSandboxProxy codeSandboxProxy = new CodeSandboxProxy(codeSandBox);
        ExecuteCodeResponse executeCodeResponse = codeSandboxProxy.execute(executeCodeRequest);

        // 封装判题上下文，交给StrategyManager决定采用哪种策略
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setSubmission(submission);
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(executeCodeResponse.getOutputList());
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);

        // 决定采用某种判题策略
        JudgeInfo judgeInfo = strategyManager.decide(judgeContext);

        // 利用同一updateSubmission对象
//        updateSubmission.setStatus(SubmissionStatusEnum.SUCCEED.getValue());
//        updateSubmission.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        Submission questionSubmitUpdate1 = new Submission();
        questionSubmitUpdate1.setId(submissionId);
        questionSubmitUpdate1.setStatus(SubmissionStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate1.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        boolean updateSuccess2 = submissionService.updateById(questionSubmitUpdate1);
//        boolean updateSuccess2 = submissionService.updateById(updateSubmission);
        if (!updateSuccess2) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }

        return submissionService.getById(submissionId);
    }
}
