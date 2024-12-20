package com.shuai.user.domain.dto;

import com.shuai.common.validate.annotations.EnumValid;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LikedRecordFormDTO {
    @ApiModelProperty(value = "点赞业务id")
    private Long bizId;

    @ApiModelProperty(value = "点赞的业务类型,1:回答，2：笔记")
    private String bizType;

    @ApiModelProperty(value = "是否点赞，true:点赞，false：取消")
    private Boolean liked;
}
