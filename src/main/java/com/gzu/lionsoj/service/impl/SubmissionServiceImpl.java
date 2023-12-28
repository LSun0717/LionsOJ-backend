package com.gzu.lionsoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzu.lionsoj.common.ErrorCode;
import com.gzu.lionsoj.constant.CommonConstant;
import com.gzu.lionsoj.exception.BusinessException;
import com.gzu.lionsoj.mapper.SubmissionMapper;
import com.gzu.lionsoj.model.dto.submission.SubmissionAddRequest;
import com.gzu.lionsoj.model.dto.submission.SubmissionQueryRequest;
import com.gzu.lionsoj.model.entity.Question;
import com.gzu.lionsoj.model.entity.Submission;
import com.gzu.lionsoj.model.entity.User;
import com.gzu.lionsoj.model.enums.SubmissionStatusEnum;
import com.gzu.lionsoj.model.enums.SystemLanguageEnum;
import com.gzu.lionsoj.model.vo.SubmissionVO;
import com.gzu.lionsoj.service.QuestionService;
import com.gzu.lionsoj.service.SubmissionService;
import com.gzu.lionsoj.service.UserService;
import com.gzu.lionsoj.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    private UserService userService;

    /**
     * @Description: 提交题目实现
     * @param submissionAddRequest 题目提交请求体封装
     * @param loginUser 当前登录用户
     * @Return: 提交信息id
     * @Author: lions
     * @Datetime: 12/28/2023 9:18 PM
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

    /**
     * @Description: 封装查询条件
     * @param submissionQueryRequest 提交信息查询请求体封装
     * @Return: QueryWrapper
     * @Author: lions
     * @Datetime: 12/28/2023 9:19 PM
     */
    @Override
    public QueryWrapper<Submission> getQueryWrapper(SubmissionQueryRequest submissionQueryRequest) {
        QueryWrapper<Submission> queryWrapper = new QueryWrapper<>();
        if (submissionQueryRequest == null) {
            return queryWrapper;
        }
        // 获取查询请求信息
        String language = submissionQueryRequest.getLanguage();
        Integer status = submissionQueryRequest.getStatus();
        Long questionId = submissionQueryRequest.getQuestionId();
        Long userId = submissionQueryRequest.getUserId();
        String sortField = submissionQueryRequest.getSortField();
        String sortOrder = submissionQueryRequest.getSortOrder();
        // 拼装查询条件
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(SubmissionStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);

        return queryWrapper;
    }

    /**
     * @Description: 获取SubmissionVO并且脱敏
     * @param submission 用户题目提交信息
     * @param loginUser 当前登录用户
     * @Return: 脱敏后SubmissionVO
     * @Author: lions
     * @Datetime: 12/28/2023 9:15 PM
     */
    @Override
    public SubmissionVO getSubmissionVO(Submission submission, User loginUser) {
        SubmissionVO submissionVO = SubmissionVO.objToVo(submission);
        long userId = loginUser.getId();
        // 判断是否是本人查看以及校验权限
        if (userId != submission.getUserId() && !userService.isAdmin(loginUser)) {
            submissionVO.setCode(null);
        }
        return submissionVO;
    }

    /**
     * @Description: 获取SubmissionVO Page
     * @param submissionPage 提交信息分页
     * @param loginUser 当前登录用户
     * @Return: SubmissionVO分页
     * @Author: lions
     * @Datetime: 12/28/2023 9:22 PM
     */
    @Override
    public Page<SubmissionVO> getSubmissionVOPage(Page<Submission> submissionPage, User loginUser) {
        List<Submission> submissionList = submissionPage.getRecords();
        long current = submissionPage.getCurrent();
        long size = submissionPage.getSize();
        long total = submissionPage.getTotal();
        Page<SubmissionVO> submissionVOPage = new Page<>(current, size, total);
        if (submissionList.isEmpty()) {
            return submissionVOPage;
        }
        // 关联查询用户信息
        List<SubmissionVO> submissionVOList = submissionList.stream()
                .map(submission -> getSubmissionVO(submission, loginUser))
                .collect(Collectors.toList());
        submissionVOPage.setRecords(submissionVOList);

        return submissionVOPage;
    }
}




