package com.shuai.message.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuai.common.utils.BeanUtils;
import com.shuai.common.utils.CollUtils;
import com.shuai.message.domain.po.CourseCatalogue;
import com.shuai.message.domain.vo.CataSimpleInfoVO;
import com.shuai.message.mapper.CourseCatalogueMapper;
import com.shuai.message.service.ICourseCatalogueService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 目录草稿 服务实现类
 * </p>
 *
 * @author Shuai
 * @since 2024-11-30
 */
@Service
public class CourseCatalogueServiceImpl extends ServiceImpl<CourseCatalogueMapper, CourseCatalogue> implements ICourseCatalogueService {

    @Override
    public List<CataSimpleInfoVO> batchQuery(List<Long> ids) {
        if (CollUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<CourseCatalogue> list = lambdaQuery().in(CourseCatalogue::getId, ids).list();
        // 对BeanUtils进行封装，封装判空操作，减少代码冗余 2024/11/30 15:34 By 少帅
        return BeanUtils.copyList(list, CataSimpleInfoVO.class);
    }
}
