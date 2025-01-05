package com.shuai.auth.controller.learning;


import com.shuai.auth.domain.query.PointsBoardQuery;
import com.shuai.auth.domain.vo.PointsBoardVO;
import com.shuai.auth.service.IPointsBoardService;
import com.shuai.common.domain.R;
import com.shuai.common.domain.dto.PageDTO;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 学霸天梯榜 前端控制器
 * </p>
 *
 * @author Shuai
 * @since 2024-12-26
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class PointsBoardController {

    private final IPointsBoardService pointsBoardService;

    @ApiOperation("查询指定赛季的积分排行榜以及当前用户的积分和排名信息")
    @GetMapping
    public R<PointsBoardVO> queryPointsBoard(PointsBoardQuery query) {
        return R.ok(pointsBoardService.queryPointsBoard(query));
    }
}
