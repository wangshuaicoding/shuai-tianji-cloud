package com.shuai.api.dto.course;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseSimpleInfoDTO {

    @ApiModelProperty(value = "课程名称")
    private String name;

    @ApiModelProperty(value = "封面链接")
    private String coverUrl;

    @ApiModelProperty(value = "售卖方式0付费，1：免费")
    private Integer free;

    @ApiModelProperty(value = "课程价格，单位为分")
    private Integer price;

    @ApiModelProperty(value = "课程有效期，单位月")
    private Integer validDuration;
}
