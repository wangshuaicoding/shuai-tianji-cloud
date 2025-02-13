package com.shuai.api.client.remark.fallback;

import com.shuai.api.client.remark.RemarkClient;
import com.shuai.common.utils.CollUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;
import java.util.Set;

@Slf4j
public class RemarkClientFallback implements FallbackFactory<RemarkClient> {

    @Override
    public RemarkClient create(Throwable cause) {
        log.error("查询remark-service服务异常，",cause);
        return new RemarkClient() {
            @Override
            public Set<Long> isBizLiked(List<Long> bizIds) {
                return CollUtils.emptySet();
            }
        };
    }
}
