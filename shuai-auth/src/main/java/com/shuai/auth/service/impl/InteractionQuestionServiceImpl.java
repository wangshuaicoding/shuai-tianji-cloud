package com.shuai.auth.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shuai.api.cache.CategoryCache;
import com.shuai.api.client.course.CatalogueClient;
import com.shuai.api.client.course.CourseClient;
import com.shuai.api.client.user.UserClient;
import com.shuai.api.dto.course.CataSimpleInfoDTO;
import com.shuai.api.dto.course.CourseFullInfoDTO;
import com.shuai.api.dto.course.CourseSimpleInfoDTO;
import com.shuai.api.dto.user.UserDTO;
import com.shuai.auth.constants.InteractionQuestionConstants;
import com.shuai.auth.constants.InteractionReplyConstants;
import com.shuai.auth.domain.dto.QuestionsFormDTO;
import com.shuai.auth.domain.po.InteractionQuestion;
import com.shuai.auth.domain.po.InteractionReply;
import com.shuai.auth.domain.query.InteractionQuestionAdminPage;
import com.shuai.auth.domain.query.InteractionQuestionPage;
import com.shuai.auth.domain.vo.InteractionQuestionAdminVO;
import com.shuai.auth.domain.vo.InteractionQuestionVO;
import com.shuai.auth.mapper.InteractionQuestionMapper;
import com.shuai.auth.service.IInteractionQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuai.auth.service.IInteractionReplyService;
import com.shuai.common.domain.dto.PageDTO;
import com.shuai.common.exceptions.BizIllegalException;
import com.shuai.common.exceptions.DbException;
import com.shuai.common.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 互动提问的问题表 服务实现类
 * </p>
 *
 * @author Shuai
 * @since 2024-12-18
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InteractionQuestionServiceImpl extends ServiceImpl<InteractionQuestionMapper, InteractionQuestion> implements IInteractionQuestionService {

    private final IInteractionReplyService replyService;

    private final UserClient userClient;

    private final CourseClient courseClient;
    private final CatalogueClient catalogueClient;
    private final CategoryCache categoryCache;
    @Override
    public void addInteractionQuestion(QuestionsFormDTO formDTO) {
        Long userId = UserContext.getUser();
        InteractionQuestion interactionQuestion = BeanUtils.copyBean(formDTO, InteractionQuestion.class);
        interactionQuestion.setUserId(userId);
        boolean success = save(interactionQuestion);
        if (!success) {
            throw new DbException(InteractionQuestionConstants.FAILED_ADD_INTERACTION_ISSUE);
        }
    }

    @Override
    public void updateInteractionQuestion(Long id, QuestionsFormDTO formDTO) {

        InteractionQuestion one = lambdaQuery().eq(InteractionQuestion::getId, id).one();
        if (one == null) {
            throw new DbException(InteractionQuestionConstants.INTERACTION_PROBLEM_NOT_EXIST);
        }

        boolean success = lambdaUpdate()
                .set(StringUtils.isNotEmpty(formDTO.getTitle()), InteractionQuestion::getTitle, formDTO.getTitle())
                .set(StringUtils.isNotEmpty(formDTO.getDescription()), InteractionQuestion::getDescription, formDTO.getDescription())
                .set(BooleanUtils.isTrue(formDTO.getAnonymity()), InteractionQuestion::getAnonymity, true)
                .eq(InteractionQuestion::getId, id)
                .update();
        if (!success) {
            throw new DbException(InteractionQuestionConstants.FAILED_UPDATE_INTERACTION_ISSUE);
        }
    }

    @Override
    public PageDTO<InteractionQuestionVO> queryInteractionQuestionPage(InteractionQuestionPage pageQuery) {
        Integer courseId = pageQuery.getCourseId();
        Integer sectionId = pageQuery.getSectionId();
        if (ObjectUtils.isEmpty(courseId) && ObjectUtils.isEmpty(sectionId)) {
            throw new BizIllegalException("请求参数不合法");
        }

        // Long userId = UserContext.getUser();
        Long userId = 2L;

        Page<InteractionQuestion> page = lambdaQuery()
                .eq(ObjectUtils.isNotEmpty(courseId), InteractionQuestion::getCourseId, courseId)
                .eq(ObjectUtils.isNotEmpty(sectionId), InteractionQuestion::getSectionId, sectionId)
                .eq(pageQuery.getOnlyMine(), InteractionQuestion::getUserId, userId)
                .eq(InteractionQuestion::getHidden, false)
                .page(new Page<>(pageQuery.getPageNo(), pageQuery.getPageSize()));

        List<InteractionQuestion> records = page.getRecords();
        if (CollUtils.isEmpty(records)) {
            return PageDTO.empty(page);
        }

        List<InteractionQuestionVO> result = new ArrayList<>(records.size());
        for (InteractionQuestion record : records) {
            InteractionQuestionVO vo = BeanUtils.copyBean(record, InteractionQuestionVO.class);
            result.add(vo);
        }

        // 收集最近一次回答的id
        Set<Long> latestAnswerIds = records.stream().map(InteractionQuestion::getLatestAnswerId).collect(Collectors.toSet());
        // 查询最后一次回答的信息
        List<InteractionReply> replyList = replyService.getBaseMapper().selectBatchIds(latestAnswerIds);
        if (CollUtils.isEmpty(replyList)) {
            return PageDTO.of(page, result);
        }
        Map<Long, InteractionReply> rMap = replyList.stream().collect(Collectors.toMap(InteractionReply::getId, c -> c));

        // 收集问题表中的用户id
        Set<Long> userIds = records.stream().map(InteractionQuestion::getUserId).collect(Collectors.toSet());
        // 收集回答表中的用户id
        Set<Long> replyUserIds = replyList.stream().map(InteractionReply::getUserId).collect(Collectors.toSet());
        userIds.addAll(replyUserIds);

        // 远程批量查询用户信息
        List<UserDTO> userDTOS = userClient.queryUserByIds(userIds);
        if (CollUtils.isEmpty(userDTOS)) {
            log.warn("远程批量查询用户信息为空");
            return PageDTO.of(page, result);
        }

        Map<Long, UserDTO> userMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, c -> c));
        for (InteractionQuestionVO vo : result) {
            UserDTO userDTO = userMap.get(vo.getUserId());
            if (userDTO != null) {
                vo.setUserName(userDTO.getName());
                vo.setUserIcon(userDTO.getIcon());
            }

            InteractionReply reply = rMap.get(vo.getLatestAnswerId());
            if (reply != null) {
                vo.setLatestReplyContent(reply.getContent());
                vo.setLatestReplyUser(userMap.get(reply.getUserId()).getName());
            }
        }
        return PageDTO.of(page, result);
    }

    @Override
    public InteractionQuestionVO queryQuestionById(Long id) {

        InteractionQuestion question = baseMapper.selectById(id);
        if (question == null) {
            throw new DbException(InteractionQuestionConstants.INTERACTION_PROBLEM_NOT_EXIST);
        }

        InteractionQuestionVO vo = BeanUtils.copyBean(question, InteractionQuestionVO.class);

        // 远程调用user的feign接口
        UserDTO userDTO = userClient.queryUserById(question.getUserId());
        if (userDTO != null) {
            vo.setUserName(userDTO.getName());
            vo.setUserIcon(userDTO.getIcon());
        }
        return vo;
    }

    @Override
    @Transactional
    public void deleteQuestion(Long id) {
        // 删除评论表中的数据
        boolean success = replyService.lambdaUpdate().eq(InteractionReply::getQuestionId, id).remove();
        if (!success) {
            throw new DbException(InteractionReplyConstants.FAILED_DELETE_COMMENT);
        }

        int result = baseMapper.deleteById(id);
        if (result <= 0) {
            throw new DbException(InteractionQuestionConstants.FAILED_DELETE_INTERACTION_ISSUE);
        }
    }

    @Override
    public PageDTO<InteractionQuestionAdminVO> queryInteractionQuestionAdminPage(InteractionQuestionAdminPage queryPage) {

        // 查询问题指定条件的问题
        String courseName = queryPage.getCourseName();
        Integer status = queryPage.getStatus();
        String begin = queryPage.getBeginTime();
        String end = queryPage.getEndTime();

        Page<InteractionQuestion> page = lambdaQuery()
                .eq(status != null, InteractionQuestion::getStatus, status)
                .gt(ObjectUtils.isNotEmpty(begin), InteractionQuestion::getCreateTime, begin)
                .lt(ObjectUtils.isNotEmpty(end), InteractionQuestion::getCreateTime, end)
                .page(new Page<>(queryPage.getPageNo(), queryPage.getPageSize()));

        List<InteractionQuestion> records = page.getRecords();
        if (CollUtils.isEmpty(records)) {
            return PageDTO.empty(page);
        }

        // 封装userId
        Set<Long> userIds = records.stream().map(InteractionQuestion::getUserId).collect(Collectors.toSet());
        Map<Long, String> uMap = null;
        if (CollUtils.isNotEmpty(userIds)) {
            List<UserDTO> userDTOS = userClient.queryUserByIds(userIds);
            if (CollUtils.isNotEmpty(userDTOS)) {
                uMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
            }
        }

        // todo courseIds 应该去elasticsearch中查询
        // 收集封装courseId
        Set<Long> courseIds = records.stream().map(InteractionQuestion::getCourseId).collect(Collectors.toSet());
        Map<Long, CourseSimpleInfoDTO> cMap = null;
        if (CollUtils.isNotEmpty(courseIds)) {
            List<CourseSimpleInfoDTO> courseDTOList = courseClient.selectListByIds(courseIds);
            if (CollUtils.isNotEmpty(courseDTOList)) {
                cMap = courseDTOList.stream().collect(Collectors.toMap(CourseSimpleInfoDTO::getId, c -> c));
            }
        }

        // 收集小节id和章id
        List<Long> lists = new ArrayList<>();
        for (InteractionQuestion record : records) {
            lists.add(record.getChapterId());
            lists.add(record.getSectionId());
        }

        Map<Long, String> caMap = null;
        if (CollUtils.isNotEmpty(lists)) {
            List<CataSimpleInfoDTO> cataDTOList = catalogueClient.batchQuery(lists);
            if (CollUtils.isNotEmpty(cataDTOList)) {
                caMap = cataDTOList.stream().collect(Collectors.toMap(CataSimpleInfoDTO::getId, CataSimpleInfoDTO::getName));
            }
        }

        // 封装 vo 返回
        List<InteractionQuestionAdminVO> result = new ArrayList<>(records.size());
        for (InteractionQuestion record : records) {
            InteractionQuestionAdminVO vo = BeanUtils.copyBean(record, InteractionQuestionAdminVO.class);
            if (uMap != null) {
                vo.setUserName(uMap.get(record.getUserId()));
            }
            if (cMap != null) {
                vo.setCourseName(cMap.get(record.getCourseId()).getName());
                vo.setCategoryName(categoryCache.getCategoryName(cMap.get(record.getCourseId()).getCategoryIds()));
            }
            if (caMap != null) {
                vo.setCategoryName(caMap.get(record.getChapterId()));
                vo.setSectionName(caMap.get(record.getSectionId()));
            }
            result.add(vo);
        }
        return PageDTO.of(page,result);
    }

    @Override
    public void hiddenQuestion(Long id, Boolean hidden) {
        boolean success = lambdaUpdate()
                .set(InteractionQuestion::getHidden, hidden)
                .eq(InteractionQuestion::getId, id)
                .update();

        if (!success) {
            throw new DbException(InteractionQuestionConstants.FAILED_HIDE_OR_SHOW_PROBLEM);
        }
    }

    @Override
    public InteractionQuestionAdminVO queryAdminQuestionById(Long id) {
        InteractionQuestion question = baseMapper.selectById(id);
        if (question == null) {
            throw new DbException(InteractionQuestionConstants.INTERACTION_PROBLEM_NOT_EXIST);
        }

        InteractionQuestionAdminVO vo = BeanUtils.copyBean(question, InteractionQuestionAdminVO.class);
        List<Long> userIds = new ArrayList<>();
        userIds.add(question.getUserId());

        // 查询课程信息
        CourseFullInfoDTO courseDTO = courseClient.selectFullInfoById(question.getCourseId());
        if (courseDTO != null) {
            vo.setCourseName(courseDTO.getName());
            vo.setCategoryName(categoryCache.getCategoryName(courseDTO.getCategoryIds()));
            userIds.addAll(courseDTO.getTeacherIds());
            List<UserDTO> userDTOS = userClient.queryUserByIds(userIds);
            if (CollUtils.isNotEmpty(userDTOS)) {
                Map<Long, String> cMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
                vo.setUserName(cMap.getOrDefault(question.getUserId(),""));
                vo.setTeacherName(cMap.getOrDefault(courseDTO.getTeacherIds().get(0),""));
            }
        }

        // 封装章节信息
        List<CataSimpleInfoDTO> cataDTOList = catalogueClient.batchQuery(Arrays.asList(question.getChapterId(), question.getSectionId()));
        Map<Long, String> cMap = null;
        if (CollUtils.isNotEmpty(cataDTOList)) {
            cMap = cataDTOList.stream().collect(Collectors.toMap(CataSimpleInfoDTO::getId, CataSimpleInfoDTO::getName));
        }
        if (cMap != null) {
            vo.setChapterName(cMap.getOrDefault(question.getChapterId(),""));
            vo.setSectionName(cMap.getOrDefault(question.getSectionId(),""));
        }

        return vo;
    }
}
