package com.shuai.user.controller;


import com.shuai.user.domain.dto.CouponFormDTO;
import com.shuai.user.service.ICouponService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 优惠券的规则信息 前端控制器
 * </p>
 *
 * @author Shuai
 * @since 2025-02-09
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {

    private final ICouponService couponService;

    @ApiOperation("新增优惠劵接口")
    @PostMapping
    public void saveCoupon(@RequestBody @Valid CouponFormDTO formDTO) {
        couponService.saveCoupon(formDTO);
    }
}
