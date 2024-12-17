package com.shuai.auth.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.DelayQueue;

@Slf4j
class DelayTaskTest {

    /**
     * 这里使用的是同一个线程来执行的任务，当没有任务的时候线程会阻塞，在实际开发中会使用线程池。
     * @throws InterruptedException
     */
    @Test
    void testDelayTask() throws InterruptedException {
        // 1、初始化延迟队列
        DelayQueue<DelayTask<String>> delayQueue = new DelayQueue<>();

        // 2、向队列中添加延迟执行的任务
        log.info("开始初始化延迟任务....");
        delayQueue.add(new DelayTask<>("任务3", Duration.ofSeconds(3)));
        delayQueue.add(new DelayTask<>("任务1", Duration.ofSeconds(1)));
        delayQueue.add(new DelayTask<>("任务2", Duration.ofSeconds(2)));

        // 3、尝试执行任务
        while (true) {
            DelayTask<String> take = delayQueue.take();
            log.info("执行任务：{}", take.getData());
        }
    }
}