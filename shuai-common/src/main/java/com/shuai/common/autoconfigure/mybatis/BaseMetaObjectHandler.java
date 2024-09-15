package com.shuai.common.autoconfigure.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.shuai.common.domain.po.BaseEntity;
import com.shuai.common.utils.UserContext;
import org.apache.ibatis.reflection.MetaObject;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 通用参数填充实现类
 * 如果没有显式的对通用参数进行赋值，这里会对通用参数进行填充、赋值
 */
public class BaseMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity) {
            BaseEntity base = (BaseEntity) metaObject.getOriginalObject();

            LocalDateTime current = LocalDateTime.now();
            // 创建时间为空，则以当前时间为插入时间
            if (Objects.isNull(base.getCreateTime())) {
                base.setCreateTime(current);
            }
            // 更新时间为空，则以当前时间为更新时间
            if (Objects.isNull(base.getUpdateTime())) {
                base.setUpdateTime(current);
            }

            Long userId = UserContext.getUser();
            // 当前登录用户不为空，创建人为空，则当前登录用户为创建人
            if (Objects.nonNull(userId) && Objects.isNull(base.getCreater())) {
                base.setCreater(userId.toString());
            }
            // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
            if (Objects.nonNull(userId) && Objects.isNull(base.getUpdater())) {
                base.setUpdater(userId.toString());
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新操作:当前时间为更新时间
        setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        // 当前的userId为更新人
        Long userId = UserContext.getUser();
        setFieldValByName("updater", userId.toString(), metaObject);
    }
}
