package com.zhsj.business.manual.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.business.manual.domain.ManualPO;
import com.zhsj.business.manual.dto.ManualDto;
import com.zhsj.business.manual.dto.ManualQueryDto;

import java.util.List;

public interface ManualService extends IService<ManualPO> {
    List<ManualDto> ListManualInfo(ManualQueryDto manualQueryDto);
}
