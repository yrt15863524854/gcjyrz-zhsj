package com.zhsj.business.point.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhsj.common.annotation.Excel;
import com.zhsj.common.core.domain.BaseEntityPO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @className: PointPO
 * @description: TODO
 * @author: yrt
 * date: 2022/4/14 14:32
 * version 1.0
 **/
@EqualsAndHashCode(callSuper = true)
@TableName("business_point")
@Data
public class PointPO extends BaseEntityPO {
    @Excel
    private Long id;
    @Excel
    private String pointCode;
    @Excel
    private String pointName;
    @Excel
    private BigDecimal pointRatio;
    @Excel
    private String pointAttribute;
    @Excel
    private String pointTarget;
}
