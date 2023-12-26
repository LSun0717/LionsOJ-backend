package com.gzu.lionsoj.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 *
 */
@Data
public class QuestionAddRequest implements Serializable {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 题目解析
     */
    private String answerBlog;

    /**
     * 测试用例（json数组）
     */
    private List<JudgeCase> judgeCase;

    /**
     * 测试配置（json数组）
     */
    private List<JudgeConfig> judgeConfig;

    private static final long serialVersionUID = 1L;
}