package com.shuai.auth.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class LearningPlansDTO {

    @ApiModelProperty(value = "课程的id")
    @Min(1)
    @NotNull
    private Long courseId;

    @ApiModelProperty(value = "每周学习次数")
    @NotNull
    @Range(min = 1, max = 20)
    private Integer weekFreq;
}
