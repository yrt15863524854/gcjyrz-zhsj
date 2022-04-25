package com.zhsj.business.point.dto;

import lombok.Data;

/**
 * @className: PointDetailDto
 * @description: TODO
 * @author: yrt
 * date: 2022/4/14 15:36
 * version 1.0
 **/
@Data
public class PointDetailDto {
    private Long id;
    private String studentNo;
    private String studentName;
    private String pointScore;
    private String pointCode;
    private String pointName;
    private String pointScoreRatio;
    private String pointRatio;
    private String studentScore;
    private String studentGroup;
    private String studentClass;
}
