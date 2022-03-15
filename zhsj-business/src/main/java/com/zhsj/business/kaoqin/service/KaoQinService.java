package com.zhsj.business.kaoqin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.business.kaoqin.domain.KaoQinPO;
import com.zhsj.business.kaoqin.dto.KaoQinDto;
import com.zhsj.business.kaoqin.dto.KaoQinQueryDto;

import java.util.List;


public interface KaoQinService extends IService<KaoQinPO> {
    void addKaoQinInfo(KaoQinDto kaoQinDto);
    List<KaoQinDto> selectKaoQinInfo(KaoQinQueryDto kaoQinQueryDto);
}
