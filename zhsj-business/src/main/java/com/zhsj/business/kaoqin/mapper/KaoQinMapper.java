package com.zhsj.business.kaoqin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.business.kaoqin.domain.KaoQinPO;
import com.zhsj.business.kaoqin.dto.KaoQinDto;
import com.zhsj.business.kaoqin.dto.KaoQinQueryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface KaoQinMapper extends BaseMapper<KaoQinPO> {
    /**
     * 添加考勤信息
     *
     * @param kaoQinPO 考勤信息对象
     */
    int addKaoQinInfo(@Param("kaoQinPO") KaoQinPO kaoQinPO);

    /**
     * 查询考勤信息
     *
     * @param dto 查询对象
     * @return 结果集
     */
    List<KaoQinDto> listKaoQinInfo(@Param("dto") KaoQinQueryDto dto);
}
