package com.shuai.auth.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuestionsFormDTO {

    @ApiModelProperty(value = "互动问题的标题")
    private String title;

    @ApiModelProperty(value = "问题描述信息")
    private String description;

    @ApiModelProperty(value = "所属课程id")
    private Long courseId;

    @ApiModelProperty(value = "所属课程章id")
    private Long chapterId;

    @ApiModelProperty(value = "所属课程节id")
    private Long sectionId;

    @ApiModelProperty(value = "是否匿名，默认false")
    private Boolean anonymity;
}
