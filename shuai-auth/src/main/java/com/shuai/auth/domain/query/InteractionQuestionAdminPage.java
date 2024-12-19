package com.shuai.auth.domain.query;

import com.shuai.common.domain.query.PageQuery;
import com.shuai.common.utils.DateUtils;
import com.shuai.common.validate.annotations.EnumValid;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class InteractionQuestionAdminPage extends PageQuery {

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "问题状态，1-已查看、0-未查看")
    @EnumValid(enumeration = {0, 1}, message = "问题状态只能是0或者1")
    private Integer status;

    @ApiModelProperty(value = "提问时间的最小值")
    /**
     * 使用 String 来接收 LocalDateTime 类型的数据，是为了增加时间格式的兼容性
     * @DateTimeFormat：这个注解会自动把字符串类型的数据，转换成设置的时间格式的LocalDateTime
     *
     *  在序列化与反序列化中的问题
     *      1、如果是 时间格式的字符串，在序列化与反序列化中不会出现任何问题
     *      2、如果是时间格式的，在json序列化中的存储类型是2022-08-18T19:52:36这样的
     *          要是json中2022-8-18 19:52:36 是这样的LocalDateTime类型，在反序列化中就会报错
     *          这是就需要 @JsonFormat(pattern = "yyyy-M-d H:m:s") 指定序列化与反序列化的格式
     */
    @DateTimeFormat(pattern = DateUtils.DEFAULT_DATE_TIME_FORMAT)
    private String beginTime;

    @ApiModelProperty(value = "提问时间的最大值")
    @DateTimeFormat(pattern = DateUtils.DEFAULT_DATE_TIME_FORMAT)
    private String endTime;
}
