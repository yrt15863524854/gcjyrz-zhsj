package com.zhsj.business.courseClassTeacher.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.business.courseClassTeacher.domain.CourseClassTeacherPO;
import com.zhsj.business.courseClassTeacher.dto.CourseClassTeacherDto;
import com.zhsj.business.courseClassTeacher.dto.CourseClassTeacherQueryDto;
import com.zhsj.common.core.domain.AjaxResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CourseClassTeacherService extends IService<CourseClassTeacherPO>{
    AjaxResult entryInformation(CourseClassTeacherDto courseClassTeacherDto);
    List<CourseClassTeacherDto> getCCTList(CourseClassTeacherQueryDto dto);
    CourseClassTeacherDto getSingle(@Param("dto")CourseClassTeacherDto dto);
}
