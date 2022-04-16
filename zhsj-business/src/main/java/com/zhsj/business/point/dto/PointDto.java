package com.zhsj.business.point.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @className: PointDto
 * @description: TODO
 * @author: yrt
 * date: 2022/4/14 14:31
 * version 1.0
 **/
@Data
public class PointDto {
    private Long id;
    private String pointCode;
    private String pointName;
    private BigDecimal pointRatio;
}
