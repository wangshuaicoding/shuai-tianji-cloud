package com.shuai.user.service.impl;

import com.shuai.api.dto.remark.LikedTimesDTO;
import com.shuai.common.autoconfigure.mq.RabbitMqHelper;
import com.shuai.common.constants.MqConstants;
import com.shuai.common.exceptions.BizIllegalException;
import com.shuai.common.utils.CollUtils;
import com.shuai.common.utils.StringUtils;
import com.shuai.common.utils.UserContext;
import com.shuai.user.constants.remark.LikedRecordConstants;
import com.shuai.user.constants.remark.RedisConstants;
import com.shuai.user.domain.dto.LikedRecordFormDTO;
import com.shuai.user.domain.po.LikedRecord;
import com.shuai.user.mapper.LikedRecordMapper;
import com.shuai.user.service.ILikedRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <p>
 * 点赞记录表 服务实现类
 * </p>
 *
 * @author Shuai
 * @since 2024-12-20
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LikedRecordServiceRedisImpl extends ServiceImpl<LikedRecordMapper, LikedRecord> implements ILikedRecordService {

    private final RabbitMqHelper mqHelper;
    private final StringRedisTemplate redisTemplate;

    @Override
    public void likeOrUnlikeRecord(LikedRecordFormDTO formDTO) {
        boolean success = formDTO.getLiked() ? like(formDTO) : unlike(formDTO);

        if (!success) {
            throw new BizIllegalException(LikedRecordConstants.OPERATION_NOT_REPEATED);
        }

        // 如果执行成功统计点赞总数
        Long likedTimes = redisTemplate.opsForSet()
                .size(RedisConstants.LIKE_BIZ_KEY_PREFIX + formDTO.getBizId());
        if (likedTimes == null) {
            return;
        }

        // 缓存点赞总数到 redis
        redisTemplate.opsForZSet().add(
                RedisConstants.LIKE_TIMES_KEY_PREFIX + formDTO.getBizType(),  // key
                formDTO.getBizId().toString(),  // value
                likedTimes   // 分数
        );

        // // 统计点赞业务id的点赞数
        // Integer count = lambdaQuery()
        //         .eq(LikedRecord::getBizId, formDTO.getBizId())
        //         .count();

        // 发送MQ请求
        // mqHelper.send(
        //         MqConstants.Exchange.LIKE_RECORD_EXCHANGE,
        //         StringUtils.format(MqConstants.Key.LIKED_TIMES_KEY_TEMPLATE,formDTO.getBizType()),
        //         LikedTimesDTO.of(formDTO.getBizId(), count)
        // );
    }

    private boolean like(LikedRecordFormDTO formDTO) {
        // Long userId = UserContext.getUser();
        Long userId = 2L;
        // LikedRecord one = lambdaQuery()
        //         .eq(LikedRecord::getUserId, userId)
        //         .eq(LikedRecord::getBizId, formDTO.getBizId())
        //         .one();
        // if (one != null) {
        //     log.info("用户:{}已经点赞过该业务:{}", userId, formDTO.getBizId());
        //     return false;
        // }
        //
        // LikedRecord likedRecord = new LikedRecord()
        //         .setUserId(userId)
        //         .setBizId(formDTO.getBizId())
        //         .setBizType(formDTO.getBizType().toString());
        //
        // boolean success = save(likedRecord);
        // if (!success) {
        //     throw new DbException(LikedRecordConstants.FAILED_INSERT_LIKES_RECORD);
        // }
        // return true;
        // 获取key
        String key = RedisConstants.LIKE_BIZ_KEY_PREFIX + formDTO.getBizId();
        // 执行SADD命令
        Long result = redisTemplate.opsForSet().add(key, userId.toString());
        return result != null && result > 0;
    }

    private boolean unlike(LikedRecordFormDTO formDTO) {
        Long userId = 2L;
        // boolean success = lambdaUpdate()
        //         .eq(LikedRecord::getUserId, userId)
        //         .eq(LikedRecord::getBizId, formDTO.getBizId())
        //         .remove();
        //
        // if (!success) {
        //     throw new DbException(LikedRecordConstants.FAILED_DELETE_LIKES_RECORD);
        // }

        // 获取key
        String key = RedisConstants.LIKE_BIZ_KEY_PREFIX + formDTO.getBizId();
        // 执行SREM命令
        Long result = redisTemplate.opsForSet().remove(key, userId.toString());
        return result != null && result > 0;
    }


    @Override
    public Set<Long> isBizLiked(List<Long> bizIds) {
        Long userId = UserContext.getUser();

        // List<LikedRecord> list = lambdaQuery()
        //         .eq(LikedRecord::getUserId, userId)
        //         .in(LikedRecord::getBizId, bizIds)
        //         .list();
        //
        // return list.stream().map(LikedRecord::getBizId).collect(Collectors.toSet());

        // 查询点赞状态，所有命令的结果都会被收集到 objects 中
        List<Object> objects = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            StringRedisConnection src = (StringRedisConnection) connection;
            for (Long bizId : bizIds) {
                String key = RedisConstants.LIKE_BIZ_KEY_PREFIX + bizId;
                // 每次调用 sIsMember 都会返回一个布尔值，指示用户是否点赞了该业务。
                src.sIsMember(key, userId.toString());
            }
            return null;
        });

        // 返回结果
        return IntStream.range(0, objects.size())  // 创建从0到集合size的流
                .filter(i -> (Boolean) objects.get(i))  // 遍历每个元素，保留结果为true的角标i
                .mapToObj(bizIds::get)  // 用角标i取bizIds中的对应数据，就是点赞过的id
                .collect(Collectors.toSet());  // 收集
    }


    @Override
    public void readLikedTimesAndSendMessage(String bizType, int maxBizSize) {
        // 1、读取并移除redis中缓存的点赞总数
        String key = RedisConstants.LIKE_TIMES_KEY_PREFIX + bizType;
        // 从有序集合中弹出（删除）最多maxBizSize个最小分数的元素，并返回这些元素及分数
        Set<ZSetOperations.TypedTuple<String>> tuples = redisTemplate.opsForZSet().popMin(key, maxBizSize);
        if (CollUtils.isEmpty(tuples)) {
            return;
        }

        // 2、数据转换
        List<LikedTimesDTO> list = new ArrayList<>(tuples.size());
        for (ZSetOperations.TypedTuple<String> tuple : tuples) {
            String bizId = tuple.getValue();
            Double likedTimes = tuple.getScore();
            if (bizId == null || likedTimes == null) {
                continue;
            }
            list.add(LikedTimesDTO.of(Long.valueOf(bizId), likedTimes.intValue()));
        }

        // 3、发送MQ消息
        mqHelper.send(
                MqConstants.Exchange.LIKE_RECORD_EXCHANGE,
                StringUtils.format(MqConstants.Key.LIKED_TIMES_KEY_TEMPLATE, bizType),
                list
        );
    }
}
