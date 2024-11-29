package com.shuai.user.service;

import com.shuai.user.domain.po.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shuai.user.domain.vo.trade.PlaceOrderResultVO;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author Shuai
 * @since 2024-11-29
 */
public interface IOrderService extends IService<Order> {

    PlaceOrderResultVO enrolledFreeCourse(Long courseId);
}
