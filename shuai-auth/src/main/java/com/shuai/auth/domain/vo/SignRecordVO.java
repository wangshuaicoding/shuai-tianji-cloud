package com.shuai.auth.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SignRecordVO {

    @ApiModelProperty("连续签到的天数")
    private Integer signDays;

    @ApiModelProperty("今日签到获取的积分")
    private Integer signPoints = 1;

    @ApiModelProperty("连续签到奖励积分，连续签到超过7天以上才有奖励")
    private Integer rewardPoints;
}
