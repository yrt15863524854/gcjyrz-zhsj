package com.zhsj.business.point.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.business.point.domain.PointDetailPO;
import com.zhsj.business.point.dto.ExcelDto;
import com.zhsj.business.point.dto.ExcelQueryDto;
import com.zhsj.business.point.dto.PointDetailDto;
import com.zhsj.business.point.dto.PointDetailQueryDto;
import com.zhsj.common.annotation.Excel;

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
}
