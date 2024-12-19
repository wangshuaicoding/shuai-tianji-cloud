package com.shuai.auth.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InteractionQuestionVO {
    @ApiModelProperty(value = "主键，互动问题的id")
    private Long id;

    @ApiModelProperty(value = "互动问题的标题")
    private String title;

    @ApiModelProperty(value = "提问学员id")
    private Long userId;

    @ApiModelProperty(value = "问题的描述")
    private String description;

    @ApiModelProperty(value = "问题下的回答数量")
    private Integer answerTimes;

    @ApiModelProperty(value = "是否匿名，默认false")
    private Boolean anonymity;

    @ApiModelProperty(value = "提问时间")
    private LocalDateTime createTime;

    // 其他表中的内容
    @ApiModelProperty(value = "提问者昵称")
    private String userName;

    @ApiModelProperty(value = "提问者头像")
    private String userIcon;

    @ApiModelProperty(value = "最近一次回答的内容")
    private String latestReplyContent;

    @ApiModelProperty(value = "最近一次回答的用户昵称")
    private String latestReplyUser;

    @JsonIgnore
    @ApiModelProperty(value = "最新的一个回答的id")
    private Long latestAnswerId;

}
