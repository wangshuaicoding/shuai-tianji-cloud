package com.shuai.user.task;

import com.shuai.user.service.ILikedRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LikedTimesCheckTask {

    private static final List<String> BIZ_TYPES = Arrays.asList("QA", "NOTE");

    // 避免一次处理的业务过多，每次处理的业务数量为30
    private static final int MAX_BIZ_SIZE = 30;

    private final ILikedRecordService likedRecordService;

    // todo 更改为XXL-job的方式试试
    @Scheduled(fixedDelay = 20000)
    public void checkLikedTimes() {
        for (String bizType : BIZ_TYPES) {
            likedRecordService.readLikedTimesAndSendMessage(bizType,MAX_BIZ_SIZE);
        }
    }
}
