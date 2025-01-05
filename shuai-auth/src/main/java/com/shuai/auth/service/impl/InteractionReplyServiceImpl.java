package com.shuai.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuai.api.client.user.UserClient;
import com.shuai.api.dto.user.UserDTO;
import com.shuai.auth.constants.InteractionQuestionConstants;
import com.shuai.auth.constants.InteractionReplyConstants;
import com.shuai.auth.domain.dto.InteractionReplyFormDTO;
import com.shuai.auth.domain.po.InteractionQuestion;
import com.shuai.auth.domain.po.InteractionReply;
import com.shuai.auth.domain.query.InteractionReplyPage;
import com.shuai.auth.domain.vo.InteractionReplyVO;
import com.shuai.auth.enums.QuestionStatus;
import com.shuai.auth.mapper.InteractionQuestionMapper;
import com.shuai.auth.mapper.InteractionReplyMapper;
import com.shuai.auth.service.IInteractionReplyService;
import com.shuai.common.domain.dto.PageDTO;
import com.shuai.common.exceptions.BizIllegalException;
import com.shuai.common.exceptions.DbException;
import com.shuai.common.utils.BeanUtils;
import com.shuai.common.utils.CollUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 互动问题的回答或评论 服务实现类
 * </p>
 *
 * @author Shuai
 * @since 2024-12-18
 */
@Service
@RequiredArgsConstructor
public class InteractionReplyServiceImpl extends ServiceImpl<InteractionReplyMapper, InteractionReply> implements IInteractionReplyService {

    private final InteractionQuestionMapper questionMapper;
    private final UserClient userClient;

    @Override
    @Transactional
    public void createInteractionReply(InteractionReplyFormDTO formDTO) {
        // Long userId = UserContext.getUser();
        Long userId = 2L;
        InteractionReply reply = BeanUtils.copyBean(formDTO, InteractionReply.class);
        reply.setUserId(userId);
        boolean success = save(reply);
        if (!success) {
            throw new DbException(InteractionReplyConstants.ADD_ANSWER_OR_COMMENT);
        }
        boolean flag = formDTO.getTargetReplyId() == null && formDTO.getTargetUserId() == null && formDTO.getAnswerId() == null;
        // 加上判断，不然如果两个都是false的话，更新sql语句不对
        if (flag || formDTO.getIsStudent()) {
            LambdaUpdateWrapper<InteractionQuestion> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper
                    .set(flag,InteractionQuestion::getLatestAnswerId, reply.getId())
                    .set(formDTO.getIsStudent(),InteractionQuestion::getStatus, QuestionStatus.NOT_VIEWED)
                    .setSql(flag,"answer_times = answer_times + 1")
                    .eq(InteractionQuestion::getId, formDTO.getQuestionId());
            int update = questionMapper.update(null, updateWrapper);

            if (update <= 0) {
                throw new DbException(InteractionQuestionConstants.FAILED_UPDATE_INTERACTION_ISSUE);
            }
        }

        if (!flag) {
            boolean update2 = lambdaUpdate()
                    .setSql("reply_times = reply_times + 1")
                    .eq(InteractionReply::getId, reply.getId())
                    .update();
            if (!update2) {
                throw new DbException(InteractionReplyConstants.UPDATE_ANSWER_OR_COMMENT);
            }
        }
    }

    @Override
    public PageDTO<InteractionReplyVO> queryInteractionReplyPage(InteractionReplyPage pageQuery) {
        if (pageQuery.getAnswerId() == null && pageQuery.getQuestionId() == null) {
            throw new BizIllegalException(InteractionReplyConstants.PARAMETER_IS_INCORRECT);
        }

        Page<InteractionReply> page = lambdaQuery()
                .eq(pageQuery.getAnswerId() != null, InteractionReply::getAnswerId, pageQuery.getAnswerId())
                .eq(pageQuery.getQuestionId() != null, InteractionReply::getQuestionId, pageQuery.getQuestionId())
                .page(new Page<>(pageQuery.getPageNo(), pageQuery.getPageSize()));

        List<InteractionReply> records = page.getRecords();
        if (CollUtils.isEmpty(records)) {
            return PageDTO.empty(page);
        }

        Set<Long> userIds = new HashSet<>();
        for (InteractionReply record : records) {
            userIds.add(record.getUserId());
            userIds.add(record.getTargetUserId());
        }

        List<UserDTO> userDTOS = userClient.queryUserByIds(userIds);
        Map<Long, UserDTO> uMap = null;
        if (CollUtils.isNotEmpty(userDTOS)) {
            uMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, c -> c));
        }

        List<InteractionReplyVO> result = new ArrayList<>(records.size());
        if (CollUtils.isNotEmpty(uMap)) {
            for (InteractionReply record : records) {
                InteractionReplyVO vo = BeanUtils.copyBean(records, InteractionReplyVO.class);
                UserDTO userDTO = uMap.get(record.getUserId());
                if (userDTO != null) {
                    vo.setUserName(userDTO.getName());
                    vo.setUserIcon(userDTO.getIcon());
                }
                UserDTO userDTO1 = uMap.get(record.getTargetUserId());
                if (userDTO1 != null) {
                    vo.setTargetUserName(userDTO1.getName());
                }
                result.add(vo);
            }
        }
        return PageDTO.of(page, result);
    }
}
