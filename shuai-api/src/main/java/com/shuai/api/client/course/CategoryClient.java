package com.shuai.api.client.course;


import com.shuai.api.dto.course.CategoryBasicDTO;
import com.shuai.common.constants.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(contextId = "remoteCategoryService", value = ServiceNameConstants.MESSAGE_SERVICE, path = "category")
public interface CategoryClient {

    /**
     * 获取所有课程及课程分类
     * @return 所有课程及课程分类
     */
    @GetMapping("getAllOfOneLevel")
    List<CategoryBasicDTO> getAllOfOneLevel();
}
