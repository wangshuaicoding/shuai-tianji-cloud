package com.shuai.auth.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PointsRecordVO {

    @ApiModelProperty(value = "积分类型描述", example = "每日签到")
    private String type;
    @ApiModelProperty(value = "今日签到获取的积分", example = "5")
    private Integer points;
    @ApiModelProperty(value = "每日积分上限")
    private Integer maxPoints;
}
