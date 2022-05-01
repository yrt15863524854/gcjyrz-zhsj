package com.zhsj.business.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import com.zhsj.business.point.dto.PointExcelDto;
import com.zhsj.business.point.service.PointService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @className: ExcelDataListener
 * @description: TODO
 * @author: yrt
 * date: 2022/4/29 10:03
 * version 1.0
 **/
@Slf4j
public class ExcelDataListener implements ReadListener<PointExcelDto> {

    private static final int BATCH_COUNT = 100;
    /**
     * 缓存的数据
     **/
    private List<PointExcelDto> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    private PointService pointService;
    public ExcelDataListener(PointService pointService) {
        this.pointService = pointService;
    }
    @Override
    public void invoke(PointExcelDto pointExcelDto, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", JSON.toJSONString(pointExcelDto));
        cachedDataList.add(pointExcelDto);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
//        pointService.save(cachedDataList);
        log.info("存储数据库成功！");
    }
}
