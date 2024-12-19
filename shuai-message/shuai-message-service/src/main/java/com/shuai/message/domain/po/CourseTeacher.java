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
 * 课程老师关系表草稿
 * </p>
 *
 * @author Shuai
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("course_teacher")
@ApiModel(value="CourseTeacher对象", description="课程老师关系表草稿")
public class CourseTeacher implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程老师关系id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "课程id")
    private Long courseId;

    @ApiModelProperty(value = "老师id")
    private Long teacherId;

    @ApiModelProperty(value = "用户端是否展示")
    private Integer isShow;

    @ApiModelProperty(value = "序号")
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
