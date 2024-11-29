package com.shuai.user.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.shuai.common.domain.po.BaseEntity;
import com.shuai.user.enums.OrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单
 * </p>
 *
 * @author Shuai
 * @since 2024-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Order对象", description="订单")
// order是mysql中的关键字，需要用``引注
@TableName(value = "`order`")
public class Order extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "交易流水支付单号")
    private Long payOrderNo;

    @ApiModelProperty(value = "订单状态，1：待支付，2：已支付，3：已关闭，4：已完成，5：已报名，6：已申请退款")
    private Integer status;

    @ApiModelProperty(value = "状态备注")
    private String message;

    @ApiModelProperty(value = "订单总金额，单位分")
    private Integer totalAmount;

    @ApiModelProperty(value = "实付金额，单位分")
    private Integer realAmount;

    @ApiModelProperty(value = "优惠金额，单位分")
    private Integer discountAmount;

    @ApiModelProperty(value = "支付渠道")
    private String payChannel;

    @ApiModelProperty(value = "优惠券id")
    private String couponIds;

    @ApiModelProperty(value = "支付时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "订单关闭时间")
    private LocalDateTime closeTime;

    @ApiModelProperty(value = "订单完成时间，支付后30天")
    private LocalDateTime finishTime;

    @ApiModelProperty(value = "申请退款时间")
    private LocalDateTime refundTime;

}
