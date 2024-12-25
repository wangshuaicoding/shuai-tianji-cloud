package com.shuai.auth.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.shuai.common.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum PointsRecordType implements BaseEnum {
    LEARNING(1, "课程学习", 50),
    SIGN(2, "每日签到", 0),
    QA(3, "课程问答", 20),
    NOTE(4, "课程笔记", 20),
    COMMENT(5, "课程评论", 0),
    ;

    @EnumValue
    @JsonValue
    int value;
    String desc;
    int maxPoints;

    PointsRecordType(Integer value, String desc, Integer maxPoints) {
        this.value = value;
        this.desc = desc;
        this.maxPoints = maxPoints;
    }

}
