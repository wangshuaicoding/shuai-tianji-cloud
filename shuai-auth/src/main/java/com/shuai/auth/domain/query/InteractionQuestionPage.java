package com.shuai.auth.domain.query;

import com.shuai.common.domain.query.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InteractionQuestionPage extends PageQuery {

    @ApiModelProperty(value = "是否只看自己的互动问题")
    private Boolean onlyMine;

    // 课程id 和小节id 至少存在一个
    @ApiModelProperty(value = "课程id")
    private Integer courseId;

    @ApiModelProperty(value = "小节id")
    private Integer sectionId;

}
