package com.shuai.user.controller;


import com.shuai.common.domain.R;
import com.shuai.user.domain.dto.LikedRecordFormDTO;
import com.shuai.user.service.ILikedRecordService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
}
