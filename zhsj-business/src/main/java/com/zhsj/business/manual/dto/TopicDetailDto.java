package com.zhsj.business.manual.dto;

import lombok.Data;

/**
 * @className: TopicDetailDto
 * @description: TODO
 * @author: yrt
 * date: 2022/4/8 10:16
 * version 1.0
 **/
@Data
public class TopicDetailDto {
    private Long id;
    //html
    private String html;
    //text
    private String text;
    //学生组号
    private Integer studentGroup;
    //课程编码
    private String classCode;
    //学生学号
    private String studentNo;
}
