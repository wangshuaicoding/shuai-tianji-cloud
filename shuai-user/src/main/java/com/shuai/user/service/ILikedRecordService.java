package com.shuai.user.service;

import com.shuai.user.domain.dto.LikedRecordFormDTO;
import com.shuai.user.domain.po.LikedRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 点赞记录表 服务类
 * </p>
 *
 * @author Shuai
 * @since 2024-12-20
 */
public interface ILikedRecordService extends IService<LikedRecord> {

    void likeOrUnlikeRecord(LikedRecordFormDTO formDTO);

    Set<Long> isBizLiked(List<Long> bizIds);

    void readLikedTimesAndSendMessage(String bizType, int maxBizSize);
}
