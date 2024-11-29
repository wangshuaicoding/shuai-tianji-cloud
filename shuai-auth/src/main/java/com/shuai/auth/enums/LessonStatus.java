package com.shuai.auth.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.shuai.common.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum LessonStatus implements BaseEnum {
    NOT_STUDIED(0, "未学习"),
    LEARNING(1,"学习中"),
    COMPLETED(2,"已学完"),
    EXPIRED(3,"已失效"),
    ;

    @JsonValue
    @EnumValue
    int value;
    String desc;

    LessonStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
