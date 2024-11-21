package com.shuai.user.controller;


import com.shuai.common.domain.R;
import com.shuai.user.domain.dto.StudentFormDTO;
import com.shuai.user.service.ICodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 验证码
 */
@RestController
@RequestMapping("/code")
@RequiredArgsConstructor
@Api(tags = "用户管理接口")
public class CodeController {

    private final ICodeService codeService;

    @ApiOperation("注册获取验证码")
    @PostMapping
    public R<Void> sendVerifyCode(@RequestBody @Valid StudentFormDTO studentFormDTO) {
        codeService.sendVerifyCode(studentFormDTO.getCellPhone());
        return R.ok();
    }
}
