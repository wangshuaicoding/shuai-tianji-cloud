package com.shuai.user.controller.remark;


import com.shuai.common.domain.R;
import com.shuai.user.domain.dto.LikedRecordFormDTO;
import com.shuai.user.service.ILikedRecordService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 点赞记录表 前端控制器
 * </p>
 *
 * @author Shuai
 * @since 2024-12-20
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikedRecordController {

    private final ILikedRecordService likedRecordService;

    @ApiOperation("用户可以给自己喜欢的内容点赞，也可以取消点赞")
    @PostMapping
    public R likeOrUnlikeRecord(@RequestBody @Valid LikedRecordFormDTO formDTO) {
        likedRecordService.likeOrUnlikeRecord(formDTO);
        return R.ok();
    }

    @ApiOperation("查询当前用户是否点赞了指定的业务")
    @GetMapping("/list")
    public Set<Long> isBizLiked(@RequestParam("bizIds") List<Long> bizIds) {
        return likedRecordService.isBizLiked(bizIds);
    }
}
