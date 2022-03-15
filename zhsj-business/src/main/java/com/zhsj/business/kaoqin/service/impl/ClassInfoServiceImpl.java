package com.zhsj.business.kaoqin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.business.kaoqin.domain.ClassInfoPO;
import com.zhsj.business.kaoqin.dto.ClassInfoDto;
import com.zhsj.business.kaoqin.mapper.ClassInfoMapper;
import com.zhsj.business.kaoqin.service.ClassInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class ClassInfoServiceImpl extends ServiceImpl<ClassInfoMapper, ClassInfoPO> implements ClassInfoService {

    @Resource
    private ClassInfoMapper classInfoMapper;

    @Override
    public List<ClassInfoDto> listClassInfo() {
        return classInfoMapper.listClassInfo();
    }
}
