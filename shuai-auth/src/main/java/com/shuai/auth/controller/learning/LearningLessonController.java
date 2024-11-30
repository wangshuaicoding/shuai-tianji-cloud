package com.shuai.auth.controller.learning;


import com.shuai.auth.service.ILearningLessonService;
import com.shuai.common.domain.R;
import com.shuai.common.domain.dto.PageDTO;
import com.shuai.common.domain.query.PageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 学生课程表 前端控制器
 * </p>
 *
 * @author Shuai
 * @since 2024-11-29
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/lessons")
public class LearningLessonController {

    private final ILearningLessonService learningLessonService;

    @ApiOperation("分页查询我的课表")
    @GetMapping("/page")
    public R<PageDTO> queryLessonPage(PageQuery pageQuery) {
        return R.ok(learningLessonService.queryLessonPage(pageQuery));
    }

    // @ApiOperation("查看我最近正在学习的客户")
    // @GetMapping("/now")
    // public


}
