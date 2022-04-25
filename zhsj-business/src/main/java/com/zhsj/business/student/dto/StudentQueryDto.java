package com.zhsj.business.student.dto;

import lombok.Data;

/**
 * 学生查询dto
 */
@Data
public class StudentQueryDto {
    private String studentName;
    private String studentNo;
    private Integer studentGroup;
}
