package com.shuai.auth.controller.learning;


import com.shuai.auth.domain.dto.InteractionReplyFormDTO;
import com.shuai.auth.domain.query.InteractionReplyPage;
import com.shuai.auth.domain.vo.InteractionReplyVO;
import com.shuai.auth.service.IInteractionReplyService;
import com.shuai.common.domain.R;
import com.shuai.common.domain.dto.PageDTO;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 互动问题的回答或评论 前端控制器
 * </p>
 *
 * @author Shuai
 * @since 2024-12-18
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/replies")
public class InteractionReplyController {

    private final IInteractionReplyService replyService;

    @ApiOperation("新增回答或评论")
    @PostMapping
    public R createInteractionReply(@RequestBody InteractionReplyFormDTO formDTO) {
        replyService.createInteractionReply(formDTO);
        return R.ok();
    }

    @ApiOperation("分页查询回答或评论列表")
    @GetMapping("/page")
    public R<PageDTO<InteractionReplyVO>> queryInteractionReplyPage(InteractionReplyPage pageQuery) {
        return R.ok(replyService.queryInteractionReplyPage(pageQuery));
    }
}
