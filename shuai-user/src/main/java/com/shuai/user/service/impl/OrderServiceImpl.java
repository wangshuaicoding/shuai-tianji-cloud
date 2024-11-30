package com.shuai.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuai.api.client.course.CourseClient;
import com.shuai.api.dto.course.CourseSimpleInfoDTO;
import com.shuai.api.dto.trade.OrderBasicDTO;
import com.shuai.common.autoconfigure.mq.RabbitMqHelper;
import com.shuai.common.constants.MqConstants;
import com.shuai.common.exceptions.BizIllegalException;
import com.shuai.common.utils.UserContext;
import com.shuai.user.constants.trade.TradeErrorInfo;
import com.shuai.user.domain.po.Order;
import com.shuai.user.domain.po.OrderDetail;
import com.shuai.user.domain.vo.trade.PlaceOrderResultVO;
import com.shuai.user.enums.OrderStatus;
import com.shuai.user.mapper.OrderMapper;
import com.shuai.user.service.IOrderDetailService;
import com.shuai.user.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author Shuai
 * @since 2024-11-29
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    private final CourseClient courseClient;
    private final IOrderDetailService orderDetailService;
    private final RabbitMqHelper rabbitMqHelper;

    @Override
    @Transactional
    public PlaceOrderResultVO enrolledFreeCourse(Long courseId) {
        Long userId = 8888L;
        List<Long> cIds = Collections.singletonList(courseId);
        List<CourseSimpleInfoDTO> courseSimpleInfoList = courseClient.selectListByIds(cIds);
        if (CollectionUtil.isEmpty(courseSimpleInfoList)) {
            throw new BizIllegalException(TradeErrorInfo.COURSE_NOT_EXIST);
        }
        CourseSimpleInfoDTO courseSimpleInfo = courseSimpleInfoList.get(0);
        if (!Objects.equals(courseSimpleInfo.getFree(), 1)) {
            throw new BizIllegalException(TradeErrorInfo.COURSE_NOT_FREE);
        }

        // 构建订单信息
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.ENROLLED.getValue());
        order.setMessage(OrderStatus.ENROLLED.getProgressName());
        order.setTotalAmount(0);
        order.setRealAmount(0);
        order.setDiscountAmount(0);
        order.setFinishTime(LocalDateTime.now());

        // 设置订单id
        long orderId = IdWorker.getId(order);
        order.setId(orderId);
        // 保存订单信息
        save(order);

        // 构建订单详情信息
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderId);
        orderDetail.setUserId(userId);
        orderDetail.setCourseId(courseId);
        orderDetail.setPrice(courseSimpleInfo.getPrice());
        orderDetail.setName(courseSimpleInfo.getName());
        orderDetail.setCoverUrl(courseSimpleInfo.getCoverUrl());
        orderDetail.setValidDuration(courseSimpleInfo.getValidDuration());
        orderDetail.setDiscountAmount(0);
        orderDetail.setRealPayAmount(courseSimpleInfo.getPrice() - orderDetail.getDiscountAmount());
        orderDetail.setStatus(OrderStatus.ENROLLED.getValue());

        // 保存订单详情信息
        orderDetailService.save(orderDetail);

        // 发送MQ消息，通知报名成功
        rabbitMqHelper.send(
                MqConstants.Exchange.ORDER_EXCHANGE,
                MqConstants.Key.ORDER_PAY_KEY,
                OrderBasicDTO.builder()
                        .orderId(orderId)
                        .userId(userId)
                        .courseIds(cIds)
                        .finishTime(LocalDateTime.now())
                        .build()
        );
        // 返回vo
        return PlaceOrderResultVO.builder()
                .orderId(orderId)
                .payAmount(0)
                .status(OrderStatus.ENROLLED.getValue())
                .build();
    }
}
