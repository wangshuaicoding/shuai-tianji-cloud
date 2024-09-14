package com.shuai.message.thirdparty;


import com.shuai.message.domain.dto.SmsInfoDTO;
import com.shuai.message.domain.po.MessageTemplate;

/**
 * 第三方接口对接平台
 */
public interface ISmsHandler {

    /**
     * 发送短信
     */
    void send(SmsInfoDTO platformSmsInfoDTO, MessageTemplate template);


}
