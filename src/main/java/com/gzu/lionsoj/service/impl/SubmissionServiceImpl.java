package com.gzu.lionsoj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzu.lionsoj.common.ErrorCode;
import com.gzu.lionsoj.exception.BusinessException;
import com.gzu.lionsoj.mapper.SubmissionMapper;
import com.gzu.lionsoj.model.dto.submission.SubmissionAddRequest;
import com.gzu.lionsoj.model.entity.Question;
import com.gzu.lionsoj.model.entity.Submission;
import com.gzu.lionsoj.model.entity.User;
import com.gzu.lionsoj.model.enums.SubmissionStatusEnum;
import com.gzu.lionsoj.model.enums.SystemLanguageEnum;
import com.gzu.lionsoj.service.QuestionService;
import com.gzu.lionsoj.service.SubmissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 孙龙
* @description 针对表【submission(题目提交表)】的数据库操作Service实现
* @createDate 2023-12-27 01:49:17
*/
@Service
public class SubmissionServiceImpl extends ServiceImpl<SubmissionMapper, Submission>
    implements SubmissionService{

    @Resource
    private QuestionService questionService;

    /**
     * 提交题目
     *
     * @param submissionAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doSubmit(SubmissionAddRequest submissionAddRequest, User loginUser) {
        // 校验编程语言是否支持
        String language = submissionAddRequest.getLanguage();
        SystemLanguageEnum languageEnum = SystemLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "当前编程语言不支持");
        }
        // 判断实体是否存在，根据类别获取实体
        Long questionId = submissionAddRequest.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已提交题目
        long userId = loginUser.getId();
        // 每个用户串行提交题目
        Submission submission = new Submission();
        submission.setUserId(userId);
        submission.setQuestionId(questionId);
        submission.setCode(submissionAddRequest.getCode());
        submission.setLanguage(submissionAddRequest.getLanguage());
        // 置初始状态
        submission.setStatus(SubmissionStatusEnum.WAITING.getValue());
        submission.setJudgeInfo("{}");
        boolean save = this.save(submission);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "提交失败，数据插入失败");
        }
        return submission.getId();
    }
}




