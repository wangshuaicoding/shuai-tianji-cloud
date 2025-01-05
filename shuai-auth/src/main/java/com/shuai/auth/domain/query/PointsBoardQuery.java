package com.shuai.auth.domain.query;

import com.shuai.common.domain.query.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PointsBoardQuery extends PageQuery {

    @ApiModelProperty(value = "赛季id,为null或者0则代表查询当前赛季")
    private Long season;
}
