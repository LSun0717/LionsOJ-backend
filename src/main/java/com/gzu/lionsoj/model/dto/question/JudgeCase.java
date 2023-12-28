package com.gzu.lionsoj.model.dto.question;

import lombok.Data;

/**
 * 题目用例
 */
/**
 * @Classname: JudgeCase
 * @Description: 题目用例
 * @Author: lions
 * @Datetime: 12/28/2023 10:53 PM
 */
@Data
public class JudgeCase {

    /**
     * 输入用例
     */
    private String input;

    /**
     * 输出用例
     */
    private String output;
}
