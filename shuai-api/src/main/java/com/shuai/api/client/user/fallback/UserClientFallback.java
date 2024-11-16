package com.shuai.api.client.user.fallback;

import com.shuai.api.client.user.UserClient;
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
public class UserClientFallback implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable throwable) {
        log.error("触发降级处理，查询用户服务出现异常:{}", throwable.getMessage());
        return new UserClient() {
            @Override
            public Long exchangeUserIdWithPhone(String phone) {
                return null;
            }

            @Override
            public R<LoginUserDTO> queryUserDetail(LoginFormDTO loginDTO, boolean isStaff) {
                return R.error("获得教师详情失败");
            }

            @Override
            public Integer queryUserType(Long id) {
                return null;
            }

            @Override
            public List<UserDTO> queryUserByIds(Iterable<Long> ids) {
                return Collections.emptyList();
            }

            @Override
            public UserDTO queryUserById(Long id) {
                return null;
            }
        };
    }
}
