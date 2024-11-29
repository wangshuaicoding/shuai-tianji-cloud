package com.shuai.api.client.course;

import com.shuai.api.client.course.fallback.CourseClientFallback;
import com.shuai.api.dto.course.CourseSimpleInfoDTO;
import com.shuai.common.constants.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "remoteCourseService", value = ServiceNameConstants.MESSAGE_SERVICE, fallbackFactory = CourseClientFallback.class)
public interface CourseClient {

    @GetMapping("/course/simpleInfo/list")
    List<CourseSimpleInfoDTO> selectListByIds(@RequestParam("ids") List<Long> ids);
}
