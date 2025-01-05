package com.shuai.api.client.remark;

import com.shuai.api.client.remark.fallback.RemarkClientFallback;
import com.shuai.common.constants.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(contextId = "remoteRemarkService", value = ServiceNameConstants.USER_SERVICE, fallbackFactory = RemarkClientFallback.class)
public interface RemarkClient {

    @GetMapping("/likes/list")
    Set<Long> isBizLiked(@RequestParam("bizIds") List<Long> bizIds);

}
