package com.zhsj.business.student.dto;

import lombok.Data;

/**
 * 学生查询dto
 */
@Data
public class StudentQueryDto {
    private String studentName;
    private Integer studentNo;
    private Integer studentGroup;
}
