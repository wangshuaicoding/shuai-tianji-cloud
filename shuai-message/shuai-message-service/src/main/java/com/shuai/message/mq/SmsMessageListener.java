package com.shuai.message.mq;

import com.shuai.common.constants.MqConstants;
import com.shuai.message.domain.dto.SmsInfoDTO;
import com.shuai.message.service.ISmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmsMessageListener {

    private final ISmsService smsService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "sms.message.queue", durable = "true"),
            exchange = @Exchange(MqConstants.Exchange.SMS_EXCHANGE),
            key = MqConstants.Key.SMS_MESSAGE
    ))
    public void listenSmsMessage(SmsInfoDTO smsInfoDTO){
        smsService.sendMessage(smsInfoDTO);
    }
}
