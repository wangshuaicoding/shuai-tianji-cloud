package com.shuai.auth.service;

import com.shuai.auth.domain.po.SignRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shuai.auth.domain.vo.SignRecordVO;

/**
 * <p>
 * 签到记录表 服务类
 * </p>
 *
 * @author Shuai
 * @since 2024-12-24
 */
public interface ISignRecordService extends IService<SignRecord> {

    SignRecordVO addSignRecord();
}
