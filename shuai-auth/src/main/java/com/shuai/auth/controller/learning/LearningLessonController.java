package com.shuai.auth.controller.learning;


import com.shuai.auth.constants.CourseConstants;
import com.shuai.auth.domain.vo.LearningLessonVO;
import com.shuai.auth.service.ILearningLessonService;
import com.shuai.common.domain.R;
import com.shuai.common.domain.dto.PageDTO;
import com.shuai.common.domain.query.PageQuery;
import com.shuai.common.exceptions.BadRequestException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("查看我最近正在学习的课程")
    @GetMapping("/now")
    public R<LearningLessonVO> checkMyRecentCourse() {
        return R.ok(learningLessonService.checkMyRecentCourse());
    }

    @ApiOperation("查询指定课程的学习状态")
    @GetMapping("/{courseId}")
    public R<Integer> queryLearningStatus(@PathVariable("courseId") Long courseId) {
        return R.ok(learningLessonService.queryLearningStatus(courseId));
    }

    @ApiOperation("删除课表中的某课程")
    @PostMapping("/{courseId}")
    public R deleteCourse(@PathVariable("courseId") Long courseId) {
        Boolean flag = learningLessonService.removeLearningLessonById(courseId);
        if (!flag) {
            throw new BadRequestException(CourseConstants.COURSE_DELETE_FAILED);
        }
        return R.ok();
    }

    @ApiOperation("校验指定课程是否是课表中的有效课程")
    @GetMapping("/{courseId}/valid")
    public Long checkCourseValid(@PathVariable("courseId") Long courseId) {
        return learningLessonService.checkCourseValid(courseId);
    }

    @ApiOperation("统计课程学习人数")
    @GetMapping("/{courseId}/count")
    public Integer countLearningUser(@PathVariable("courseId") Long courseId) {
        return learningLessonService.countLearningUser(courseId);
    }
}
