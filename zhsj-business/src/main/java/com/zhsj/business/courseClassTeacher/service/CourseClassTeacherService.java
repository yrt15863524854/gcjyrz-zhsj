package com.zhsj.business.courseClassTeacher.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.business.courseClassTeacher.domain.CourseClassTeacherPO;
import com.zhsj.business.courseClassTeacher.dto.CourseClassTeacherDto;
import com.zhsj.common.core.domain.AjaxResult;

public interface CourseClassTeacherService extends IService<CourseClassTeacherPO>{
    AjaxResult entryInformation(CourseClassTeacherDto courseClassTeacherDto);
}
