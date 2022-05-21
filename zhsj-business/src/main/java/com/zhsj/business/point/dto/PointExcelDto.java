package com.zhsj.business.point.dto;

import lombok.Data;


/**
 * @className: PointExcelDto
 * @description: TODO
 * @author: yrt
 * date: 2022/4/27 22:58
 * version 1.0
 **/
@Data
public class PointExcelDto {
    private String ClassName;
    private String studentNo;
    private String studentName;
    private String pointName;
    private String pointScore;
    private String studentScore;
}
