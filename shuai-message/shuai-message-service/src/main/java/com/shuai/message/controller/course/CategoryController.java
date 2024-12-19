package com.shuai.message.controller.course;


import com.shuai.api.dto.course.CategoryBasicDTO;
import com.shuai.message.domain.vo.CategoryVO;
import com.shuai.message.service.ICategoryService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 课程分类 前端控制器
 * </p>
 *
 * @author Shuai
 * @since 2024-12-18
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final ICategoryService categoryService;

    @ApiOperation("获取所有课程分类")
    @GetMapping("getAllOfOneLevel")
    public List<CategoryVO> getAllOfOneLevel() {
        return categoryService.getAllOfOneLevel();
    }
}
