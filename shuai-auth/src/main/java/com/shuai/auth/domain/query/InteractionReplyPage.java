package com.shuai.auth.domain.query;

import com.shuai.common.domain.query.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InteractionReplyPage extends PageQuery {

    // 问题id 和回答id 至少存在一个
    @ApiModelProperty(value = "问题id")
    private Long questionId;

    @ApiModelProperty(value = "回答id")
    private Long answerId;

}
