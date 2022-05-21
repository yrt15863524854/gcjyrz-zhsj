package com.zhsj.business.rate.dto;

import lombok.Data;

/**
 * @className: RateQueryDto
 * @description: TODO
 * @author: yrt
 * date: 2022/5/7 17:18
 * version 1.0
 **/
@Data
public class RateQueryDto {
    private Integer rateStatus;
    private String classCode;
    private String teacherCode;
}
