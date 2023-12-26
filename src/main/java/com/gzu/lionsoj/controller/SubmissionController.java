package com.gzu.lionsoj.controller;

import com.gzu.lionsoj.common.BaseResponse;
import com.gzu.lionsoj.common.ErrorCode;
import com.gzu.lionsoj.common.ResultUtils;
import com.gzu.lionsoj.exception.BusinessException;
import com.gzu.lionsoj.model.dto.submission.SubmissionAddRequest;
import com.gzu.lionsoj.model.entity.User;
import com.gzu.lionsoj.service.SubmissionService;
import com.gzu.lionsoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 答题信息提交接口
 *
 */
@RestController
@RequestMapping("/submission")
@Slf4j
public class SubmissionController {

    @Resource
    private SubmissionService submissionService;

    @Resource
    private UserService userService;

    /**
     * 提交答题信息
     *
     * @param submissionAddRequest
     * @param request
     * @return submissionId 本次答题提交的id
     */
    @PostMapping("/")
    public BaseResponse<Long> doSubmit(@RequestBody SubmissionAddRequest submissionAddRequest,
            HttpServletRequest request) {
        if (submissionAddRequest == null || submissionAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能提交
        final User loginUser = userService.getLoginUser(request);
        Long submissionId = submissionService.doSubmit(submissionAddRequest, loginUser);
        return ResultUtils.success(submissionId);
    }

}
