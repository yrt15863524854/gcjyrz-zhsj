package com.zhsj.business.point.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.business.point.domain.PointPO;
import com.zhsj.business.point.dto.PointDto;

import java.util.Map;

/**
 * @interfaceName: PointService
 * @description: TODO
 * @author: yrt
 * date: 2022/4/14 14:37
 * version 1.0
 **/
public interface PointService extends IService<PointPO> {
    Map<String, String> insertPoint(PointDto dto);
    Map<String, String> updatePoint(PointDto dto);
}
