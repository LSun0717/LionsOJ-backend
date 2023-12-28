package com.gzu.lionsoj.model.dto.submission;

import lombok.Data;

/**
 * @Classname: JudgeInfo
 * @Description: 评测结果信息
 * @Author: lions
 * @Datetime: 12/28/2023 10:51 PM
 */
@Data
public class JudgeInfo {

    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 内存消耗（KB）
     */
    private long memory;

    /**
     * 时间消耗（ms）
     */
    private long time;
}
