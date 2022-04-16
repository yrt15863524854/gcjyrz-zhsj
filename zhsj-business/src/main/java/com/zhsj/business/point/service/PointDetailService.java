package com.zhsj.business.point.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.business.point.domain.PointDetailPO;
import com.zhsj.business.point.dto.PointDetailDto;
import com.zhsj.business.point.dto.PointDetailQueryDto;

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
}
