package com.zhsj.business.course.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.business.course.domain.CoursePO;
import com.zhsj.business.course.mapper.CourseMapper;
import com.zhsj.business.course.service.CourseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, CoursePO> implements CourseService {

    @Resource
    private CourseMapper courseMapper;

    @Override
    public void m1() {

    }
}
