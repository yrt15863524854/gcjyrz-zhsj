package com.zhsj.business.rate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.business.courseClassTeacher.domain.CourseClassTeacherPO;
import com.zhsj.business.courseClassTeacher.mapper.CourseClassTeacherMapper;
import com.zhsj.business.kaoqin.dto.KaoQinDto;
import com.zhsj.business.rate.domain.RatePO;
import com.zhsj.business.rate.dto.RateDto;
import com.zhsj.business.rate.dto.RateQueryDto;
import com.zhsj.business.rate.mapper.RateMapper;
import com.zhsj.business.rate.service.RateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @className: RateServiceImpl
 * @description: TODO
 * @author: yrt
 * date: 2022/5/6 23:05
 * version 1.0
 **/
@Service
public class RateServiceImpl extends ServiceImpl<RateMapper, RatePO> implements RateService {
    @Resource
    private RateMapper rateMapper;
    @Resource
    private CourseClassTeacherMapper courseClassTeacherMapper;

    @Override
    public List<RateDto> listRate(RateQueryDto dto) {
        List<RateDto> list = new ArrayList<>();
        QueryWrapper<CourseClassTeacherPO> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_code", dto.getTeacherCode());
        List<CourseClassTeacherPO> courseClassTeacherPOS = courseClassTeacherMapper.selectList(wrapper);
        List<String> classList = courseClassTeacherPOS.stream().map(CourseClassTeacherPO::getClassCode).collect(Collectors.toList());
        for (String classCode : classList) {
            dto.setClassCode(classCode);
            List<RateDto> tem = rateMapper.listRate(dto);
            list.addAll(tem);
        }
        return list;
    }
}
