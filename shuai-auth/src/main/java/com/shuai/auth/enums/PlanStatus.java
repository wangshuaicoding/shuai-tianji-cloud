package com.shuai.auth.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.shuai.common.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum PlanStatus implements BaseEnum {
    NO_PLAN(0, "没有计划"),
    PLANS_ARE_UNDERWAY(1,"计划进行中"),
    ;

    @JsonValue
    @EnumValue
    int value;
    String desc;

    PlanStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
