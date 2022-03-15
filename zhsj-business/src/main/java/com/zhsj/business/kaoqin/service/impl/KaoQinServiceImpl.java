package com.zhsj.business.kaoqin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.business.kaoqin.domain.KaoQinPO;
import com.zhsj.business.kaoqin.dto.KaoQinDto;
import com.zhsj.business.kaoqin.dto.KaoQinQueryDto;
import com.zhsj.business.kaoqin.mapper.KaoQinMapper;
import com.zhsj.business.kaoqin.service.KaoQinService;
import com.zhsj.common.exception.base.BaseException;
import com.zhsj.common.utils.DateUtils;
import com.zhsj.common.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class KaoQinServiceImpl extends ServiceImpl<KaoQinMapper, KaoQinPO> implements KaoQinService {

    @Resource
    private KaoQinMapper kaoQinMapper;

    @Override
    public void addKaoQinInfo(KaoQinDto kaoQinDto) {
        KaoQinPO kaoQinPO = new KaoQinPO();
        kaoQinPO.setKaoqinStudent(kaoQinDto.getKaoqinStudent());
        int status = Integer.parseInt(kaoQinDto.getKaoqinStatus());
        kaoQinPO.setKaoqinStatus(status);
        kaoQinPO.setStudentNo(kaoQinDto.getStudentNo());
        kaoQinPO.setClassCode(kaoQinDto.getClassCode());
        String info;
        if (status == 0){
            info = DateUtils.getTime() + kaoQinDto.getKaoqinStudent() + "出勤";
            kaoQinPO.setKaoqinInfo(info);
        } else if (status == 1){
            info = DateUtils.getTime() + kaoQinDto.getKaoqinStudent() + "缺勤";
            kaoQinPO.setKaoqinInfo(info);
        } else {
            info = DateUtils.getTime() + kaoQinDto.getKaoqinStudent() + "请假";
            kaoQinPO.setKaoqinInfo(info);
        }
        kaoQinPO.setCreateBy(SecurityUtils.getUsername());
        kaoQinPO.setCreateTime(DateUtils.getNowDate());
        int result = kaoQinMapper.addKaoQinInfo(kaoQinPO);
        if (result != 1){
            throw new BaseException("添加考勤信息失败！！！");
        }
    }

    @Override
    public List<KaoQinDto> selectKaoQinInfo(KaoQinQueryDto kaoQinQueryDto) {
        return kaoQinMapper.listKaoQinInfo(kaoQinQueryDto);
    }
}
