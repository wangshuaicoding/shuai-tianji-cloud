package com.shuai.auth.controller.learning;


import com.shuai.auth.domain.dto.LearningPlansDTO;
import com.shuai.auth.domain.dto.LearningRecordFormDTO;
import com.shuai.auth.domain.vo.LearningProgressVO;
import com.shuai.auth.service.ILearningRecordService;
import com.shuai.common.domain.R;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 学习记录表 前端控制器
 * </p>
 *
 * @author Shuai
 * @since 2024-12-14
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/learning-records")
public class LearningRecordController {

    private final ILearningRecordService learningRecordService;

    @ApiOperation("查询当前用户指定课程的学习进度")
    @GetMapping("/course/{courseId}")
    public R<LearningProgressVO> queryLearningRecord(@PathVariable("courseId") Long courseId) {
        return R.ok(learningRecordService.queryLearningRecord(courseId));
    }

    @ApiOperation("提交学习记录")
    @PostMapping
    public R saveLearningRecord(@RequestBody @Valid LearningRecordFormDTO formDTO) {
        learningRecordService.saveLearningRecord(formDTO);
        return R.ok();
    }
}
