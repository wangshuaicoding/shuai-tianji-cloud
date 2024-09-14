package com.shuai.message.thirdparty.uc;

import com.shuai.message.domain.dto.SmsInfoDTO;
import com.shuai.message.domain.po.MessageTemplate;
import com.shuai.message.thirdparty.ISmsHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * uCloud平台短信发送功能
 */
@Service("uCloud")
@Slf4j
public class UcSmsHandler implements ISmsHandler {
    @Override
    public void send(SmsInfoDTO platformSmsInfoDTO, MessageTemplate template) {
        //第三方发送短信验证码
        log.info("短信发送成功 ...");
        log.info("platformSmsInfoDTO：{}", platformSmsInfoDTO);
    }
}