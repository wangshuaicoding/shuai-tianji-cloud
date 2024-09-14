package com.shuai.message.handler;

import com.shuai.common.domain.dto.PageDTO;
import com.shuai.common.utils.StringUtils;
import com.shuai.message.domain.po.NoticeTask;
import com.shuai.message.service.INoticeTaskService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NoticeJobHandler {

    private final INoticeTaskService taskService;

    @XxlJob("publishNoticeJob")
    public void publishNotice(){
        // 1.获取分片参数，作为数据查询的分页参数，分片数+1作为页码，jobParam作为每页大小，默认size为10
        int index = XxlJobHelper.getShardIndex() + 1;
        String jobParam = XxlJobHelper.getJobParam();
        int size = StringUtils.isNumeric(jobParam) ? Integer.parseInt(jobParam) : 10;
        // 2.查询待处理的任务：发布时间已经超时，并且尚未完成的任务
        PageDTO<NoticeTask> page = taskService.queryTodoNoticeTaskByPage(index, size);
        if (page.isEmpty()) {
            // 没有待处理任务，直接结束
            return;
        }
        // 3.有待处理任务，开搞
        List<NoticeTask> list = page.getList();
        for (NoticeTask noticeTask : list) {
            taskService.handleTask(noticeTask);
        }
    }
}