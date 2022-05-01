package com.zhsj.business.point.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.business.point.domain.PointDetailPO;
import com.zhsj.business.point.dto.ExcelQueryDto;
import com.zhsj.business.point.dto.PointExcelDto;
import com.zhsj.common.annotation.Excel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @className: PointDetailMapper
 * @description: TODO
 * @author: yrt
 * date: 2022/4/14 15:36
 * version 1.0
 **/
public interface PointDetailMapper extends BaseMapper<PointDetailPO> {
    List<PointExcelDto> listPointExcel(@Param("dto") ExcelQueryDto dto);
}
