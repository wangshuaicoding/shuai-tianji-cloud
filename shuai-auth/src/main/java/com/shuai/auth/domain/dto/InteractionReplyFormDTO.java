package com.shuai.auth.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InteractionReplyFormDTO {

    @ApiModelProperty(value = "问题id")
    private Long questionId;

    @ApiModelProperty(value = "回答的内容")
    private String content;

    @ApiModelProperty(value = "是否匿名")
    private Boolean anonymity;

    @ApiModelProperty(value = "上级回答id,没有可不填")
    private Long answerId;

    @ApiModelProperty(value = "目标回复id,没有可不填")
    private Long targetReplyId;

    @ApiModelProperty(value = "目标用户id,没有可不填")
    private Long targetUserId;

    @ApiModelProperty(value = "标记当前回复是否是学生提交的")
    private Boolean isStudent;
}
