package com.shuai.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuai.api.dto.remark.LikedTimesDTO;
import com.shuai.common.autoconfigure.mq.RabbitMqHelper;
import com.shuai.common.constants.MqConstants;
import com.shuai.common.exceptions.DbException;
import com.shuai.common.utils.StringUtils;
import com.shuai.common.utils.UserContext;
import com.shuai.user.constants.remark.LikedRecordConstants;
import com.shuai.user.domain.dto.LikedRecordFormDTO;
import com.shuai.user.domain.po.LikedRecord;
import com.shuai.user.mapper.LikedRecordMapper;
import com.shuai.user.service.ILikedRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 点赞记录表 服务实现类
 * </p>
 *
 * @author Shuai
 * @since 2024-12-20
 */
// @Service
@RequiredArgsConstructor
@Slf4j
public class LikedRecordServiceImpl extends ServiceImpl<LikedRecordMapper, LikedRecord> implements ILikedRecordService {

    private final RabbitMqHelper mqHelper;
    @Override
    public void likeOrUnlikeRecord(LikedRecordFormDTO formDTO) {
        boolean success = formDTO.getLiked() ? like(formDTO) : unlike(formDTO);
        if (!success) {
            log.info("点赞或取消点赞失败");
            return;
        }

        // 统计点赞业务id的点赞数
        Integer count = lambdaQuery()
                .eq(LikedRecord::getBizId, formDTO.getBizId())
                .count();

        // 发送MQ请求
        mqHelper.send(
                MqConstants.Exchange.LIKE_RECORD_EXCHANGE,
                StringUtils.format(MqConstants.Key.LIKED_TIMES_KEY_TEMPLATE,formDTO.getBizType()),
                LikedTimesDTO.of(formDTO.getBizId(), count)
        );
    }

    private boolean like(LikedRecordFormDTO formDTO) {
        Long userId = UserContext.getUser();
        LikedRecord one = lambdaQuery()
                .eq(LikedRecord::getUserId, userId)
                .eq(LikedRecord::getBizId, formDTO.getBizId())
                .one();
        if (one != null) {
            log.info("用户:{}已经点赞过该业务:{}", userId, formDTO.getBizId());
            return false;
        }

        LikedRecord likedRecord = new LikedRecord()
                .setUserId(userId)
                .setBizId(formDTO.getBizId())
                .setBizType(formDTO.getBizType().toString());

        boolean success = save(likedRecord);
        if (!success) {
            throw new DbException(LikedRecordConstants.FAILED_INSERT_LIKES_RECORD);
        }
        return true;
    }

    private boolean unlike(LikedRecordFormDTO formDTO) {
        Long userId = UserContext.getUser();
        boolean success = lambdaUpdate()
                .eq(LikedRecord::getUserId, userId)
                .eq(LikedRecord::getBizId, formDTO.getBizId())
                .remove();

        if (!success) {
            throw new DbException(LikedRecordConstants.FAILED_DELETE_LIKES_RECORD);
        }
        return true;
    }


    @Override
    public Set<Long> isBizLiked(List<Long> bizIds) {
        Long userId = UserContext.getUser();

        List<LikedRecord> list = lambdaQuery()
                .eq(LikedRecord::getUserId, userId)
                .in(LikedRecord::getBizId, bizIds)
                .list();

        return list.stream().map(LikedRecord::getBizId).collect(Collectors.toSet());
    }

    @Override
    public void readLikedTimesAndSendMessage(String bizType, int maxBizSize) {

    }
}
