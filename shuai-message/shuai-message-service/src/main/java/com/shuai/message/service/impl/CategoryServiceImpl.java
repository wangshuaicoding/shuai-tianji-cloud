package com.shuai.message.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuai.api.dto.course.CategoryBasicDTO;
import com.shuai.common.utils.BeanUtils;
import com.shuai.common.utils.NumberUtils;
import com.shuai.message.domain.po.Category;
import com.shuai.message.domain.vo.CategoryVO;
import com.shuai.message.mapper.CategoryMapper;
import com.shuai.message.service.ICategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程分类 服务实现类
 * </p>
 *
 * @author Shuai
 * @since 2024-12-18
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Override
    public List<CategoryVO> getAllOfOneLevel() {
        List<Category> categoryList = baseMapper.selectList(null);
        if (CollectionUtil.isEmpty(categoryList)) {
            return new ArrayList<>();
        }

        // 统计一级二级目录对应三级目录的数量，做一个三分钟的redis 缓存
        Map<Long,Long> ThirdCategoryNumMap = this.statisticThirdCategory(categoryList);

        List<CategoryVO> categoryVOS = BeanUtil.copyToList(categoryList, CategoryVO.class);
        if (CollectionUtil.isNotEmpty(ThirdCategoryNumMap)) {
            for (CategoryVO categoryVO : categoryVOS) {
                categoryVO.setThirdCategoryNum(ThirdCategoryNumMap.getOrDefault(categoryVO.getId(),0L).intValue());
            }
        }
        return categoryVOS;
    }

    private Map<Long, Long> statisticThirdCategory(List<Category> categoryList) {
        Map<Long, Long> result = new HashMap<>();

        // 过滤出二级分类拥有三级分类的数量
        Map<Long, Long> pMap = categoryList.stream()
                .filter(category -> category.getLevel() == 3)
                .collect(Collectors.groupingBy(Category::getParentId, Collectors.counting()));

        result.putAll(pMap);

        // 过滤出一级分类拥有的三级课程分类的数量
        Map<Long, List<Category>> category2Map = categoryList.stream()
                .filter(category -> category.getLevel() == 2)
                .collect(Collectors.groupingBy(Category::getParentId));

        // 遍历category2Map
        for (Map.Entry<Long, List<Category>> entry : category2Map.entrySet()) {
            long sum = entry.getValue()
                    .stream()
                    .map(category -> NumberUtils.null2Zero(pMap.get(category.getId())))
                    .collect(Collectors.summarizingLong(num -> num))
                    .getSum();
            result.put(entry.getKey(), sum);
        }

        return result;
    }
}
