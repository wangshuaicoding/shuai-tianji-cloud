package com.shuai.user.controller;


import com.shuai.common.domain.R;
import com.shuai.common.domain.dto.PageDTO;
import com.shuai.user.domain.dto.StudentFormDTO;
import com.shuai.user.domain.query.UserPageQuery;
import com.shuai.user.domain.vo.StudentPageVo;
import com.shuai.user.service.IStudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 学员详情表 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/students")
@Api(tags = "用户管理接口")
public class StudentController {

    @Autowired
    private IStudentService studentService;

    @ApiOperation("分页查询学生信息")
    @GetMapping("/page")
    public R<PageDTO<StudentPageVo>> queryStudentPage(UserPageQuery pageQuery){
        return R.ok(studentService.queryStudentPage(pageQuery));
    }

    @ApiOperation("学员注册")
    @PostMapping("/register")
    public R<Void> registerStudent(@RequestBody @Valid StudentFormDTO studentFormDTO) {
        studentService.saveStudent(studentFormDTO);
        return R.ok();
    }

    @ApiOperation("修改学员密码")
    @PutMapping("/password")
    public void updateMyPassword(@RequestBody StudentFormDTO studentFormDTO) {
        studentService.updateMyPassword(studentFormDTO);
    }
}
