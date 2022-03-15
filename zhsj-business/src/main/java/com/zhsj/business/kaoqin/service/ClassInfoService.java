package com.zhsj.business.kaoqin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.business.kaoqin.domain.ClassInfoPO;
import com.zhsj.business.kaoqin.dto.ClassInfoDto;

import java.util.List;

public interface ClassInfoService extends IService<ClassInfoPO> {
    List<ClassInfoDto> listClassInfo();
}
