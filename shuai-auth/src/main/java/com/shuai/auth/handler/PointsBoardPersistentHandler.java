package com.shuai.auth.handler;

import com.shuai.auth.constants.LessonConstants;
import com.shuai.auth.constants.RedisConstants;
import com.shuai.auth.domain.po.PointsBoard;
import com.shuai.auth.service.IPointsBoardSeasonService;
import com.shuai.auth.service.IPointsBoardService;
import com.shuai.auth.util.TableInfoContext;
import com.shuai.common.utils.CollUtils;
import com.shuai.common.utils.DateUtils;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务生成榜单表
 */
@Component
@RequiredArgsConstructor
public class PointsBoardPersistentHandler {

    private final IPointsBoardService pointsBoardService;
    private final IPointsBoardSeasonService pointsBoardSeasonService;
    private final StringRedisTemplate redisTemplate;

    // @Scheduled(cron = "0 0 3 1 * ?") // 每月1号，凌晨3点执行
    @XxlJob("createTableJob")
    public void createPointsBoardTableOfLastSeason() {
        // 1、获取上月时间
        LocalDateTime time = LocalDateTime.now().minusMonths(1);
        // 2、查询赛季id
        Integer season = pointsBoardSeasonService.querySeasonByTime(time);
        if (season == null) {
            return;
        }
        // 3、创建表
        pointsBoardService.createPointsBoardTable(season);
    }

    @XxlJob("savePointBoard2DB")
    public void savePointBoard2DB() {
        // 1.获取上个月时间
        LocalDateTime time = LocalDateTime.now().minusMonths(1);

        // 2.计算动态表名
        // 2.1、查询赛季信息
        Integer season = pointsBoardSeasonService.querySeasonByTime(time);

        // 2.2、将表名存入ThreadLocal
        TableInfoContext.steInfo(LessonConstants.POINTS_BOARD_TABLE_PREFIX + season);

        // 3、查询榜单数据
        // 3.1、拼接KEY
        String key = RedisConstants.POINTS_BOARD_KEY_PREFIX + time.format(DateUtils.POINTS_BOARD_SUFFIX_FORMATTER);
        // 3.2、查询数据
        // int pageNo = 1;
        int index = XxlJobHelper.getShardIndex();
        int total = XxlJobHelper.getShardTotal();
        int pageNo = index + 1;  // 起始页，就是分片序号 +1
        int pageSize = 1000;

        while (true) {
            List<PointsBoard> boardList =  pointsBoardService.queryCurrentBoardList(key, pageNo, pageSize);
            if (CollUtils.isEmpty(boardList)) {
                break;
            }

            // 4.持久化到数据库
            // 4.1、把排名信息写入id
            boardList.forEach(b->{
                b.setId(b.getRank().longValue());
                b.setRank(null);
            });

            // 4.2、持久化
            pointsBoardService.saveBatch(boardList);
            // 翻页，跳过N个页，N就是分片数量
            // pageNo++;
            pageNo += total;
        }
        // 任务结束，移除动态表名
        TableInfoContext.remove();
    }

    /**
     * 清除 Redis 缓存
     */
    @XxlJob("clearPointsBoardFormRedis")
    public void clearPointsBoardFormRedis() {
        // 1.获取上个月时间
        LocalDateTime time = LocalDateTime.now().minusMonths(1);
        // 2.拼接KEY
        String key = RedisConstants.POINTS_BOARD_KEY_PREFIX + time.format(DateUtils.POINTS_BOARD_SUFFIX_FORMATTER);
        // 3.删除
        redisTemplate.delete(key);
    }

}
