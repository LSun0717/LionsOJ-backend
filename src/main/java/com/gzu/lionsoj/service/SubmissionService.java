package com.gzu.lionsoj.service;

import com.gzu.lionsoj.model.dto.submission.SubmissionAddRequest;
import com.gzu.lionsoj.model.entity.Submission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gzu.lionsoj.model.entity.User;

/**
* @author 孙龙
* @description 针对表【submission(题目提交表)】的数据库操作Service
* @createDate 2023-12-27 01:49:17
*/
public interface SubmissionService extends IService<Submission> {
    /**
     * 题目提交
     *
     * @param submissionAddRequest
     * @param loginUser
     * @return
     */
    long doSubmit(SubmissionAddRequest submissionAddRequest, User loginUser);

}
