package com.zhsj.business.point.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhsj.common.core.domain.BaseEntityPO;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @className: PointPO
 * @description: TODO
 * @author: yrt
 * date: 2022/4/14 14:32
 * version 1.0
 **/
@TableName("business_point")
@Data
public class PointPO extends BaseEntityPO {
    private Long id;
    private String pointCode;
    private String pointName;
    private BigDecimal pointRatio;
}
