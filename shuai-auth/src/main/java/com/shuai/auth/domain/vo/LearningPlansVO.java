package com.shuai.auth.domain.vo;

import com.shuai.auth.domain.dto.LearningPlansFormDTO;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.List;

@Data
public class LearningPlansVO {

    @ApiModelProperty(value = "本周学习积分")
    private Integer weekPoints;

    @ApiModelProperty(value = "本周已学完小节数量")
    private Integer weekFinished;

    @ApiModelProperty(value = "本周计划学习小结数量")
    private Integer weekTotalPlan;

    private List<LearningPlansFormDTO> list;

}
