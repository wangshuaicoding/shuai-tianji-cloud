package com.shuai.api.client.course;

import com.shuai.api.client.course.fallback.CourseClientFallback;
import com.shuai.api.dto.course.CourseFullInfoDTO;
import com.shuai.api.dto.course.CourseSimpleInfoDTO;
import com.shuai.common.constants.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 首先标记为 feign客户端
 */
@FeignClient(contextId = "remoteCourseService", value = ServiceNameConstants.MESSAGE_SERVICE, fallbackFactory = CourseClientFallback.class)
public interface CourseClient {

    @GetMapping("/course/simpleInfo/list")
    List<CourseSimpleInfoDTO> selectListByIds(@RequestParam("ids") Iterable<Long> ids);

    /**
     * 根据id查询课程详情
     * @param id
     * @return
     * mvc注解的两套使用逻辑
     * 1、标注在Controller上，是接受这样的请求
     * 2、标注在FeignClient上，是发送这样的请求
     *  是把传过来的 id 赋值到@PathVariable("id")，最后放到/{id}上，帮助我们发送这个请求，底层使用的是restTemplate
     */
    @GetMapping("/course/fullInfo/{id}")
    CourseFullInfoDTO selectFullInfoById(@PathVariable("id") Long id);
}
