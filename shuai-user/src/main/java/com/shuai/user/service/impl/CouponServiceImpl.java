package com.shuai.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.shuai.common.exceptions.BadRequestException;
import com.shuai.common.utils.CollUtils;
import com.shuai.user.domain.dto.CouponFormDTO;
import com.shuai.user.domain.po.Coupon;
import com.shuai.user.domain.po.CouponScope;
import com.shuai.user.mapper.CouponMapper;
import com.shuai.user.service.ICouponScopeService;
import com.shuai.user.service.ICouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 优惠券的规则信息 服务实现类
 * </p>
 *
 * @author Shuai
 * @since 2025-02-09
 */
@Service
@RequiredArgsConstructor
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements ICouponService {

    private final ICouponScopeService couponScopeService;

    @Override
    @Transactional
    public void saveCoupon(CouponFormDTO formDTO) {
        Coupon coupon = BeanUtil.copyProperties(formDTO, Coupon.class);
        save(coupon);

        // 没有限制范围直接返回
        if (!formDTO.getSpecific()) {
            return;
        }

        Long couponId = coupon.getId();
        List<Long> scopes = formDTO.getScopes();
        if (CollUtils.isEmpty(scopes)) {
            throw new BadRequestException("限制返回不能为空");
        }

        // 转换为po
        List<CouponScope> couponScopeList = scopes.stream()
                .map(bizId -> new CouponScope().setBizId(bizId).setCouponId(couponId))
                .collect(Collectors.toList());

        // 批量保存该优惠卷的使用范围
        couponScopeService.saveBatch(couponScopeList);
    }
}
