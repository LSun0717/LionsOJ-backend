package com.gzu.lionsoj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gzu.lionsoj.common.BaseResponse;
import com.gzu.lionsoj.common.ErrorCode;
import com.gzu.lionsoj.common.ResultUtils;
import com.gzu.lionsoj.exception.BusinessException;
import com.gzu.lionsoj.model.dto.submission.SubmissionAddRequest;
import com.gzu.lionsoj.model.dto.submission.SubmissionQueryRequest;
import com.gzu.lionsoj.model.entity.Submission;
import com.gzu.lionsoj.model.entity.User;
import com.gzu.lionsoj.model.vo.SubmissionVO;
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
 * @Classname: SubmissionController
 * @Description: 答题提交接口
 * @Author: lions
 * @Datetime: 12/28/2023 8:52 PM
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
     * @Description: 提交答题信息
     * @param submissionAddRequest 请求体封装
     * @param request http请求
     * @Return: 本次答题提交的id
     * @Author: lions
     * @Datetime: 12/28/2023 8:58 PM
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

    /**
     * @Description: 答题信息分页查询
     * @param submissionQueryRequest 请求封装
     * @param request http请求封装
     * @Return: 答题信息VO分页
     * @Author: lions
     * @Datetime: 12/28/2023 8:57 PM
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<SubmissionVO>> listSubmissionByPage(@RequestBody SubmissionQueryRequest submissionQueryRequest,
                                                                 HttpServletRequest request) {
        long current = submissionQueryRequest.getCurrent();
        long pageSize = submissionQueryRequest.getPageSize();
        // 拼装查询参数
        QueryWrapper<Submission> queryWrapper = submissionService.getQueryWrapper(submissionQueryRequest);
        // POJO分页查询
        Page<Submission> submissionPage = submissionService.page(new Page<>(current, pageSize), queryWrapper);
        // POJO to VO Page
        final User loginUser = userService.getLoginUser(request);
        Page<SubmissionVO> submissionVOPage = submissionService.getSubmissionVOPage(submissionPage, loginUser);
        return ResultUtils.success(submissionVOPage);
    }

}
