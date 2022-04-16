package com.zhsj.business.courseClassTeacher.dto;

import lombok.Data;

@Data
public class CourseClassTeacherDto {
    private Long id;
    private String courseName;
    private String className;
    private String teacherName;
    private String courseCode;
    private String classCode;
    private String teacherCode;
}
