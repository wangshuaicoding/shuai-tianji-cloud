package com.shuai.auth.controller.learning;


import com.shuai.auth.domain.dto.QuestionsFormDTO;
import com.shuai.auth.domain.query.InteractionQuestionPage;
import com.shuai.auth.domain.vo.InteractionQuestionVO;
import com.shuai.auth.service.IInteractionQuestionService;
import com.shuai.common.domain.R;
import com.shuai.common.domain.dto.PageDTO;
import com.shuai.common.domain.query.PageQuery;
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
@RequestMapping("/questions")
public class InteractionQuestionController {

    private final IInteractionQuestionService questionService;

    @ApiOperation(value = "新增互动问题")
    @PostMapping
    public R addInteractionQuestion(@RequestBody QuestionsFormDTO formDTO) {
        questionService.addInteractionQuestion(formDTO);
        return R.ok();
    }

    @ApiOperation("修改互动问题")
    @PostMapping("/{id}")
    public R updateInteractionQuestion(@PathVariable("id") Long id, @RequestBody QuestionsFormDTO formDTO) {
        questionService.updateInteractionQuestion(id, formDTO);
        return R.ok();
    }

    @ApiOperation("分页查询问题（用户端）")
    @GetMapping("/page")
    public R<PageDTO<InteractionQuestionVO>> queryInteractionQuestionPage(InteractionQuestionPage pageQuery) {
        return R.ok(questionService.queryInteractionQuestionPage(pageQuery));
    }

    @ApiOperation("根据id查询问题详情")
    @GetMapping("/{id}")
    public R<InteractionQuestionVO> queryQuestionById(@PathVariable("id") Long id) {
        return R.ok(questionService.queryQuestionById(id));
    }

    @ApiOperation("删除互动问题")
    @DeleteMapping("/{id}")
    public R deleteQuestion(@PathVariable("id") Long id) {
        questionService.deleteQuestion(id);
        return R.ok();
    }
}
