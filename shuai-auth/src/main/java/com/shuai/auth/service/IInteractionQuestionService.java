package com.shuai.auth.service;

import com.shuai.auth.domain.dto.QuestionsFormDTO;
import com.shuai.auth.domain.po.InteractionQuestion;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shuai.auth.domain.query.InteractionQuestionAdminPage;
import com.shuai.auth.domain.query.InteractionQuestionPage;
import com.shuai.auth.domain.vo.InteractionQuestionAdminVO;
import com.shuai.auth.domain.vo.InteractionQuestionVO;
import com.shuai.common.domain.dto.PageDTO;

/**
 * <p>
 * 互动提问的问题表 服务类
 * </p>
 *
 * @author Shuai
 * @since 2024-12-18
 */
public interface IInteractionQuestionService extends IService<InteractionQuestion> {

    void addInteractionQuestion(QuestionsFormDTO formDTO);

    void updateInteractionQuestion(Long id, QuestionsFormDTO formDTO);

    PageDTO<InteractionQuestionVO> queryInteractionQuestionPage(InteractionQuestionPage pageQuery);

    InteractionQuestionVO queryQuestionById(Long id);

    void deleteQuestion(Long id);

    PageDTO<InteractionQuestionAdminVO> queryInteractionQuestionAdminPage(InteractionQuestionAdminPage queryPage);

    void hiddenQuestion(Long id, Boolean hidden);

    InteractionQuestionAdminVO queryAdminQuestionById(Long id);
}
