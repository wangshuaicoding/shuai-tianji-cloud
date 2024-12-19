package com.shuai.auth.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InteractionReplyVO {

    @ApiModelProperty(value = "回答id")
    private Long id;

    @ApiModelProperty(value = "回答者id")
    private Long userId;

    @ApiModelProperty(value = "回答内容")
    private String content;

    @ApiModelProperty(value = "评论数量")
    private Integer replyTimes;

    @ApiModelProperty(value = "点赞数量")
    private Integer likedTimes;

    @ApiModelProperty(value = "是否匿名，默认false")
    private Boolean anonymity;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    // 用户表
    @ApiModelProperty(value = "回答者昵称")
    private String userName;

    @ApiModelProperty(value = "回答者头像")
    private String userIcon;

    @ApiModelProperty(value = "目标用户昵称")
    private String targetUserName;

}
