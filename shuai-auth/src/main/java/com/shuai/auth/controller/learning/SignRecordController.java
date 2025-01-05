package com.shuai.auth.controller.learning;


import com.shuai.auth.domain.vo.SignRecordVO;
import com.shuai.auth.service.ISignRecordService;
import com.shuai.common.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 签到记录表 前端控制器
 * </p>
 *
 * @author Shuai
 * @since 2024-12-24
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sign-records")
public class SignRecordController {

    private final ISignRecordService signRecordService;

    @ApiOperation("签到功能接口")
    @PostMapping
    public R<SignRecordVO> addSignRecord() {
        return R.ok(signRecordService.addSignRecord());
    }
}
