package com.shuai.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shuai.message.domain.po.CourseCatalogue;
import com.shuai.message.domain.vo.CataSimpleInfoVO;

import java.util.List;

/**
 * <p>
 * 目录草稿 服务类
 * </p>
 *
 * @author Shuai
 * @since 2024-11-30
 */
public interface ICourseCatalogueService extends IService<CourseCatalogue> {

    List<CataSimpleInfoVO> batchQuery(List<Long> ids);
}
