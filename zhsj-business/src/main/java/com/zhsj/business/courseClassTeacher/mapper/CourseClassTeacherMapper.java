package com.zhsj.business.courseClassTeacher.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.business.courseClassTeacher.domain.CourseClassTeacherPO;
import com.zhsj.business.courseClassTeacher.dto.CourseClassTeacherDto;
import com.zhsj.business.courseClassTeacher.dto.CourseClassTeacherQueryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CourseClassTeacherMapper extends BaseMapper<CourseClassTeacherPO> {
    int entryInformation(@Param("PO")CourseClassTeacherPO PO);
    List<CourseClassTeacherDto> getCCTList(@Param("dto") CourseClassTeacherQueryDto dto);
    int modifyInformation(@Param("PO")CourseClassTeacherPO PO);
    CourseClassTeacherDto getSingle(@Param("dto") CourseClassTeacherDto dto);
}
