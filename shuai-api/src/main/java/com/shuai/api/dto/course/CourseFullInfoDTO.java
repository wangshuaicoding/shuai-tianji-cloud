package com.shuai.api.dto.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Data
public class CourseFullInfoDTO {

    @ApiModelProperty(value = "课程草稿id，对应正式草稿id")
    private Long id;

    @ApiModelProperty(value = "课程名称")
    private String name;

    @ApiModelProperty(value = "课程类型，1：直播课，2：录播课")
    private Integer courseType;

    @ApiModelProperty(value = "封面链接")
    private String coverUrl;

    @ApiModelProperty(value = "一级课程分类id")
    private Long firstCateId;

    @ApiModelProperty(value = "二级课程分类id")
    private Long secondCateId;

    @ApiModelProperty(value = "三级课程分类id")
    private Long thirdCateId;

    @ApiModelProperty(value = "售卖方式0付费，1：免费")
    private Integer free;

    @ApiModelProperty(value = "课程价格，单位为分")
    private Integer price;

    @ApiModelProperty(value = "模板类型，1：固定模板，2：自定义模板")
    private Integer templateType;

    @ApiModelProperty(value = "自定义模板的连接")
    private String templateUrl;

    @ApiModelProperty(value = "课程状态，1：待上架，2：已上架，3：下架，4：已完结")
    private Integer status;

    @ApiModelProperty(value = "课程购买有效期开始时间")
    private LocalDateTime purchaseStartTime;

    @ApiModelProperty(value = "课程购买有效期结束时间")
    private LocalDateTime purchaseEndTime;

    @ApiModelProperty(value = "信息填写进度")
    private Integer step;

    @ApiModelProperty(value = "课程评价得分，45代表4.5星")
    private Integer score;

    @ApiModelProperty(value = "课程总时长")
    private Integer mediaDuration;

    @ApiModelProperty(value = "课程有效期，单位月")
    private Integer validDuration;

    @ApiModelProperty(value = "课程总节数，包括练习")
    private Integer sectionNum;

    @ApiModelProperty(value = "部门id")
    private Long depId;

    @ApiModelProperty(value = "发布次数")
    private Integer publishTimes;

    @ApiModelProperty(value = "最近一次发布时间")
    private LocalDateTime publishTime;

    @ApiModelProperty("老师列表")
    private List<Long> teacherIds;

    @JsonIgnore
    public List<Long> getCategoryIds() {
        return Arrays.asList(firstCateId, secondCateId, thirdCateId);
    }
}
