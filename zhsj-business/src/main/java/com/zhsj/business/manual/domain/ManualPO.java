package com.zhsj.business.manual.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhsj.common.core.domain.BaseEntityPO;
import lombok.Data;

@TableName("business_manual")
@Data
public class ManualPO extends BaseEntityPO {
    private Long id;
    private String manualCode;
    private String manualName;
    private String courseCode;
    private String studentGroup;
}
