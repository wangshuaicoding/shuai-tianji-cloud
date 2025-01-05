package com.shuai.api.client.course.fallback;

import com.shuai.api.client.course.CourseClient;
import com.shuai.api.client.user.UserClient;
import com.shuai.api.dto.course.CourseFullInfoDTO;
import com.shuai.api.dto.course.CourseSimpleInfoDTO;
import com.shuai.api.dto.user.LoginFormDTO;
import com.shuai.api.dto.user.UserDTO;
import com.shuai.common.domain.R;
import com.shuai.common.domain.dto.LoginUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.Collections;
import java.util.List;

/**
 * 用户服务降级处理
 */
@Slf4j
public class CourseClientFallback implements FallbackFactory<CourseClient> {
    @Override
    public CourseClient create(Throwable throwable) {
        log.error("触发降级处理，查询用户服务出现异常:{}", throwable.getMessage());
        return new CourseClient() {
            @Override
            public List<CourseSimpleInfoDTO> selectListByIds(Iterable<Long> ids) {
                log.error("根据课程ids批量查询课程服务出现异常:{}", throwable.getMessage());
                return null;
            }

            @Override
            public CourseFullInfoDTO selectFullInfoById(Long id) {
                return null;
            }
        };
    }
}
