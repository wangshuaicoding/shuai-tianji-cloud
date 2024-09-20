package com.shuai.auth.task;

import cn.hutool.core.collection.CollectionUtil;
import com.shuai.auth.service.IPrivilegeService;
import com.shuai.auth.util.PrivilegeCache;
import com.shuai.common.domain.dto.PrivilegeRoleDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class LoadPrivilegeRunner{

    private final IPrivilegeService privilegeService;
    private final PrivilegeCache privilegeCache;

    /**
     * 初始化操作：@PostConstruct 注解的方法主要用于执行一些初始化操作，例如打开资源、加载配置等。
     * 执行时机
     *  依赖注入完成后：在 Spring 容器完成依赖注入之后。
     *  Bean 使用之前：在 Bean 被任何其他 Bean 使用之前。
     */
    @PostConstruct
    public void loadPrivilegeCache(){
        try {
            log.trace("开始更新权限缓存数据");
            // 1.查询数据
            List<PrivilegeRoleDTO> privilegeRoleDTOS = privilegeService.listPrivilegeRoles();
            if (CollectionUtil.isEmpty(privilegeRoleDTOS)) {
                return;
            }
            // 2.缓存
            privilegeCache.initPrivilegesCache(privilegeRoleDTOS);
            log.trace("更新权限缓存数据成功！");
        }catch (Exception e){
            log.error("更新权限缓存数据失败！原因：{}", e.getMessage());
        }
    }
}
