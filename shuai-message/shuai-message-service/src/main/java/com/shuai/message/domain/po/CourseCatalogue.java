package com.shuai.message.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 目录草稿
 * </p>
 *
 * @author Shuai
 * @since 2024-11-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("course_catalogue")
@ApiModel(value="CourseCatalogue对象", description="目录草稿")
public class CourseCatalogue implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程目录id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "目录名称")
    private String name;

    @ApiModelProperty(value = "是否支持试看")
    private Integer trailer;

    @ApiModelProperty(value = "课程id")
    private Long courseId;

    @ApiModelProperty(value = "目录类型1：章，2：节，3：测试")
    private Integer type;

    @ApiModelProperty(value = "所属章id，只有小节和测试有该值，章没有，章默认为0")
    private Long parentCatalogueId;

    @ApiModelProperty(value = "媒资id")
    private Long mediaId;

    @ApiModelProperty(value = "视频id")
    private Long videoId;

    @ApiModelProperty(value = "视频名称")
    private String videoName;

    @ApiModelProperty(value = "直播开始时间")
    private LocalDateTime livingStartTime;

    @ApiModelProperty(value = "直播结束时间")
    private LocalDateTime livingEndTime;

    @ApiModelProperty(value = "是否支持回放")
    private Integer playBack;

    @ApiModelProperty(value = "视频时长，以秒为单位")
    private Integer mediaDuration;

    @ApiModelProperty(value = "用于章节排序")
    private Integer cIndex;

    @ApiModelProperty(value = "部门id")
    private Long depId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    private Long creater;

    @ApiModelProperty(value = "更新人")
    private Long updater;

    @ApiModelProperty(value = "逻辑删除")
    private Integer deleted;
}
