package com.shuai.user.controller;


import com.shuai.common.domain.dto.PageDTO;
import com.shuai.user.domain.query.UserPageQuery;
import com.shuai.user.domain.vo.TeacherPageVO;
import com.shuai.user.service.ITeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 教师详情表 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/teachers")
@Api(tags = "用户管理接口")
public class TeacherController {

    @Autowired
    private ITeacherService teacherService;

    @GetMapping("/page")
    @ApiOperation("分页查询教师信息")
    public PageDTO<TeacherPageVO> queryTeacherPage(UserPageQuery pageQuery){
        return teacherService.queryTeacherPage(pageQuery);
    }
}
