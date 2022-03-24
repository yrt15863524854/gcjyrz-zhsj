package com.zhsj.business.courseClassTeacher.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.business.courseClassTeacher.domain.CourseClassTeacherPO;
import com.zhsj.business.courseClassTeacher.dto.CourseClassTeacherDto;
import com.zhsj.business.courseClassTeacher.mapper.CourseClassTeacherMapper;
import com.zhsj.business.courseClassTeacher.service.CourseClassTeacherService;
import com.zhsj.common.core.domain.AjaxResult;
import com.zhsj.common.exception.base.BaseException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CourseClassTeacherServiceIpl extends ServiceImpl<CourseClassTeacherMapper, CourseClassTeacherPO> implements CourseClassTeacherService {
    @Resource
    private CourseClassTeacherMapper courseClassTeacherMapper;

    @Override
    public AjaxResult entryInformation(CourseClassTeacherDto courseClassTeacherDto) {
        int result = courseClassTeacherMapper.entryInformation(courseClassTeacherDto);
        if (result != 1){
            throw new BaseException("添加失败");
        }
        return AjaxResult.success("添加成功");
    }
}
