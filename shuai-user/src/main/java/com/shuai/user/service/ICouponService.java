package com.shuai.user.service;

import com.shuai.user.domain.dto.CouponFormDTO;
import com.shuai.user.domain.po.Coupon;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 优惠券的规则信息 服务类
 * </p>
 *
 * @author Shuai
 * @since 2025-02-09
 */
public interface ICouponService extends IService<Coupon> {

    void saveCoupon(CouponFormDTO formDTO);
}
