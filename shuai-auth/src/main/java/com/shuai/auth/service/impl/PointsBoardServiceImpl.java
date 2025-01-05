package com.shuai.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuai.api.client.user.UserClient;
import com.shuai.api.dto.user.UserDTO;
import com.shuai.auth.constants.LessonConstants;
import com.shuai.auth.constants.RedisConstants;
import com.shuai.auth.domain.po.PointsBoard;
import com.shuai.auth.domain.query.PointsBoardQuery;
import com.shuai.auth.domain.vo.PointsBoardItemVO;
import com.shuai.auth.domain.vo.PointsBoardVO;
import com.shuai.auth.mapper.PointsBoardMapper;
import com.shuai.auth.service.IPointsBoardService;
import com.shuai.common.utils.CollUtils;
import com.shuai.common.utils.DateUtils;
import com.shuai.common.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 学霸天梯榜 服务实现类
 * </p>
 *
 * @author Shuai
 * @since 2024-12-26
 */
@Service
@RequiredArgsConstructor
public class PointsBoardServiceImpl extends ServiceImpl<PointsBoardMapper, PointsBoard> implements IPointsBoardService {

    private final StringRedisTemplate redisTemplate;

    private final UserClient userClient;

    @Override
    public PointsBoardVO queryPointsBoard(PointsBoardQuery query) {

        // 判断是否是当前赛季
        Long season = query.getSeason();
        boolean isCurrent = season == null || season == 0;

        // 去redis 中获取key
        LocalDateTime now = LocalDateTime.now();
        String key = RedisConstants.POINTS_BOARD_KEY_PREFIX + now.format(DateUtils.POINTS_BOARD_SUFFIX_FORMATTER);
        // 2、查询我的积分和排名
        PointsBoard myBoard = isCurrent ?
                queryMyCurrentBoard(key) :  // 查询当前榜单（redis）
                queryMyHistoryBoard(season);  // 查询历史榜单（mysql）

        // 3、查询榜单列表
        List<PointsBoard> list = isCurrent ?
                queryCurrentBoardList(key, query.getPageNo(), query.getPageSize()) :
                queryHistoryBosrdList(query);

        // 4、封装vo
        PointsBoardVO vo = new PointsBoardVO();
        // 处理我的信息
        if (myBoard != null) {
            vo.setPoints(myBoard.getPoints());
            vo.setRank(myBoard.getRank());
        }

        if (CollUtils.isEmpty(list)) {
            return vo;
        }
        Set<Long> userIds = list.stream().map(PointsBoard::getUserId).collect(Collectors.toSet());
        List<UserDTO> userDTOS = userClient.queryUserByIds(userIds);
        Map<Long, String> uMap = new HashMap<>(userIds.size());
        if (CollUtils.isNotEmpty(userDTOS)) {
            uMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        }
        List<PointsBoardItemVO> boardList = new ArrayList<>(list.size());
        for (PointsBoard p : list) {
            PointsBoardItemVO item = new PointsBoardItemVO();
            item.setRank(p.getRank());
            item.setPoints(p.getPoints());
            item.setName(uMap.get(p.getUserId()));
            boardList.add(item);
        }
        vo.setBoardList(boardList);
        return vo;
    }

    @Override
    public void createPointsBoardTable(Integer season) {
        baseMapper.createPointsBoardTable(LessonConstants.POINTS_BOARD_TABLE_PREFIX + season);
    }

    private List<PointsBoard> queryHistoryBosrdList(PointsBoardQuery query) {
        return null;
    }

    @Override
    public List<PointsBoard> queryCurrentBoardList(String key, Integer pageNo, Integer pageSize) {
        /**
         * 1、计算分页，分页是从0开始的，因此需要减去1
         *      计算出从第几个元素开始查询
         */
        int from = (pageNo - 1) * pageSize;
        /**
         * key: 查询指定建（key）的有序集合
         * 查询范围是从 from 到 from + pageSize - 1,即当前页的数据
         * reverseRangeWithScores:返回的是一个包含用户ID和分数的 TypedTuple集合，按分数从高到低排序
         */
        Set<ZSetOperations.TypedTuple<String>> tuples = redisTemplate.opsForZSet()
                .reverseRangeWithScores(key, from, from + pageSize - 1);

        if (CollUtils.isEmpty(tuples)) {
            return CollUtils.emptyList();
        }

        // 3、封装
        // 因为 redis 的排名是从 0 开始的，而通常展示时排名从1开始
        int rank = from + 1;
        List<PointsBoard> list = new ArrayList<>(tuples.size());
        for (ZSetOperations.TypedTuple<String> tuple : tuples) {
            String userId = tuple.getValue();
            Double points = tuple.getScore();
            if (userId == null || points == null) {
                continue;
            }
            PointsBoard p = new PointsBoard();
            p.setUserId(Long.valueOf(userId));
            p.setPoints(points.intValue());
            p.setRank(rank++);
            list.add(p);
        }
        return list;
    }


    private PointsBoard queryMyHistoryBoard(Long season) {
        return null;
    }

    private PointsBoard queryMyCurrentBoard(String key) {
        // 绑定key
        BoundZSetOperations<String, String> ops = redisTemplate.boundZSetOps(key);
        // 获取当前用户信息，因为存储在redis中都会被序列化为json字符串
        String userId = UserContext.getUser().toString();
        // 3、查询积分
        Double points = ops.score(userId);
        /**
         * 4、查询排名: 查询当前用户在有序集合中的逆序排名（从高到低的排名）
         *             redis的排序是从0开始的，因此实际排名需要加1
         */
        Long rank = ops.reverseRank(userId);
        // 5、封装返回
        PointsBoard p = new PointsBoard();
        p.setPoints(points == null ? 0 : points.intValue());
        p.setRank(rank == null ? 0 : rank.intValue() + 1);
        return p;
    }
}
