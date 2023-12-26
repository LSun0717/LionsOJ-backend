package com.gzu.lionsoj.model.dto.submission;

import lombok.Data;

/**
 * 评测结果信息
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
