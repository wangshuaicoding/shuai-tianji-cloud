package com.shuai.auth.service;

import com.shuai.auth.domain.dto.InteractionReplyFormDTO;
import com.shuai.auth.domain.po.InteractionReply;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shuai.auth.domain.query.InteractionReplyPage;
import com.shuai.auth.domain.vo.InteractionReplyVO;
import com.shuai.common.domain.dto.PageDTO;

/**
 * <p>
 * 互动问题的回答或评论 服务类
 * </p>
 *
 * @author Shuai
 * @since 2024-12-18
 */
public interface IInteractionReplyService extends IService<InteractionReply> {

    void createInteractionReply(InteractionReplyFormDTO formDTO);

    PageDTO<InteractionReplyVO> queryInteractionReplyPage(InteractionReplyPage pageQuery);
}
