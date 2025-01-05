package com.shuai.auth.util;

import com.shuai.auth.domain.po.LearningLesson;
import com.shuai.auth.domain.po.LearningRecord;
import com.shuai.auth.mapper.LearningRecordMapper;
import com.shuai.auth.service.ILearningLessonService;
import com.shuai.common.utils.JsonUtils;
import com.shuai.common.utils.StringUtils;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.DelayQueue;

@Slf4j
@Component
@RequiredArgsConstructor
public class LearningRecordDelayTaskHandler {

    private final DelayQueue<DelayTask<RecordTaskData>> queue = new DelayQueue<>();
    private final StringRedisTemplate redisTemplate;
    private final LearningRecordMapper LearningRecordMapper;
    private final ILearningLessonService learningLessonService;
    private final static String RECORD_KEY_TEMPLATE = "learning:record:{}";
    private static volatile boolean begin = true;

    @PostConstruct  // 用于初始化操作
    public void init() {
        // 用于异步执行一个不返回结果的任务
        CompletableFuture.runAsync(this::handleDelayTask);
    }

    // 用于执行清理操作
    @PreDestroy
    public void destroy() {
        begin = false;
        log.debug("延迟任务停止执行");
    }

    public void handleDelayTask() {
        while (begin) {
            try {
                // 1、获得到期的延迟任务
                DelayTask<RecordTaskData> take = queue.take();
                RecordTaskData data = take.getData();
                // 2、查询redis 缓存
                LearningRecord record = readRecordCache(data.getLessonId(), data.getSectionId());
                if (record == null) {
                    continue;
                }

                // 3、比较moment的值
                if (!Objects.equals(record.getMoment(), data.getMoment())) {
                    // 如果延迟队列中的数据，与redis中的数据不一致，则不是最后一次提交，直接跳过
                    continue;
                }

                // 4、持久化播放进度数据到数据库
                record.setFinished(null);
                LearningRecordMapper.updateById(record);
                // 5、更新课表最近的学习信息
                LearningLesson learningLesson = new LearningLesson();
                learningLesson.setId(data.getLessonId());
                learningLesson.setLatestSectionId(data.getSectionId());
                learningLesson.setLatestLearnTime(LocalDateTime.now());
                learningLessonService.updateById(learningLesson);

            } catch (InterruptedException e) {
                log.error("处理延迟任务出现异常", e);
            }
        }
    }

    public LearningRecord readRecordCache(Long lessonId, Long sectionId) {
        String key = StringUtils.format(RECORD_KEY_TEMPLATE, lessonId);
        Object cacheData = redisTemplate.opsForHash().get(key, sectionId.toString());
        if (cacheData == null) {
            return null;
        }

        // 2、数据检查和转换
        return JsonUtils.toBean(cacheData.toString(), LearningRecord.class);
    }

    public void addLearningRecordTask(LearningRecord record) {
        // 添加数据到redis缓存
        writeRecordCache(record);
        // 提交延迟任务到延迟队列
        queue.add(new DelayTask<>(new RecordTaskData(record), Duration.ofSeconds(20)));
    }

    public void writeRecordCache(LearningRecord record) {
        log.info("更新学习记录到缓存数据");
        try {
            String json = JsonUtils.toJsonStr(new RecordCacheData(record));
            // 写入redis
            String key = StringUtils.format(RECORD_KEY_TEMPLATE, record.getLessonId());
            redisTemplate.opsForHash().put(key, record.getSectionId().toString(), json);

            // 添加过期时间
            // Duration.ofMinutes(1) ：设置一分钟的持续时间
            redisTemplate.expire(key, Duration.ofMinutes(1));
        } catch (Exception e) {
            log.error("更新缓存数据异常", e);
        }
    }

    public void cleanRecordCache(Long lessonId, Long sectionId) {
        String key = StringUtils.format(RECORD_KEY_TEMPLATE, lessonId);
        redisTemplate.opsForHash().delete(key, sectionId.toString());
    }

    @Data
    @NoArgsConstructor
    public static class RecordCacheData{
        private Long id;
        private Integer moment;
        private Boolean finished;

        public RecordCacheData(LearningRecord record) {
            this.id = record.getId();
            this.moment = record.getMoment();
            this.finished = record.getFinished();
        }
    }
    @Data
    @NoArgsConstructor
    public static class RecordTaskData {
        private Long lessonId;
        private Long sectionId;
        private Integer moment;

        public RecordTaskData(LearningRecord record) {
            this.lessonId = record.getLessonId();
            this.sectionId = record.getSectionId();
            this.moment = record.getMoment();
        }
    }
}
