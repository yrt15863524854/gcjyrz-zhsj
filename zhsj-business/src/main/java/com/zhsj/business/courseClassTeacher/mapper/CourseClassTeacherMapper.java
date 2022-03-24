package com.zhsj.business.courseClassTeacher.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.business.courseClassTeacher.domain.CourseClassTeacherPO;
import com.zhsj.business.courseClassTeacher.dto.CourseClassTeacherDto;
import org.apache.ibatis.annotations.Param;

public interface CourseClassTeacherMapper extends BaseMapper<CourseClassTeacherPO> {
    int entryInformation(@Param("dto")CourseClassTeacherDto courseClassTeacherDto);
}
