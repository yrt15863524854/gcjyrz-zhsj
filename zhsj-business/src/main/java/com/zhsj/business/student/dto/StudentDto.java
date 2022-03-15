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
    private Integer studentNo;
    /**
     * 学生班级
     */
    private String studentClass;
    /**
     * 学生组号
     */
    private Integer studentGroup;
}
