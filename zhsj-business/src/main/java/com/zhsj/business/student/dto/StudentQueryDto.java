package com.zhsj.business.student.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 学生查询dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentQueryDto {
    private String studentName;
    private String studentNo;
    private Integer studentGroup;
    private String classCode;
    private String teacherCode;
    private Integer[] ids;
}
