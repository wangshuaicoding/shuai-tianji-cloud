package com.shuai.auth.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PointsBoardItemVO {

    @ApiModelProperty("我的榜单排名")
    private Integer rank;

    @ApiModelProperty("我的积分值")
    private Integer points;

    @ApiModelProperty("姓名")
    private String name;
}
