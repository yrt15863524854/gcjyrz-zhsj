package com.zhsj.business.manual.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.business.manual.domain.ManualPO;
import com.zhsj.business.manual.dto.ManualDto;
import com.zhsj.business.manual.dto.ManualQueryDto;
import com.zhsj.business.manual.mapper.ManualMapper;
import com.zhsj.business.manual.service.ManualService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ManualServiceImpl extends ServiceImpl<ManualMapper, ManualPO> implements ManualService {

    @Resource
    private ManualMapper manualMapper;
    @Override
    public List<ManualDto> ListManualInfo(ManualQueryDto manualQueryDto) {
        return manualMapper.listManualInfo(manualQueryDto);
    }
}
