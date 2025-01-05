package com.shuai.api.dto.remark;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
// 指定静态构造函数名称为of,进一步减少样本代码
@AllArgsConstructor(staticName = "of")
public class LikedTimesDTO {
    private Long bizId;
    private Integer likedTimes;
}
