package com.zhsj.business.kaoqin.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhsj.common.core.domain.BaseEntityPO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@TableName("business_class")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassInfoPO extends BaseEntityPO {
    private static final long serialVersionUID = 1L;
    /**
     * 班级id
     */
    private Long id;
    /**
     * 班级编码
     */
    private String classCode;
    /**
     * 班级名称
     */
    private String className;
    /**
     * 状态
     */
    private Integer status;
}
