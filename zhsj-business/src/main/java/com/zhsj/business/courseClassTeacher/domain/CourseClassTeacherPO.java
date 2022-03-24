package com.zhsj.business.courseClassTeacher.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhsj.common.core.domain.BaseEntityPO;
import lombok.Data;

@Data
@TableName("business_course_teacher")
public class CourseClassTeacherPO extends BaseEntityPO {
    private Long id;
    private String courseCode;
    private String classCode;
    private String teacherCode;
}
