package com.shuai.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shuai.auth.domain.po.PointsBoard;
import com.shuai.auth.domain.query.PointsBoardQuery;
import com.shuai.auth.domain.vo.PointsBoardVO;

import java.util.List;

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

    List<PointsBoard> queryCurrentBoardList(String key, Integer pageNo, Integer pageSize);
}
