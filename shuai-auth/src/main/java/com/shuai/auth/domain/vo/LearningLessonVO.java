package com.shuai.auth.domain.vo;

import com.shuai.auth.enums.LessonStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LearningLessonVO {

    @ApiModelProperty(value = "课程id")
    private Long courseId;

    @ApiModelProperty(value = "课程状态，0-未学习，1-学习中，2-已学完，3-已失效")
    private LessonStatus status;

    @ApiModelProperty(value = "每周学习频率，例如每周学习6小节，则频率为6")
    private Integer weekFreq;

    @ApiModelProperty(value = "已学习小节数量")
    private Integer learnedSections;

    @ApiModelProperty(value = "最近一次学习的小节id")
    private Long latestSectionId;

    @ApiModelProperty(value = "课表的创建时间")
    private LocalDateTime createTime;

    // course表中数据
    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "封面链接")
    private String coverUrl;

    @ApiModelProperty(value = "课程总节数，包括练习")
    private Integer sectionNum;

    @ApiModelProperty(value = "课程数量")
    private Integer courseAmount;

    // 根据小节id获取小结名称
    @ApiModelProperty(value = "最后一次学习的小节名")
    private String LatestSectionName;

    @ApiModelProperty(value = "最后一次学习的小节索引")
    private Integer LatestSectionIndex;

}
