package com.zhsj.business.point.dto;

import lombok.Data;

/**
 * @className: ScoreDto
 * @description: TODO
 * @author: yrt
 * date: 2022/4/23 15:44
 * version 1.0
 **/
@Data
public class ScoreDto {
    private Long id;
    private String studentNo;
    private String studentName;
    private String className;
    private String studentGroup;
    private String studentScore;
}
