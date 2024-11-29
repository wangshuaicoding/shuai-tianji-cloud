package com.shuai.message.controller.course;


import com.shuai.api.dto.course.CourseSimpleInfoDTO;
import com.shuai.message.service.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 草稿课程 前端控制器
 * </p>
 *
 * @author Shuai
 * @since 2024-11-29
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {

    private final ICourseService courseService;

    @GetMapping("/simpleInfo/list")
    public List<CourseSimpleInfoDTO> selectListByIds(@RequestParam("ids") List<Long> ids) {
        return courseService.selectListByIds(ids);
    }
}
