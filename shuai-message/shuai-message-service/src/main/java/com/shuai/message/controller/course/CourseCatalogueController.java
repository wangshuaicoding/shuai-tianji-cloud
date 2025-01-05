package com.shuai.message.controller.course;


import com.shuai.common.domain.R;
import com.shuai.message.domain.po.CourseCatalogue;
import com.shuai.message.domain.vo.CataSimpleInfoVO;
import com.shuai.message.service.ICourseCatalogueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 目录草稿 前端控制器
 * </p>
 *
 * @author Shuai
 * @since 2024-11-30
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/catalogues")
public class CourseCatalogueController {

    private final ICourseCatalogueService courseCatalogueService;

    /**
     * 远程feign传过来的参数是CataSimpleInfoDTO与CataSimpleInfoVO名称不一样，但是里面的属性是包含关系
     * @param ids
     * @return
     */
    @GetMapping("/batchQuery")
    public List<CataSimpleInfoVO> batchQuery(@RequestParam("ids") List<Long> ids) {
        return courseCatalogueService.batchQuery(ids);
    }
}
