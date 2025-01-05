package com.shuai.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shuai.api.dto.course.CategoryBasicDTO;
import com.shuai.message.domain.po.Category;
import com.shuai.message.domain.vo.CategoryVO;

import java.util.List;

/**
 * <p>
 * 课程分类 服务类
 * </p>
 *
 * @author Shuai
 * @since 2024-12-18
 */
public interface ICategoryService extends IService<Category> {

    List<CategoryVO> getAllOfOneLevel();
}
