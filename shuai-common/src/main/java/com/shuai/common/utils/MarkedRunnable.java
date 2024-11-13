package com.shuai.common.utils;

import org.slf4j.MDC;

import java.util.Map;

public class MarkedRunnable implements Runnable{
    private Runnable runnable;
    private Map<String, String> context;

    public MarkedRunnable(Runnable runnable) {
        this.runnable = runnable;
        // 获取当前线程的MDC上下文副本 2024/11/13 21:13 By 少帅
        this.context = MDC.getCopyOfContextMap();
    }

    @Override
    public void run() {
        if(context == null){
            MDC.clear();
        }else {
            // 将MDC上下文副本设置到另一个线程 2024/11/13 21:14 By 少帅
            MDC.setContextMap(context);
        }

        try {
            runnable.run();
        }finally {
            MDC.clear();
        }
    }
}
