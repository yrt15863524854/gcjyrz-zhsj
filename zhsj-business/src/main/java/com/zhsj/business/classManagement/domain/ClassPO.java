package com.zhsj.business.classManagement.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhsj.common.core.domain.BaseEntityPO;
import lombok.Data;

/**
 * @className: ClassPO
 * @description: TODO
 * @author: yrt
 * date: 2022/4/29 13:29
 * version 1.0
 **/
@Data
@TableName("business_class")
public class ClassPO extends BaseEntityPO {
    private Long id;
    private String classCode;
    private String className;
    private String status;
}
