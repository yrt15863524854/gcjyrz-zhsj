package com.zhsj.business.kaoqin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.business.kaoqin.domain.ClassInfoPO;
import com.zhsj.business.kaoqin.dto.ClassInfoDto;

import java.util.List;

public interface ClassInfoMapper extends BaseMapper<ClassInfoPO> {
    List<ClassInfoDto> listClassInfo();
}
