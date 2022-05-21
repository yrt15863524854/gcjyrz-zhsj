package com.zhsj.business.rate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.business.rate.domain.RatePO;
import com.zhsj.business.rate.dto.RateDto;
import com.zhsj.business.rate.dto.RateQueryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @interfaceName: RateService
 * @description: TODO
 * @author: yrt
 * date: 2022/5/6 23:05
 * version 1.0
 **/
public interface RateService extends IService<RatePO> {
    List<RateDto> listRate(RateQueryDto dto);
}
