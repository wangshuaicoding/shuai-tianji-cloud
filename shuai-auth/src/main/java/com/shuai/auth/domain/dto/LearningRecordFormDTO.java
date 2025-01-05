package com.shuai.auth.domain.dto;

import com.shuai.common.validate.annotations.EnumValid;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "学习记录")
public class LearningRecordFormDTO {

    @ApiModelProperty(value = "课表id")
    @NotNull(message = "课表id不能为空")
    private Long lessonId;

    @ApiModelProperty(value = "小节id")
    @NotNull(message = "小节id不能为空")
    private Long sectionId;

    @ApiModelProperty(value = "小节类型,1-视频，2-考试")
    @NotNull(message = "小节类型不能为空")
    @EnumValid(enumeration = {1,2}, message = "小节类型错误,只能是：1-视频，2-考试")
    private Integer sectionType;

    @ApiModelProperty(value = "提交时间")
    private LocalDateTime commitTime;

    @ApiModelProperty(value = "学习时长，单位秒")
    private Integer duration;

    @ApiModelProperty(value = "学习进度，单位秒")
    private Integer moment;
}
