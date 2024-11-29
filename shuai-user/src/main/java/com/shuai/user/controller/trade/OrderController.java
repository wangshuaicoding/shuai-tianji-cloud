package com.shuai.user.controller.trade;


import com.shuai.user.domain.vo.trade.PlaceOrderResultVO;
import com.shuai.user.service.IOrderService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author Shuai
 * @since 2024-11-29
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final IOrderService orderService;

    @ApiOperation("免费课立即报名接口")
    @PostMapping("/freeCourse/{courseId}")
    public PlaceOrderResultVO enrolledFreeCourse(@PathVariable("courseId") Long courseId) {
        return orderService.enrolledFreeCourse(courseId);
    }
}
