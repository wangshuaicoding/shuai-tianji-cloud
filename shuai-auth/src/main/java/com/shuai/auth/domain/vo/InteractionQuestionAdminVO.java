package com.shuai.auth.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InteractionQuestionAdminVO {

    @ApiModelProperty(value = "主键，互动问题的id")
    private Long id;

    @ApiModelProperty(value = "互动问题的标题")
    private String title;

    @ApiModelProperty(value = "问题的描述")
    private String description;

    @ApiModelProperty(value = "问题下的回答数量")
    private Integer answerTimes;

    @ApiModelProperty(value = "是否隐藏，默认false")
    private Boolean hidden;

    @ApiModelProperty("管理端问题状态")
    private Integer status;

    @ApiModelProperty(value = "提问时间")
    private LocalDateTime createTime;

    // 用户表中
    @ApiModelProperty(value = "提问者昵称")
    private String userName;

    // 关于课程表中的内容
    @ApiModelProperty(value = "章名称")
    private String chapterName;

    @ApiModelProperty(value = "小节名称")
    private String sectionName;

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "分类名称1/分类名称2/分类名称3")
    private String categoryName;

    @ApiModelProperty(value = "老师名称")
    private String teacherName;

}
