package com.shuai.auth.controller.learning;


import com.shuai.auth.domain.vo.PointsRecordVO;
import com.shuai.auth.service.IPointsRecordService;
import com.shuai.common.domain.R;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 学习积分记录，每个月底清零 前端控制器
 * </p>
 *
 * @author Shuai
 * @since 2024-12-24
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/points")
public class PointsRecordController {

    private final IPointsRecordService pointsRecordService;

    @ApiOperation("查询我的今日积分")
    @GetMapping("today")
    public R<List<PointsRecordVO>> queryMyTodayPoints() {
        return R.ok(pointsRecordService.queryMyTodayPoints());
    }

    @ApiOperation("查询签到记录")
    @GetMapping
    public R<List<Integer>> queryMySignRecord() {
        return R.ok(pointsRecordService.queryMySignRecord());
    }
}
