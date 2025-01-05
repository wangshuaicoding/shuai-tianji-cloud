package com.shuai.auth.mq;

import com.shuai.api.dto.remark.LikedTimesDTO;
import com.shuai.auth.domain.po.InteractionReply;
import com.shuai.auth.service.IInteractionReplyService;
import com.shuai.common.constants.MqConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class LikeTimesChangeListener {

    private final IInteractionReplyService replyService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "qa.liked.times.queue", durable = "true"),
            exchange = @Exchange(name = MqConstants.Exchange.LIKE_RECORD_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = MqConstants.Key.QA_LIKED_TIMES_KEY
    ))
    public void listenLikeTimesChange(List<LikedTimesDTO> likedTimesDTOS) {
        log.debug("监听到回答或评论的点赞数变更");
        List<InteractionReply> list = new ArrayList<>(likedTimesDTOS.size());
        for (LikedTimesDTO likedTimesDTO : likedTimesDTOS) {
            InteractionReply reply = new InteractionReply();
            reply.setId(likedTimesDTO.getBizId());
            reply.setLikedTimes(likedTimesDTO.getLikedTimes());
            list.add(reply);
        }

        replyService.updateBatchById(list);
    }
}
