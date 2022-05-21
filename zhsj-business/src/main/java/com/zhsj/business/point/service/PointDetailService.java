package com.zhsj.business.point.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.business.point.domain.PointDetailPO;
import com.zhsj.business.point.dto.*;
import com.zhsj.common.annotation.Excel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * @interfaceName: PointDetailService
 * @description: TODO
 * @author: yrt
 * date: 2022/4/14 15:37
 * version 1.0
 **/
public interface PointDetailService extends IService<PointDetailPO> {
    void insertPointDetail(PointDetailDto dto);
    void updatePointDetail(PointDetailDto dto);
    PointDetailPO getOneDataById(PointDetailQueryDto dto);
    List<ExcelDto> listExcel(ExcelQueryDto dto);
    PointComments getPointCommentsByNo(String studentNo);
    /**
     * @description degreeOfAchievement 生成课程设计达成度excel文件
     * @date 2022/5/3 23:24
     * @param dto
     * @return void
     * @author yrt
     **/
    void degreeOfAchievement(ExcelQueryDto dto) throws IOException;
    /**
     * @description achievementReport 生成达成度报告word文件
     * @date 2022/5/6 19:32
     * @param dto
     * @return void
     * @author yrt
     **/
    void achievementReport(HttpServletResponse response,ExcelQueryDto dto) throws IOException, InvalidFormatException;
}
