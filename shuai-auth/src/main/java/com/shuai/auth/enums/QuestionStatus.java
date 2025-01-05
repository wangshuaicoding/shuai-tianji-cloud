package com.shuai.auth.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.shuai.common.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum QuestionStatus implements BaseEnum {
    NOT_VIEWED(0, "未查看"),
    VIEWED(1,"已查看"),
    ;

    @JsonValue
    @EnumValue
    private final int value;
    private final String desc;

    QuestionStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
