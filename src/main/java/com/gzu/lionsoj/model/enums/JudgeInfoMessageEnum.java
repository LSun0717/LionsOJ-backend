package com.gzu.lionsoj.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname: JudgeInfoMessageEnum
 * @Description: TODO 编写运行结果枚举类，WA/OOM之类的
 * @Author: lions
 * @Datetime: 12/28/2023 10:56 PM
 */
public enum JudgeInfoMessageEnum {

    WAITING("等待", "waiting");

    private final String text;

    private final String value;

    JudgeInfoMessageEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * @Description: 获取值列表
     * @Return: 值列表
     * @Author: lions
     * @Datetime: 12/28/2023 10:56 PM
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * @Description: 根据 value 获取枚举
     * @param value 枚举值
     * @Return: 判题信息枚举
     * @Author: lions
     * @Datetime: 12/28/2023 10:58 PM
     */
    public static JudgeInfoMessageEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (JudgeInfoMessageEnum anEnum : JudgeInfoMessageEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
