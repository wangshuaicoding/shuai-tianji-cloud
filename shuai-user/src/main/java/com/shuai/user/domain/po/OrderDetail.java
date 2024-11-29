package com.shuai.user.domain.po;

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
 * 订单明细
 * </p>
 *
 * @author Shuai
 * @since 2024-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order_detail")
@ApiModel(value="OrderDetail对象", description="订单明细")
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单明细id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "课程id")
    private Long courseId;

    @ApiModelProperty(value = "课程价格")
    private Integer price;

    @ApiModelProperty(value = "课程名称")
    private String name;

    @ApiModelProperty(value = "封面地址")
    private String coverUrl;

    @ApiModelProperty(value = "课程学习有效期，单位月。空则代表永久有效")
    private Integer validDuration;

    @ApiModelProperty(value = "课程学习的过期时间，支付成功开始计时")
    private LocalDateTime courseExpireTime;

    @ApiModelProperty(value = "折扣金额")
    private Integer discountAmount;

    @ApiModelProperty(value = "实付金额")
    private Integer realPayAmount;

    @ApiModelProperty(value = "订单详情状态，1：待支付，2：已支付，3：已关闭，4：已完成，5：已报名")
    private Integer status;

    @ApiModelProperty(value = "1：待审批，2：取消退款，3：同意退款，4：拒绝退款，5：退款成功，6：退款失败'")
    private Integer refundStatus;

    @ApiModelProperty(value = "支付渠道名称")
    private String payChannel;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    private Long creater;

    @ApiModelProperty(value = "更新人")
    private Long updater;


}
