package com.gzu.lionsoj.model.dto.question;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Classname: QuestionEditRequest
 * @Description: 编辑请求封装
 * @Author: lions
 * @Datetime: 12/28/2023 10:54 PM
 */
@Data
public class QuestionEditRequest implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）
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
    private JudgeConfig judgeConfig;

    private static final long serialVersionUID = 1L;
}