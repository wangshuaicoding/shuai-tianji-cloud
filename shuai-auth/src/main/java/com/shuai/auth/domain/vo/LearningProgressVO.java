package com.shuai.auth.domain.vo;

import com.shuai.auth.domain.dto.LearningRecordDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class LearningProgressVO {

    @ApiModelProperty(value = "课表id")
    private Long id;

    @ApiModelProperty(value = "最近学习的小节id")
    private Long latestSectionId;

    @ApiModelProperty(value = "最近学习的记录")
    private List<LearningRecordDTO> records;
}
