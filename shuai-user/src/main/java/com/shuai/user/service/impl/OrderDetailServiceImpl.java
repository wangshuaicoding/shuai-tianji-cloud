package com.shuai.user.service.impl;

import com.shuai.user.domain.po.OrderDetail;
import com.shuai.user.mapper.OrderDetailMapper;
import com.shuai.user.service.IOrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单明细 服务实现类
 * </p>
 *
 * @author Shuai
 * @since 2024-11-29
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {

}
