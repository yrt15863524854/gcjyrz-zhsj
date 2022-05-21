package com.zhsj.business.rate.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @className: RatePO
 * @description: TODO
 * @author: yrt
 * date: 2022/5/6 23:00
 * version 1.0
 **/
@TableName("business_rate")
@Data
public class RatePO {
    private Long id;
    private String studentNo;
    private String classCode;
    private Integer questionOne;
    private Integer questionTow;
    private Integer questionThree;
    private Integer questionFour;
    private Integer questionFive;
    private Integer questionSix;
    private Integer questionSeven;
    private Integer questionEight;
    private Integer rateStatus;
}
