package com.zhsj.business.rate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.business.rate.domain.RatePO;
import com.zhsj.business.rate.dto.RateDto;
import com.zhsj.business.rate.dto.RateQueryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @interfaceName: RateMapper
 * @description: TODO
 * @author: yrt
 * date: 2022/5/6 23:04
 * version 1.0
 **/
public interface RateMapper extends BaseMapper<RatePO> {
    List<RateDto> listRate(@Param("dto") RateQueryDto dto);
}
