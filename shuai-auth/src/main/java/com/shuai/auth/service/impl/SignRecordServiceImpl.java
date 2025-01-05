package com.shuai.auth.service.impl;

import com.shuai.auth.constants.RedisConstants;
import com.shuai.auth.constants.SignRecordConstants;
import com.shuai.auth.domain.po.SignRecord;
import com.shuai.auth.domain.vo.SignRecordVO;
import com.shuai.auth.mapper.SignRecordMapper;
import com.shuai.auth.mq.message.SignInMessage;
import com.shuai.auth.service.ISignRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuai.common.autoconfigure.mq.RabbitMqHelper;
import com.shuai.common.constants.MqConstants;
import com.shuai.common.exceptions.BizIllegalException;
import com.shuai.common.utils.BooleanUtils;
import com.shuai.common.utils.CollUtils;
import com.shuai.common.utils.DateUtils;
import com.shuai.common.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 签到记录表 服务实现类
 * </p>
 *
 * @author Shuai
 * @since 2024-12-24
 */
@Service
@RequiredArgsConstructor
public class SignRecordServiceImpl extends ServiceImpl<SignRecordMapper, SignRecord> implements ISignRecordService {

    private final StringRedisTemplate redisTemplate;

    private final RabbitMqHelper mqHelper;

    @Override
    public SignRecordVO addSignRecord() {
        // 1、签到
        // Long userId = UserContext.getUser();
        Long userId = 2L;

        LocalDateTime now = LocalDateTime.now();
        // 拼接key
        String key = RedisConstants.SIGN_RECORD_KEY_PREFIX
                + userId
                + now.format(DateUtils.SIGN_DATE_SUFFIX_FORMATTER);

        // 计算偏移量 offset
        int offset = now.getDayOfMonth() - 1;
        // 保存签到信息
        Boolean exists = redisTemplate.opsForValue().setBit(key, offset, true);
        if (BooleanUtils.isTrue(exists)) {
            throw new BizIllegalException(SignRecordConstants.DUPLICATE_CHECK_NOT_ALLOWED);
        }

        // 2、计算连续签到的天数
        int signDays = countSignDays(key,now.getDayOfMonth());

        // 3、计算签到得分
        int rewardPoints = 0;
        switch (signDays) {
            case 7:
                rewardPoints = 10;
                break;
            case 14:
                rewardPoints = 20;
                break;
            case 28:
                rewardPoints = 40;
                break;
        }

        // 4.保存积分明细记录
        mqHelper.send(
                MqConstants.Exchange.LEARNING_EXCHANGE,
                MqConstants.Key.SIGN_IN,
                SignInMessage.of(userId,rewardPoints + 1)  // 签到积分是基本积分+奖励积分
        );
         // 封装返回
        SignRecordVO vo = new SignRecordVO();
        vo.setSignDays(signDays);
        vo.setRewardPoints(rewardPoints);
        return vo;
    }


    private int countSignDays(String key, int len) {
        // 1.获取本月从第一天开始，到今天为止的所有签到记录
        /**
         * BitFieldSubCommands.create():创建一个新的 BitFieldSubCommands 对象，用于定义 BITFIELD 命令的具体操作。
         * BitFieldSubCommands.BitFieldType.unsigned(len):定义要获取的位字段类型为无符号整数。如果len为31，则表示该字段最多可以存储一个31位的无符号整数
         * .valueAt(0): 列表中只有一个元素，因为命令中我们只指定了一个获取操作，从位图的最左边（最高位）开始读取
         */
        List<Long> result = redisTemplate.opsForValue()
                .bitField(key, BitFieldSubCommands.create().get(
                        BitFieldSubCommands.BitFieldType.unsigned(len)).valueAt(0));
        if (CollUtils.isEmpty(result)) {
            return 0;
        }

        int num = result.get(0).intValue();
        // 定义一个计数器
        int count = 0;
        // 循环，与1做与运算，得到最后一个bit,判断是否为0，为0则终止，为1则继续
        while ((num & 1) == 1) {
            // 计数器+1
            count++;
            /**
             * 把数字右移一位，最后一位被舍弃，倒数第二位成了最后一位
             * >>> 是逻辑右移
             * num >>>= 1;就等于 num = num >>> 1;
             */
            num >>>= 1;
        }
        return count;
    }
}
