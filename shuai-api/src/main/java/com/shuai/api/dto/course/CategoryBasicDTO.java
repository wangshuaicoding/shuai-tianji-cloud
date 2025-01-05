package com.shuai.api.dto.course;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CategoryBasicDTO {

    @ApiModelProperty(value = "分类id")
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "父级分类id")
    private Long parentId;
}
