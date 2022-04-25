package com.zhsj.business.point.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.business.point.domain.ScorePO;
import com.zhsj.business.point.dto.ScoreDto;
import com.zhsj.business.point.dto.ScoreQueryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @interfaceName: ScoreMapper
 * @description: TODO
 * @author: yrt
 * date: 2022/4/18 15:52
 * version 1.0
 **/
public interface ScoreMapper extends BaseMapper<ScorePO> {
    List<ScoreDto> getStudentScore(@Param("dto") ScoreQueryDto dto);
}
