package com.shuai.auth.mq;

import cn.hutool.core.collection.CollectionUtil;
import com.shuai.api.dto.trade.OrderBasicDTO;
import com.shuai.auth.service.ILearningLessonService;
import com.shuai.common.constants.MqConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class LessonChangeListener {

    private final ILearningLessonService learningLessonService;

    /**
     * 监听订单支付或课程报名的消息
     *
     * @param orderBasicDTO: 订单信息
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "learning.lesson.pay.queue", durable = "true"),
            exchange = @Exchange(name = MqConstants.Exchange.ORDER_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = MqConstants.Key.ORDER_PAY_KEY
    ))
    public void listenLessonPay(OrderBasicDTO orderBasicDTO) {
        // 1、健壮性处理
        if (orderBasicDTO == null || orderBasicDTO.getUserId() == null || CollectionUtil.isEmpty(orderBasicDTO.getCourseIds())) {
            log.error("接收到MQ消息有误，订单数据为空");
            return;
        }
        // 2、添加课程
        log.info("监听到用户{}的订单{}，需要添加课程{}到课表中", orderBasicDTO.getUserId(), orderBasicDTO.getOrderId(), orderBasicDTO.getCourseIds());
        learningLessonService.addUserLessons(orderBasicDTO.getUserId(), orderBasicDTO.getCourseIds());
    }
}
