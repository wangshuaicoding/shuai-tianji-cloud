package com.shuai.message.service;


import com.shuai.api.dto.user.UserDTO;
import com.shuai.message.domain.dto.SmsInfoDTO;
import com.shuai.message.domain.po.NoticeTemplate;

import java.util.List;

public interface ISmsService {
    void sendMessageByTemplate(NoticeTemplate noticeTemplate, List<UserDTO> users);

    void sendMessage(SmsInfoDTO smsInfoDTO);

    void sendMessageAsync(SmsInfoDTO smsInfoDTO);
}
