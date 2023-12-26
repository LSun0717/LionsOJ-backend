package com.gzu.lionsoj.model.dto.submission;

import lombok.Data;

import java.io.Serializable;

/**
 * 答题提交请求
 *
 */
@Data
public class SubmissionAddRequest implements Serializable {

    /**
     * 代码语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 题目 id
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;
}