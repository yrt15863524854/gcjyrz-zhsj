package com.zhsj.business.manual.dto;

import lombok.Data;

/**
 * @className: TopicDetailQueryDto
 * @description: TODO
 * @author: yrt
 * date: 2022/4/8 10:16
 * version 1.0
 **/
@Data
public class TopicDetailQueryDto {
    private Long id;
    private Integer studentGroup;
    private String classCode;
    private String studentNo;
}
