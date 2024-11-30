package com.shuai.api.client.course;

import com.shuai.api.dto.course.CataSimpleInfoDTO;
import com.shuai.common.constants.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "remoteCatalogueService", value = ServiceNameConstants.MESSAGE_SERVICE, path = "catalogues")
public interface CatalogueClient {

    @GetMapping("/batchQuery")
    List<CataSimpleInfoDTO> batchQuery(@RequestParam("ids") List<Long> ids);
}
