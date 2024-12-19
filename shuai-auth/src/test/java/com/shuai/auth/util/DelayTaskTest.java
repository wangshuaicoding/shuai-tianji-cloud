package com.shuai.auth.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.stream.Collectors;

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


    /**
     * 测试本地缓存caffeine
     *  使用场景：
     *      数据量小
     *      不经常更改的数据
     */
    @Test
    void testBasicOps() {
        // 创建cache对象
        Cache<String, String> cache = Caffeine.newBuilder().build();

        // 存数据
        // cache.put("shuai", "迪丽热巴");

        // 取数据
        String shuai = cache.getIfPresent("shuai");
        System.out.println("shuai=" + shuai);

        // 取数据，包含两个参数
        // 参数一：缓存的key
        // 参数二：lambda表达式，表达式参数就是缓存的key,方法体中是查询数据库的逻辑
        // 优先根据key查询JVM 缓存,如果未命中则执行参数二的lambda表达式
        String defaultShuai = cache.get("shuai", (key) -> {
            // 根据key去数据库查询数据
            return "周星驰";
        });

        System.out.println("defaultShuai=" + defaultShuai);
    }

    /**
     * 测试stream 的用法
     */
    @Test
    void testStream() {

        // 2,4,6,8
        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        long sum = list.stream()
                .map(item -> item * 2)
                .collect(Collectors.summarizingLong(num -> num))
                .getSum();

        int ss = 0;
        for (Integer i : list) {
            ss += (int) (i*2);
        }
        System.out.println(ss);
        System.out.println(sum);
    }
}