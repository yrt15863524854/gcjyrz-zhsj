package com.zhsj.business.point.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhsj.common.core.domain.BaseEntityPO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @className: PointDetailPO
 * @description: TODO
 * @author: yrt
 * date: 2022/4/14 15:32
 * version 1.0
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("business_point_detail")
public class PointDetailPO extends BaseEntityPO {
    private Long id;
    private String pointCode;
    private String pointName;
    private String studentNo;
    private String pointScore;
    private String pointScoreRatio;
    private String classCode;
}
