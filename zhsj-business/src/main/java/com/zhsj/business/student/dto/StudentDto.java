package com.zhsj.business.student.dto;

import lombok.Data;

/**
 * studentDto层
 */
@Data
public class StudentDto {
    /**
     * 学生id
     */
    private Integer id;
    /**
     * 学生编码
     */
    private String studentCode;
    /**
     * 学生姓名
     */
    private String studentName;
    /**
     * 学号
     */
    private String  studentNo;
    /**
     * 学生班级名称
     */
    private String studentClass;
    /**
     * 学生班级
     */
    private String classCode;
    /**
     * 学生组号
     */
    private Integer studentGroup;
    /**
     * 是否为组长
     */
    private Integer leader;
    /**
     * 考勤状态
     */
    private Integer studentKaoqin;
}
