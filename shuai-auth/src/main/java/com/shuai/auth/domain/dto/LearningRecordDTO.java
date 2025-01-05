package com.shuai.auth.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LearningRecordDTO {

    @ApiModelProperty(value = "对应小节的id")
    private Long sectionId;

    @ApiModelProperty(value = "视频的当前观看时间点，单位秒")
    private Integer moment;

    @ApiModelProperty(value = "是否完成学习，默认false")
    private Boolean finished;
}
