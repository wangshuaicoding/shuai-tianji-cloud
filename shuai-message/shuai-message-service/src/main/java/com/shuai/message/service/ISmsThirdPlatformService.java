package com.shuai.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shuai.common.domain.dto.PageDTO;
import com.shuai.message.domain.dto.SmsThirdPlatformDTO;
import com.shuai.message.domain.dto.SmsThirdPlatformFormDTO;
import com.shuai.message.domain.po.SmsThirdPlatform;
import com.shuai.message.domain.query.SmsThirdPlatformPageQuery;

import java.util.List;

/**
 * <p>
 * 第三方云通讯平台 服务类
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-19
 */
public interface ISmsThirdPlatformService extends IService<SmsThirdPlatform> {

    List<SmsThirdPlatform> queryAllPlatform();

    Long saveSmsThirdPlatform(SmsThirdPlatformFormDTO thirdPlatformDTO);

    void updateSmsThirdPlatform(SmsThirdPlatformFormDTO thirdPlatformDTO);

    PageDTO<SmsThirdPlatformDTO> querySmsThirdPlatforms(SmsThirdPlatformPageQuery query);

    SmsThirdPlatformDTO querySmsThirdPlatform(Long id);
}
