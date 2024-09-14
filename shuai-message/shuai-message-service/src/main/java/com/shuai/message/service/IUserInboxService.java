package com.shuai.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shuai.api.dto.user.UserDTO;
import com.shuai.common.domain.dto.PageDTO;
import com.shuai.message.domain.dto.UserInboxDTO;
import com.shuai.message.domain.dto.UserInboxFormDTO;
import com.shuai.message.domain.po.NoticeTemplate;
import com.shuai.message.domain.po.UserInbox;
import com.shuai.message.domain.query.UserInboxQuery;

import java.util.List;

/**
 * <p>
 * 用户通知记录 服务类
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-19
 */
public interface IUserInboxService extends IService<UserInbox> {

    void saveNoticeToInbox(NoticeTemplate noticeTemplate, List<UserDTO> users);

    PageDTO<UserInboxDTO> queryUserInBoxesPage(UserInboxQuery query);

    Long sentMessageToUser(UserInboxFormDTO userInboxFormDTO);
}
