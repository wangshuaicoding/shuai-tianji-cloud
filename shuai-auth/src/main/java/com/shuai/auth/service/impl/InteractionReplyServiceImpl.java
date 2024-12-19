package com.shuai.auth.service.impl;

import com.shuai.auth.domain.po.InteractionReply;
import com.shuai.auth.mapper.InteractionReplyMapper;
import com.shuai.auth.service.IInteractionReplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 互动问题的回答或评论 服务实现类
 * </p>
 *
 * @author Shuai
 * @since 2024-12-18
 */
@Service
public class InteractionReplyServiceImpl extends ServiceImpl<InteractionReplyMapper, InteractionReply> implements IInteractionReplyService {

}
