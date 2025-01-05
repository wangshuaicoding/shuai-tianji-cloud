package com.shuai.auth.util;

import lombok.Data;

import java.time.Duration;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延迟任务类，保持任务数据
 * @param <D>
 */
@Data
public class DelayTask<D> implements Delayed {
    // 表示任务的具体数据
    private D data;
    // 表示任务到期的时间（纳秒）
    private long deadlineNanos;

    public DelayTask(D data, Duration delayTime) {
        this.data = data;
        /**
         * System.nanoTime():当前时间的纳秒数
         * delayTime.toNanos()：延迟时间的纳秒数
         */
        this.deadlineNanos = System.nanoTime() + delayTime.toNanos();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        // unit.convert(): 将纳秒转换成指定的时间单位
        return unit.convert(Math.max(0, deadlineNanos - System.nanoTime()), TimeUnit.NANOSECONDS);
    }

    /**
     * l 的值最小，谁先被执行
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(Delayed o) {
        long l = getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        if(l > 0){
            return 1;
        }else if(l < 0){
            return -1;
        }else {
            return 0;
        }
    }
}
