package com.shuai.user.domain.dto;

import com.shuai.common.validate.annotations.EnumValid;
import com.shuai.user.enums.DiscountType;
import com.shuai.user.enums.ObtainType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@ApiModel(description = "优惠券表单数据")
public class CouponFormDTO {

    @ApiModelProperty(value = "优惠券id，新增不需要添加，更新必填")
    private Long id;

    @ApiModelProperty(value = "优惠券名称，可以和活动名称保持一致")
    @NotNull(message = "优惠券名称不能为空")
    @Size(min = 4, max = 20, message = "优惠券长度错误")
    private String name;

    @ApiModelProperty(value = "优惠券作用范围")
    private List<Long> scopes;

    @ApiModelProperty(value = "折扣类型，1：满减，2：每满减，3：折扣，4：无门槛")
    @NotNull(message = "优惠券类型不能为空")
    @EnumValid(enumeration = {1, 2, 3, 4})
    private DiscountType discountType;

    @ApiModelProperty(value = "是否限定作用范围，false：不限定，true：限定。默认false")
    private Boolean specific;

    @ApiModelProperty(value = "折扣值，如果是满减则存满减金额，如果是折扣，则存折扣率，8折就是存80")
    private Integer discountValue;

    @ApiModelProperty(value = "使用门槛，0：表示无门槛，其他值：最低消费金额")
    private Integer thresholdAmount;

    @ApiModelProperty(value = "最高优惠金额，满减最大，0：表示没有限制，不为0，则表示该券有金额的限制")
    private Integer maxDiscountAmount;

    @ApiModelProperty(value = "获取方式：1：手动领取，2：兑换码")
    @NotNull(message = "获取方式不能为空")
    @EnumValid(enumeration = {1, 2}, message = "获取方式错误")
    private ObtainType obtainWay;

    @ApiModelProperty(value = "总数量，不超过5000")
    @Range(min = 1, max = 5000, message = "优惠券总量必须在1~5000")
    private Integer totalNum;

    @ApiModelProperty(value = "每个人限领的数量，默认1")
    @Range(min = 1, max = 10, message = "每人限领数量必须在1~10")
    private Integer userLimit;
}
