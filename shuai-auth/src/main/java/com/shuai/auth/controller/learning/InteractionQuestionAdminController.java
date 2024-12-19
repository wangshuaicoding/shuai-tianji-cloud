package com.shuai.auth.controller.learning;


import com.shuai.auth.domain.dto.QuestionsFormDTO;
import com.shuai.auth.domain.query.InteractionQuestionAdminPage;
import com.shuai.auth.domain.query.InteractionQuestionPage;
import com.shuai.auth.domain.vo.InteractionQuestionAdminVO;
import com.shuai.auth.domain.vo.InteractionQuestionVO;
import com.shuai.auth.service.IInteractionQuestionService;
import com.shuai.common.domain.R;
import com.shuai.common.domain.dto.PageDTO;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 互动提问的问题表 前端控制器
 * </p>
 *
 * @author Shuai
 * @since 2024-12-18
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/questions")
public class InteractionQuestionAdminController {

    private final IInteractionQuestionService questionService;

    @ApiOperation("管理端分页查询互动问题")
    @GetMapping("/page")
    public R<PageDTO<InteractionQuestionAdminVO>> queryInteractionQuestionAdminPage(InteractionQuestionAdminPage queryPage) {
        return R.ok(questionService.queryInteractionQuestionAdminPage(queryPage));
    }

    @ApiOperation("管理端隐藏或显示问题")
    @PutMapping("/{id}/hidden/{hidden}")
    public R hiddenQuestion(@PathVariable("id") Long id, @PathVariable("hidden") Boolean hidden) {
        questionService.hiddenQuestion(id, hidden);
        return R.ok();
    }

    @ApiOperation("管理端根据id查询问题详情")
    @GetMapping("/{id}")
    public R<InteractionQuestionAdminVO> queryQuestionById(@PathVariable("id") Long id) {
        return R.ok(questionService.queryAdminQuestionById(id));
    }

}
