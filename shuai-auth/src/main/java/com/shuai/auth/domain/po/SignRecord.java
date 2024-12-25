package com.shuai.auth.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.Year;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 签到记录表
 * </p>
 *
 * @author Shuai
 * @since 2024-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sign_record")
@ApiModel(value="SignRecord对象", description="签到记录表")
public class SignRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "签到年份")
    private Year year;

    @ApiModelProperty(value = "签到月份")
    private Integer month;

    @ApiModelProperty(value = "签到日期")
    private LocalDate date;

    @ApiModelProperty(value = "是否补签")
    private Boolean isBackup;


}
