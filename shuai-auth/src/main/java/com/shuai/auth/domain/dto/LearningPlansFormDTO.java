package com.shuai.auth.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class LearningPlansFormDTO {

    @ApiModelProperty(value = "课程的id")
    private Long courseId;

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "本周学习的小节数量")
    private Integer weekLearnedSections;

    @ApiModelProperty(value = "本周计划学习数量")
    private Integer weekFreq;

    @ApiModelProperty(value = "总已学习小节数量")
    private Integer learnedSections;

    @ApiModelProperty(value = "总小节数量")
    private Integer sections;

    @ApiModelProperty(value = "最近一次学习时间")
    private LocalDateTime latestLearnTime;
}
