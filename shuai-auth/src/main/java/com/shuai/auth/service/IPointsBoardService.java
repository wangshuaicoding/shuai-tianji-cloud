package com.shuai.auth.service;

import com.shuai.auth.domain.po.PointsBoard;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shuai.auth.domain.query.PointsBoardQuery;
import com.shuai.auth.domain.vo.PointsBoardVO;
import com.shuai.common.domain.dto.PageDTO;

/**
 * <p>
 * 学霸天梯榜 服务类
 * </p>
 *
 * @author Shuai
 * @since 2024-12-26
 */
public interface IPointsBoardService extends IService<PointsBoard> {

    PointsBoardVO queryPointsBoard(PointsBoardQuery query);

    void createPointsBoardTable(Integer season);
}
