package com.shuai.message.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 课程分类
 * </p>
 *
 * @author Shuai
 * @since 2024-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("category")
@ApiModel(value="Category对象", description="课程分类")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程分类id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "父分类id，一级分类父id为0")
    private Long parentId;

    @ApiModelProperty(value = "分类级别，1,2,3：代表一级分类，二级分类，三级分类")
    private Integer level;

    @ApiModelProperty(value = "同级目录优先级，数字越小优先级越高，可以重复")
    private Integer priority;

    @ApiModelProperty(value = "课程分类状态，1：正常，2：禁用")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建者")
    private Long creater;

    @ApiModelProperty(value = "更新者")
    private Long updater;

    private Integer deleted;


}
