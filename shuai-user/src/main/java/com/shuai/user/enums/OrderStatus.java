package com.shuai.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.shuai.common.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStatus implements BaseEnum {

    WAIT_PAY(1, "待支付","下单"),
    PAID(2, "已支付","付款"),
    CLOSE(3, "已关闭","交易关闭"),
    FINISH(4, "已完成","交易完成"),
    ENROLLED(5, "已报名","免费报名"),
    REFUND(6, "申请退款","申请退款");

    @JsonValue // json序列化使用的值
    @EnumValue  // 与数据库交互使用的值
    private final int value;
    private final String desc;
    private final String progressName;
}
