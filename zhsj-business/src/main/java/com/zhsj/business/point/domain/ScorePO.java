package com.zhsj.business.point.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhsj.common.core.domain.BaseEntityPO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @className: ScorePO
 * @description: TODO
 * @author: yrt
 * date: 2022/4/18 15:50
 * version 1.0
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("business_score")
public class ScorePO extends BaseEntityPO {
    private Long id;
    private String studentNo;
    private String studentScore;
    private Integer studentGroup;
}
