package com.shuai.api.dto.course;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CataSimpleInfoDTO {
    @ApiModelProperty(value = "课程目录id")
    private Long id;

    @ApiModelProperty(value = "目录名称")
    private String name;

    @ApiModelProperty(value = "用于章节排序")
    private Integer cIndex;


}
