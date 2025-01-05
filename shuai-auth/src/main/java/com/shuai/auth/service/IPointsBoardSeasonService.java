package com.shuai.auth.service;

import com.shuai.auth.domain.po.PointsBoardSeason;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;

/**
 * <p>
 * 赛季表 服务类
 * </p>
 *
 * @author Shuai
 * @since 2024-12-26
 */
public interface IPointsBoardSeasonService extends IService<PointsBoardSeason> {

    Integer querySeasonByTime(LocalDateTime time);
}
