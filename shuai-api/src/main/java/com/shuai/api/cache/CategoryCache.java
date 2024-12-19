package com.shuai.api.cache;


import com.github.benmanes.caffeine.cache.Cache;
import com.shuai.api.client.course.CategoryClient;
import com.shuai.api.dto.course.CategoryBasicDTO;
import com.shuai.common.utils.CollUtils;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 课程分类很多地方要用，所以使用本地缓存
 */
@RequiredArgsConstructor
public class CategoryCache {

    private final Cache<String, Map<Long, CategoryBasicDTO>> categoryCaches;
    private final CategoryClient categoryClient;

    /**
     * 根据三级分类的数据，例：[1,3,6]
     */
    public String getCategoryName(List<Long> ids) {
        if (CollUtils.isEmpty(ids)) {
            return "";
        }
        // 1、读取分类缓存
        Map<Long, CategoryBasicDTO> cMap = getCategoryMap();
        // 2、根据id分类名称并组装
        StringBuilder sb = new StringBuilder();
        for (Long id : ids) {
            sb.append(cMap.get(id).getName()).append("/");
        }
        // 返回结果
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    public Map<Long, CategoryBasicDTO> getCategoryMap() {
        return categoryCaches.get("CATEGORY", key -> {
            List<CategoryBasicDTO> list = categoryClient.getAllOfOneLevel();
            if (CollUtils.isEmpty(list)) {
                return CollUtils.emptyMap();
            }
            // 2、转换数据
            return list.stream().collect(Collectors.toMap(CategoryBasicDTO::getId, v -> v));
        });
    }
}
