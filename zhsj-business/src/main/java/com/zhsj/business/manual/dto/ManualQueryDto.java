package com.zhsj.business.manual.dto;

import lombok.Data;

@Data
public class ManualQueryDto {
    /**
     * 教师编码
     */
    private String teacherCode;
    /**
     * 班级编码
     */
    private String classCode;
    /**
     * 课程编码
     */
    private String courseCode;
    /**
     * 学生学号
     */
    private String studentNo;
    /**
     * 学生组号
     */
    private String studentGroup;
}
