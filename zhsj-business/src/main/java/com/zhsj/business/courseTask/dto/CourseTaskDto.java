package com.zhsj.business.courseTask.dto;

import lombok.Data;

/**
 * @className: CourseTaskDto
 * @description: TODO
 * @author: yrt
 * date: 2022/4/21 14:11
 * version 1.0
 **/
@Data
public class CourseTaskDto {
    private Long id;
    private String courseTaskCode;
    private String courseCode;
    private String courseName;
    private String courseNature;
    private String courseTaskName;
    private String courseBriefly;
    private String courseTaskHtml;
    private String createBy;
}
