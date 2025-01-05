package com.shuai.auth.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.shuai.common.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum SectionTypeStatus implements BaseEnum {
    VIDEO(1, "视频"),
    EXAMINATION(2,"考试"),
    ;

    @JsonValue
    @EnumValue
    final
    int value;
    String desc;

    SectionTypeStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
