package com.zhsj.business.rate.dto;

import lombok.Data;

/**
 * @className: RateDto
 * @description: TODO
 * @author: yrt
 * date: 2022/5/7 17:10
 * version 1.0
 **/
@Data
public class RateDto {
    private String studentNo;
    private String studentName;
    private String className;
    private Integer rateStatus;
}
